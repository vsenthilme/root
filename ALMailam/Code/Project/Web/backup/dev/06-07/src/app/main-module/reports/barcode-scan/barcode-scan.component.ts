import { SelectionModel } from '@angular/cdk/collections';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription, forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { MasterService } from 'src/app/shared/master.service';
import { ContainerReceiptService } from '../../Inbound/Container-receipt/container-receipt.service';
import { PickingService } from '../../Outbound/picking/picking.service';
import { ReportsService } from '../reports.service';
import { StocksampleService } from '../stocksamplereport/stocksample.service';
import { threadId } from 'worker_threads';

@Component({
  selector: 'app-barcode-scan',
  templateUrl: './barcode-scan.component.html',
  styleUrls: ['./barcode-scan.component.scss']
})
export class BarcodeScanComponent implements OnInit {
  isShowDiv = false;
  table = true;
  fullscreen = true;
  search = false;
  back = false;



  showFloatingButtons: any;
  toggle = true;
  public icon = 'exand_more';
  showFiller = false;
          displayedColumns: string[] =  ['Branch','barcode','itemCode', 'storageBin', 'referenceField1','createdBy', 'createdOn', ];
  sub = new Subscription();
  ELEMENT_DATA: any[] = [];


  constructor(public dialog: MatDialog,
    private service: StocksampleService,
    private http: HttpClient,
    // private cas: CommonApiService,
    private fb: FormBuilder,
    public toastr: ToastrService,
    private router: Router,
    private spin: NgxSpinnerService,
    public cs: CommonService, private reportService: ReportsService,
    private auth: AuthService,) { }

  routeto(url: any, id: any) {
    sessionStorage.setItem('crrentmenu', id);
    this.router.navigate([url]);
  }
  animal: string | undefined;
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  ngOnInit(): void {
    this.searchBarcodeScan();
  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }


  searchBarcodeScan(){
    this.spin.show();
      this.reportService.barCodeScan().subscribe(res => {
        console.log(res)
        this.dataSource = new MatTableDataSource<any>(res);
        this.selection = new SelectionModel<any>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.table = true;
        this.search = false;
        this.fullscreen = false;
        this.back = false;
        this.spin.hide();
      }, err => {
        this.cs.commonerrorNew(err);
        this.spin.hide();
      });
  }



  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);


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

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }
  }

  @ViewChild(MatSort, { static: true })
  sort!: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination
  // Pagination




  totalRecords = 0;
  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Branch": x.createdBy == "amghara1" ? 'JSP' : x.createdBy == "amghara" ? 'ASP' : x.createdBy == "amghara2" ? 'JSP' : x.createdBy == "amghara3" ? 'ASP' : '',
        "Barcode ": x.barcode,
        "Product Code":  x.itemCode,
        "Bin Location":  x.storageBin,
        'Mfr Code': x.referenceField1,
        'Created By': x.createdBy,
        "Created On": this.cs.dateExcel(x.createdOn),
      });

    })
    this.cs.exportAsExcel(res, "Barcode Scan");
  }


  togglesearch() {
    this.search = false;
    this.table = true;
    this.fullscreen = false;
    this.back = true;
  }
  backsearch() {
    this.table = true;
    this.search = true;
    this.fullscreen = true;
    this.back = false;
  }


  onItemSelect(item: any) {
  }

  onSelectAll(items: any) {
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.warehouseId + 1}`;
  }



  clearselection(row: any) {

    this.selection.clear();
    this.selection.toggle(row);
  }


}
