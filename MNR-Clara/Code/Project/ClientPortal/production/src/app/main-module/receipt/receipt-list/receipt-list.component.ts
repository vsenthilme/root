import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { QuotationService } from '../../quotation/quotation.service';
import { HttpErrorResponse } from '@angular/common/http';
import { DomSanitizer } from '@angular/platform-browser';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { ReceiptService } from '../receipt.service';

@Component({
  selector: 'app-receipt-list',
  templateUrl: './receipt-list.component.html',
  styleUrls: ['./receipt-list.component.scss']
})
export class ReceiptListComponent implements OnInit {

  displayedColumns: string[] = ['matterNumber','receiptNo','receiptType','receiptDate','no','statusIddes'];
  sub = new Subscription();
  ELEMENT_DATA: any[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
  constructor(public dialog: MatDialog,
    private service: ReceiptService, private router: Router,
    public toastr: ToastrService, private route: ActivatedRoute,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService,
    private sanitizer: DomSanitizer,) { }
    
    routeto(url: any, id: any) {
      localStorage.setItem('crrentmenu', id);
      this.router.navigate([url]);
    }
  toggleFloat() {

    this.isShowDiv = !this.isShowDiv;
    this.toggle = !this.toggle;

    if (this.icon === 'expand_more') {
      this.icon = 'chevron_left';
    } else {
      this.icon = 'expand_more'
    }
    this.showFloatingButtons = !this.showFloatingButtons;

  }
  showFiller = false;
  animal: string | undefined;
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  ngOnInit(): void {
   // this.auth.isuserdata();

    this.getall();
  }

  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);

 
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
  @ViewChild(MatSort, { static: true })
  sort!: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator!: MatPaginator; // Pagination
 // Pagination
 statusList: any;
   multiselectstatusList: any[] = [];
   multistatusList: any[] = [];

   statuslist: any[] = [];

   getall() {

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: any[]) => {
        res.forEach((x) => {
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;

        })
       this.ELEMENT_DATA = res.filter(x => x.clientId == this.auth.clientId);
        this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<any>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();

  }


  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    if (this.isAllSelected()) {
      this.selection.clear();
      return;
    }

    this.selection.select(...this.dataSource.data);
  }

  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.matterNumber + 1}`;
  }


  clearselection(row: any) {

    this.selection.clear();
    this.selection.toggle(row);
  }

  quotationUrl(url :any){
    window.open(url, '_blank');
  }

  openPdf(data: any): void {
    let paramdata = this.cs.encrypt({ code: data });
console.log(data)
    this.router.navigate(['/main/quotations/quotationpdf/' + paramdata]);

  }

  docurl: any;
  fileUrldownload: any;

  async download(element) {
    this.spin.show()
    console.log(element);
    const blob = await this.service.getfile1(element.referenceField8,element.clientId,element.matterNumber)
      .catch((err: HttpErrorResponse) => {
        this.cs.commonerror(err);
      });
    this.spin.hide();
    if (blob) {
      const blobOb = new Blob([blob], {
        type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      });
      this.fileUrldownload = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blobOb));
      // http://remote.url.tld/path/to/document.doc&embedded=true
      this.docurl = window.URL.createObjectURL(blob);
      const a = document.createElement('a')
      a.href = this.docurl
      a.download = element.referenceField8;
      a.click();
      URL.revokeObjectURL(this.docurl);

    }
    this.spin.hide();
  }
}

 

