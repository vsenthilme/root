




  import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
//import { table } from 'console';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { forkJoin, of, Subscription } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { DialogExampleComponent } from 'src/app/common-field/innerheader/dialog-example/dialog-example.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { PreinboundService } from 'src/app/main-module/Inbound/preinbound/preinbound.service';
import { BOMElement, BOMService } from 'src/app/main-module/Masters -1/other-masters/bom/bom.service';
import { BusinessPartnerService } from 'src/app/main-module/Masters -1/other-masters/business-partner/business-partner.service';
import { ReportsService } from 'src/app/main-module/reports/reports.service';
import { MasterService } from 'src/app/shared/master.service';
import { PreoutboundService } from '../preoutbound.service';

export interface PeriodicElement {

  position: number;
}


interface SelectItem {
  item_id: number;
  item_text: string;
}

// const ELEMENT_DATA: PeriodicElement[] = [
//   //{position: 1,},
// ];
@Component({
  selector: 'app-preoutbound-new',
  templateUrl: './preoutbound-new.component.html',
  styleUrls: ['./preoutbound-new.component.scss']
})
export class PreoutboundNewComponent implements OnInit {

  email = new FormControl('', [Validators.required, Validators.email, Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")]);
  displayedColumns: string[] = ['no','lineReference', 'orderType','orderedQty','sku','skuDescription','uom', 'delete'];
  dataSource = new MatTableDataSource<any>([]);
  screenid: 1037 | undefined;

  sub = new Subscription();

  //creation of Form
  SoLine = this.fb.group({
        lineReference: [],
        orderType: [],
        orderedQty: [],
        sku: [],
        skuDescription: [],
        uom: [],
  });
  rows: FormArray = this.fb.array([this.SoLine]);

  SoHeader = this.fb.group({
    storeID: [],
    storeName: [],
    transferOrderNumber: [],
    wareHouseId: [],
    requiredDeliveryDate : [],
  });

  form = this.fb.group({
    soHeader: this.SoHeader,
    soLine: this.rows,
  });
  submitted = false;
  disabled = false;
  step = 0;
  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }
  isShowDiv = false;
  public icon = 'expand_more';
  showFloatingButtons: any;
  toggle = true;
  storecodeList: any;
  toggleFloat() {
    this.isShowDiv = !this.isShowDiv;
    this.toggle = !this.toggle;

    if (this.icon === 'expand_more') {
      this.icon = 'chevron_left';
    } else {
      this.icon = 'expand_more'
    }
    this.showFloatingButtons = !this.showFloatingButtons;
    console.log('show:' + this.showFloatingButtons);
  }

  // statusList: any[] = [
  //   { key: "Active", value: 'Active' },
  //   { key: "InActive", value: 'InActive' }];



  panelOpenState = false;
  constructor(
    // public dialogRef: MatDialogRef<DialogExampleComponent>,
    // @Inject(MAT_DIALOG_DATA) public data: any, private dialog: MatDialog,
    private service: PreoutboundService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private bonService: BusinessPartnerService,
    private masterService: MasterService,
    private auth: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private cs: CommonService,
    private ReportsService: ReportsService
  ) { }
  ngOnInit(): void {
 this.dropdownfill();
    
  }
  multiWarehouseList: any[] = [];
  multiPartnerList: any[] = [];
  dropdownfill() {
    this.spin.show();
    forkJoin({
      warehouse: this.masterService.getWarehouseMasterDetails().pipe(catchError(err => of(err))),
      partner: this.bonService.search({warehouseId: [this.auth.warehouseId], businessPartnerType: [2]}).pipe(catchError(err => of(err))),
    })
    .subscribe(({ warehouse, partner }) => {
      warehouse.forEach(x => this.multiWarehouseList.push({ value: x.warehouseId, label: x.warehouseId }));
      partner.forEach(x => this.multiPartnerList.push({ value: x.partnerCode, label: x.partnerCode + ' - ' + x.partnerName }));
      this.form.get('soHeader.wareHouseId')?.patchValue(this.auth.warehouseId)
    })
    this.spin.hide();
  }








  submit() {
this.submitted = true;
    
    this.form.get('soHeader.requiredDeliveryDate')?.patchValue(this.cs.dateddMMYY(this.form.get('soHeader.requiredDeliveryDate')?.value));
    this.sub.add(this.service.createShipmentOrder(this.form.getRawValue()).subscribe(res => {
  if(res){
    this.toastr.success("Order created successfully!",   "Notification", {
      timeOut: 2000,
      progressBar: false,
    });
    
    this.router.navigate(["/main/outbound/preoutbound"]);
    this.spin.hide();
   // this.dialogRef.close();
  }

    }, err => {

      this.cs.commonerrorNew(err);
      this.spin.hide();

    }));
  };

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

  elementdata: any;
  @ViewChild(MatTable)
  table!: MatTable<PeriodicElement>;
  add() {

    if (!this.form.get('soHeader.wareHouseId')?.value || !this.form.get('soHeader.storeID')?.value || !this.form.get('soHeader.transferOrderNumber')?.value || !this.form.get('soHeader.requiredDeliveryDate')?.value) {
      this.toastr.error("Please fill the header details to continue", "Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }

    if (this.dataSource.data.length > 0) {
      this.addRow();
    }
    this.dataSource.data.push(this.dataSource.data.length);

    // this.elementdata.push(this.elementdata[0]);

    this.dataSource._updateChangeSubscription();


  }

  addRow() {
    const row = this.fb.group({
      lineReference: [],
      orderType: [],
      orderedQty: [],
      sku: [],
      skuDescription: [],
      uom: [],
    });
    this.rows.push(row);
    // if (!noUpdate) { this.updateView(); }
  }

  addRowOnEdit(bom: any) {
    const row = this.fb.group({
      lineReference: [],
      orderType: [],
      orderedQty: [],
      sku: [],
      skuDescription: [],
      uom: [],
    });
    this.rows.push(row);
    console.log(this.rows);
    // if (!noUpdate) { this.updateView(); }
  }

  removeRow(index: any) {
    this.rows.removeAt(index);
    const rowNo = this.dataSource.data.indexOf(index);
    console.log(rowNo);
    if(rowNo == -1)
    {
      this.dataSource.data.splice(index, 1);
    }
    else
    {
      this.dataSource.data.splice(rowNo, 1);
    }
    
    this.dataSource._updateChangeSubscription();
  }


  onItemSelect(item: any) {
    console.log(item);
  }

  onSelectAll(items: any) {
    console.log(items);
  }


  multiselectItemCodeList: any[] = [];
  itemCodeList: any[] = [];
  multiselectItemCodeList1: any[] = [];
itemCodeList1: any[] = [];
  onItemType(searchKey) {
    let searchVal = searchKey?.filter;
    if (searchVal !== '' && searchVal !== null) {
      forkJoin({
        itemList: this.ReportsService.getItemCodeDropDown(searchVal.trim()).pipe(catchError(err => of(err))),
      })
        .subscribe(({ itemList }) => {
          if (itemList != null && itemList.length > 0) {
            this.multiselectItemCodeList = [];
            this.itemCodeList = itemList;
            this.itemCodeList.forEach(x => this.multiselectItemCodeList.push({value: x.itemCode, label: x.itemCode, description: x.description}))
          }
        });
    }
  }

  selectedProduct(e, index){
    this.multiselectItemCodeList.forEach(x => {
      if(x.value == e.value){
        const skuDescription = this.rows.at(index) as FormGroup
        skuDescription.get('skuDescription')?.setValue(x.description); //handle according to your code
      }
    })
  }

}




