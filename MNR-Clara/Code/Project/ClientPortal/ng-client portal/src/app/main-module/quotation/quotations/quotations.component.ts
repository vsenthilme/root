import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { QuotationService } from "../quotation.service";

// export interface ordermanagement {
//   lineno: string;
//   partner: string;
//   product: string;
//   status: string; 
//   date:string;
  
//   }
  
//   const ELEMENT_DATA: ordermanagement[] = [
//   { lineno: '300001 -01',status: 'Pending for Approval', partner: '900001',date: '02-02-2022', product: 'Legal fees for filing I130',},
//   { lineno: '300001 -02',status: 'Approved', partner: '900001',date: '02-02-2022', product: 'Legal fees for H1B ',},
//   { lineno: '300001 -03',status: 'Approved', partner: '900001',date: '02-02-2022', product: 'Legal fees for H1B ',},
//   { lineno: '300001 -04',status: 'Approved', partner: '900001',date: '02-02-2022', product: 'Legal fess for N400 ',},
  
//   ];
@Component({
  selector: 'app-quotations',
  templateUrl: './quotations.component.html',
  styleUrls: ['./quotations.component.scss']
})
export class QuotationsComponent implements OnInit {
  displayedColumns: string[] = ['matterNumber','quotationNo','quotationDate','status','no','payment'];
  sub = new Subscription();
  ELEMENT_DATA: any[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
  constructor(public dialog: MatDialog,
    private service: QuotationService,
   // private cas: CommonApiService,
    public toastr: ToastrService,
    private router: Router, 
    private spin: NgxSpinnerService,
    private cs: CommonService,
   // private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
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

    this.getAll();
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

   

   getAll() {
    this.spin.show();

    this.sub.add(this.service.Getstatus().subscribe(res => {
      this.statusList = res;
      this.statusList.forEach((x: { statusId: string; status: string; }) => this.multistatusList.push({ id: x.statusId, itemName: x.status }))

      console.log(this.multistatusList)
      this.spin.hide();
    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
  this.spin.show();
  this.sub.add(this.service.Search({ clientId: [this.auth.clientId] }).subscribe((res: any[]) => {
    res.forEach((x) => {
      x.statusId = this.multistatusList.find(z => z.id == x.statusId)?.itemName;
    })
    let result = res.filter(x => x.referenceField10 == "Sent");
    this.ELEMENT_DATA = result;
    this.dataSource = new MatTableDataSource<any>(result);
    this.selection = new SelectionModel<any>(true, []);
    this.dataSource.sort = this.sort;
   this.dataSource.paginator = this.paginator;
    this.spin.hide();
  }, err => {
    this.cs.commonerror(err);
    this.spin.hide();
  }));
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

}

 

