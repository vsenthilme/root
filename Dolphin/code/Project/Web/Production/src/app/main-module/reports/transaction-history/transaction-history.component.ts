import { Component, OnInit, ViewChild } from '@angular/core';
import { ReportsService } from '../reports.service';
import { SelectionModel } from '@angular/cdk/collections';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
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

@Component({
  selector: 'app-transaction-history',
  templateUrl: './transaction-history.component.html',
  styleUrls: ['./transaction-history.component.scss']
})
export class TransactionHistoryComponent implements OnInit {

  isShowDiv = false;
  table = false;
  fullscreen = false;
  search = true;
  back = false;



  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
  showFiller = false;

  displayedColumns: string[] = ['warehouseId', 'itemCode','itemDescription',  'openingStock', 'inboundQty', 'outboundQty','stockAdjustmentQty','closingStock','systemInventory'];
  sub = new Subscription();
  ELEMENT_DATA: any[] = [];


  constructor(public dialog: MatDialog,
    private http: HttpClient,
    // private cas: CommonApiService,
    private fb: FormBuilder,
    public toastr: ToastrService,
    private router: Router,
    private spin: NgxSpinnerService,
    public cs: CommonService,
    private service: ReportsService,
  private masterService: MasterService,
    private auth: AuthService) { }
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
    this.dropdownfill();
  }

  multiselectWarehouseList: any[] = [];
  multiselectItemCodeList: any[] = [];
  itemCodeList: any[] = [];
  
  onItemType(searchKey) {
    let searchVal = searchKey?.filter;
    if (searchVal !== '' && searchVal !== null) {
      forkJoin({
        itemList: this.service.getItemCodeDropDown2(searchVal.trim(),this.auth.companyId,this.auth.plantId,this.auth.warehouseId,this.auth.languageId).pipe(catchError(err => of(err))),
      })
        .subscribe(({ itemList }) => {
          if (itemList != null && itemList.length > 0) {
            this.multiselectItemCodeList = [];
            this.itemCodeList = itemList;
            this.itemCodeList.forEach(x => this.multiselectItemCodeList.push({ value: x.itemCode, label: x.itemCode + ' - ' + x.description }))
          }
        });
    }
  }

  warehouseList: any[] = [];
  selectedWarehouseList: any[] = [];
  selectedItems: any[] = [];
  multiWarehouseList: any[] = [];

  dropdownfill() {
    this.spin.show();
    forkJoin({
      warehouse: this.masterService.getWarehouseMasterDetails().pipe(catchError(err => of(err))),

    })
    .subscribe(({ warehouse }) => {
      if(this.auth.userTypeId != 3){
        this.warehouseList = warehouse.filter((x: any) => x.warehouseId == this.auth.warehouseId);
      }else{
        this.warehouseList = warehouse
      }
        this.warehouseList.forEach(x => this.multiWarehouseList.push({ value: x.warehouseId, label: x.warehouseId + (x.description == null ? '' : '- ' + x.description) }));
        this.multiselectWarehouseList = this.multiWarehouseList;
        this.multiselectWarehouseList.forEach((warehouse: any) => {
          if (warehouse.value == this.auth.warehouseId)
            this.searhform.controls.warehouseId.patchValue(warehouse.value);
        })
      }, (err) => {
        this.toastr.error(
          err,
          ""
        );
      });
    this.spin.hide();

  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
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


  searhform = this.fb.group({

    warehouseId: [],
    itemCode: [],
    itemCodeFE: [],
    fromCreatedOn: [,Validators.required],
    toCreatedOn: [,Validators.required],
  });

  totalRecords = 0;

  reset(){
    this.searhform.reset();
  }
  filtersearch(excel = false) {
this.spin.show();

    if (this.searhform.invalid) {
      this.toastr.error(
        "Please fill required fields to continue",
        ""
      );

      this.cs.notifyOther(true);
      this.spin.hide();
      return;
    }

      if(this.searhform.controls.itemCodeFE.value){
        this.searhform.controls.itemCode.patchValue([this.searhform.controls.itemCodeFE.value])
      }
    this.searhform.controls.fromCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.fromCreatedOn.value));
    this.searhform.controls.toCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.toCreatedOn.value));

    this.service.getTransactionHistoryReport(this.searhform.value).subscribe(res => {
      this.dataSource = new MatTableDataSource<any>(res);
      this.selection = new SelectionModel<any>(true, []);
      this.dataSource.sort = this.sort;

      console.log( this.dataSource.data.length)
      this.dataSource.paginator = this.paginator;
      this.totalRecords =   this.dataSource.data.length;
      this.dataSource.sort = this.sort;
      this.table = true;
      this.search = false;
      this.fullscreen = false;
      this.back = true;
      this.spin.hide();
    }, err => {

      this.cs.commonerror(err);
      this.spin.hide();

    });
  }
  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        'Warehouse Id': x.warehouseId,
        "Item Code": x.itemCode,
        "Item Description":x.itemDescription,
        'Opening Stock': x.openingStock,
        "Inbound Qty": x.inboundQty,
        "Outbound Qty": x.outboundQty,
        "Stock Adjustment Qty ": x.stockAdjustmentQty,
        "Closing Qty ": x.closingStock,
        "System Stock":x.systemInventory,
      });

    })
    this.cs.exportAsExcel(res, "Transaction History");
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

