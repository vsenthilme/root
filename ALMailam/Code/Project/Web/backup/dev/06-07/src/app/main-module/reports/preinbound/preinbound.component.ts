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
import { Table } from 'primeng/table';

@Component({
  selector: 'app-preinbound',
  templateUrl: './preinbound.component.html',
  styleUrls: ['./preinbound.component.scss']
})
export class PreinboundComponent implements OnInit {
  screenid=3185;
  inbound: any[] = [];
  selectedinbound : any[] = [];
  @ViewChild('inboundTag') inboundTag: Table | any;
 
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
      displayedColumns: string[] = ['refDocNumber', 'lineNo', 'itemCode', 'description', 'orderQty', 'acceptedQty', 'damageQty', 'varianceQty', 'confirmedOn', 'createdOn', 'referenceField10'];
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

  





 
  itemCode: any;
  confirmedStorageBin: any;
  refDocNumber = '';
  warehouseId = '';

  filtersearch(){
this.spin.show();
this.form.controls.endConfirmedOn.patchValue(this.cs.day_callapiSearch(this.form.controls.endConfirmedOn.value));
this.form.controls.startConfirmedOn.patchValue(this.cs.day_callapiSearch(this.form.controls.startConfirmedOn.value));
this.form.controls.itemCode.patchValue([this.form.controls.itemCodeFE.value]);

    this.sub.add(this.serviceLine.searchLine(this.form.getRawValue()).subscribe(res => {
      console.log(res)
      this.inbound = res;
   
     
      this.table = true;
      this.search = false;
      this.fullscreen = false;
      this.back = true;
      this.spin.hide();
     
    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
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
    this.inbound.forEach(x => {
      res.push({
        "Order No ": x.refDocNumber,
        "Line No": x.lineNo,
        'Item Code': x.itemCode,
        'Description': x.description,
        'Acc Qty': x.acceptedQty,
        'Dam Qty': x.damageQty,
        'Variance': x.varianceQty,
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

 




  
  

  reset(){
   this.form.reset();
  }
}
