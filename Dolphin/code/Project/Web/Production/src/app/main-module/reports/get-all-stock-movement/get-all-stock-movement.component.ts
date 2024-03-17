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
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ReportsService } from '../reports.service';

@Component({
  selector: 'app-get-all-stock-movement',
  templateUrl: './get-all-stock-movement.component.html',
  styleUrls: ['./get-all-stock-movement.component.scss']
})
export class GetAllStockMovementComponent implements OnInit {
  isShowDiv = false;
  table = true;
  fullscreen = true;
  search = false;
  back = false;



  showFloatingButtons: any;
  toggle = true;
  public icon = 'exand_more';
  showFiller = false;
  displayedColumns: string[] = ['warehouseId', 'manufacturerSKU', 'itemCode', 'itemText', 'documentType', 'documentNumber', 'newmMovementQty', 'customerCode', 'confirmedOn', 'confirmedOn1'];
  sub = new Subscription();
  ELEMENT_DATA: any[] = [];


  constructor(public dialog: MatDialog,
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
      this.reportService.getallStockMovement().subscribe(res => {
        res.sort((a, b) => (a.confirmedOn > b.confirmedOn) ? 1 : -1)
        this.dataSource = new MatTableDataSource<any>(res);
        this.dataSource = new MatTableDataSource<any>(res);
        this.dataSource.data.forEach((x) => {
          if (x.documentType == "OutBound") {
            x['newmMovementQty'] = '-' + (x.movementQty)
          }
          if (x.documentType == "InBound") {
            x['newmMovementQty'] = '+' + (x.movementQty)
          }
        })
        
        this.selection = new SelectionModel<any>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.table = true;
        this.search = false;
        this.fullscreen = false;
        this.back = false;
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
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
        "Warehouse": x.warehouseId,
        'MFR Part No': x.manufacturerSKU,
        'Item Code': x.itemCode,
        "Item Description": x.itemText,
        "Document Type": x.documentType,
        "Document Number": x.documentNumber,
      //  "Opening Qty": x.openingStock,
        'Movement Quantity': x.newmMovementQty,
     //   'Balance OH Qty': x.balanceOHQty,
        'Customer Code': x.customerCode,
        'Date': this.cs.dateMMYY(x.confirmedOn),
        'Time': this.cs.dateHHSS(x.confirmedOn),
      });

    })
    this.cs.exportAsExcel(res, "Stock Movement Report - New");
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
