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
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { MasterService } from 'src/app/shared/master.service';
import { PreinboundService } from '../../Inbound/preinbound/preinbound.service';
import { InboundConfirmationService } from '../../Inbound/inbound-confirmation/inbound-confirmation.service';
import { catchError } from 'rxjs/operators';
import { ReportsService } from '../reports.service';

@Component({
  selector: 'app-preinbound',
  templateUrl: './preinbound.component.html',
  styleUrls: ['./preinbound.component.scss']
})
export class PreinboundComponent implements OnInit {
  screenid=3185;
  isShowDiv = false;
  table = false;
  fullscreen = false;
  search = true;
  back = false;



  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
  showFiller = false;

 // displayedColumns: string[] = [ 'statusId','itemCode','proposedStorageBin', 'confirmedStorageBin', 'packBarcodes', 'putAwayQuantity', 'putawayConfirmedQty', 'confirmedBy','confirmedOn','createdOn', 'leadTime'];
      //wms demo
      displayedColumns: string[] = ['refDocNumber', 'lineNo', 'itemCode', 'description', 'orderedQuantity', 'acceptedQty', 'damageQty', 'variance', 'confirmedOn', 'createdOn', 'referenceField10'];
  sub = new Subscription();
  ELEMENT_DATA: any[] = [];

  soTypeList1: any[];
  constructor(public dialog: MatDialog,
    private http: HttpClient,
    // private cas: CommonApiService,
    private fb: FormBuilder,
    public toastr: ToastrService,
    private router: Router,
    private spin: NgxSpinnerService,
    public cs: CommonService, 
    private service: PreinboundService,
    private reportService: ReportsService,
    private serviceLine: InboundConfirmationService,
    private auth: AuthService,
    private masterService: MasterService,) { 
      this.soTypeList1 = [
        {value: 'N', label: 'N'},
        {value: 'S', label: 'S'},
    ];
    }
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

  form = this.fb.group({
   warehouseId: [[this.auth.warehouseId],],
   endConfirmedOn: [],
   itemCode: [],
   itemCodeFE: [],
   manufacturerPartNo: [],
   refDocNumber: [],
   statusId: [],
   startConfirmedOn: [],
  });

  ngOnInit(): void {
    this.getDropdown();
  }
  multiselectItemCodeList: any[] = [];
  itemCodeList: any[] = [];

  onItemType(searchKey) {
    let searchVal = searchKey?.filter;
    if (searchVal !== '' && searchVal !== null) {
      forkJoin({
        itemList: this.reportService.getItemCodeDropDown2(searchVal.trim(),this.auth.companyId,this.auth.plantId,this.auth.warehouseId,this.auth.languageId).pipe(catchError(err => of(err))),
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


  multiOrderNo: any[] = [];
    getDropdown(){
      this.spin.show();
      this.sub.add(this.serviceLine.search({warehouseId : [this.auth.warehouseId]}).subscribe(res => {
        res.forEach((x: any) => this.multiOrderNo.push({ value: x.refDocNumber, label: x.refDocNumber }));
        this.spin.hide();
      }, err => {
        this.spin.hide();
        this.cs.commonerror(err);

      }))
    }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }





 
  itemCode: any;
  confirmedStorageBin: any;
  refDocNumber = '';
  warehouseId = '';

  filtersearch(){
    this.spin.show();
    this.form.controls.endConfirmedOn.patchValue(this.cs.day_callapiSearch(this.form.controls.endConfirmedOn.value));
    this.form.controls.startConfirmedOn.patchValue(this.cs.day_callapiSearch(this.form.controls.startConfirmedOn.value));
    if(this.form.controls.itemCodeFE.value != null){
      this.form.controls.itemCode.patchValue([this.form.controls.itemCodeFE.value]);
    }

    this.sub.add(this.serviceLine.searchLine(this.form.getRawValue()).subscribe(res => {
      res.forEach(x=> {
        x['variance'] = x.orderedQuantity - (x.damageQty + x.acceptedQty);
      })
      this.dataSource = new MatTableDataSource<any>(res);
      this.selection = new SelectionModel<any>(true, []);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.table = true;
      this.search = false;
      this.fullscreen = false;
      this.back = true;
      this.spin.hide();
      this.totalRecords = this.dataSource.data.length
    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
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
        "Order No ": x.refDocNumber,
        "Line No": x.lineNo,
        'Item Code': x.itemCode,
        'Description': x.description,
        'Acc Qty': x.acceptedQty,
        'Dam Qty': x.damageQty != null ? x.damageQty  : '0',
        'Variance': x.variance,
        'Created On': this.cs.dateExcel(x.createdOn),
        'Confirmed On': this.cs.dateExcel(x.confirmedOn),
        "Status ": x.referenceField10
      });

    })
    this.cs.exportAsExcel(res, "Preinbound");
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
  

  reset(){
   this.form.reset();
  }

  getDamQty(){
    let total = 0;
    this.dataSource.data.forEach(x =>{
      total = total + (x.damageQty != null ? x.damageQty : 0)
    })
    return Math.round(total *100 / 100)
  }

  getAccQty(){
    let total = 0;
    this.dataSource.data.forEach(x =>{
      total = total + (x.acceptedQty != null ? x.acceptedQty : 0)
    })
    return Math.round(total *100 / 100)
  }

  getVarianceQty(){
    let total = 0;
    this.dataSource.data.forEach(x =>{
      total = total + (x.variance != null ? x.variance : 0)
    })
    return Math.round(total *100 / 100)
  }
}
