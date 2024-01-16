import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription, forkJoin, of } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ItemReceiptService } from '../../item-receipt.service';
import { ReportsService } from 'src/app/main-module/reports/reports.service';
import { catchError } from 'rxjs/operators';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { Basicdata1Service } from 'src/app/main-module/Masters -1/masters/basic-data1/basicdata1.service';
import { BasicdataService } from 'src/app/main-module/Masters -1/masternew/basicdata/basicdata.service';
import { BarcodetypeidService } from 'src/app/main-module/other-setup/barcodetypeid/barcodetypeid.service';

@Component({
  selector: 'app-label-generate',
  templateUrl: './label-generate.component.html',
  styleUrls: ['./label-generate.component.scss']
})
export class LabelGenerateComponent implements OnInit {
  constructor(public dialogRef: MatDialogRef<any>, private service: ItemReceiptService,     @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService, private barcodeService: BarcodetypeidService,
    // private cas: CommonApiService,
    private spin: NgxSpinnerService,     private basicdataService: BasicdataService,
    private cs: CommonService, private reportService: ReportsService) { }




  ngOnInit(): void {

    this.findDimensions()
    if(this.data.partner_item_barcode == null || this.data.partner_item_barcode == ''){
      this.searchBarcode();
    }else{
      this.data['partner_item_barcode1'] = this.data.partner_item_barcode;
    }
    this.barcodeService.search({warehouseId : this.data.warehouseId}).subscribe(res => {
      this.data['barCodeType'] = res[0].barcodeType;
      console.log(this.data['barCodeType'])
    })
    
  }

  getVariance() {
    let num: number = (Number(this.data.acceptedQty ? this.data.acceptedQty : 0) + Number(this.data.damageQty ? this.data.damageQty : 0));
    this.data.varianceQty = Number(this.data.orderQty) as number - num;

    this.data['acceptedCbmQty'] = (Number(this.data.length ? this.data.length : 0) * Number(this.data.width ? this.data.width : 0) * Number(this.data.height ? this.data.height : 0)* Number(this.data.acceptedQty ? this.data.acceptedQty : 0))
    this.data['rejectedCbmQty'] = (Number(this.data.length ? this.data.length : 0) * Number(this.data.width ? this.data.width : 0) * Number(this.data.height ? this.data.height : 0)* Number(this.data.damageQty ? this.data.damageQty : 0))
  }

  sub = new Subscription();



  printed = false;

  submit() {
    if(this.data.interimStorageBin == null || this.data.interimStorageBin == ''){
      this.toastr.error(
        "Please fill storage bin field to continue",
        "Notification",{
          timeOut: 2000,
          progressBar: false,
        }
      );
  
      this.cs.notifyOther(true);
      return;
    }
  
    if (this.data.packBarcodes.length == 0) {
      this.spin.show();
      this.sub.add(this.service.packBarcodeV2(this.data.acceptedQty, this.data.damageQty, this.data.warehouseId, this.data.companyCode, this.data.plantId, this.data.languageId).subscribe(res => {

        this.data.packBarcodes = res;
        this.data['barcodeId'] = this.data.partner_item_barcode;
        this.data.partner_item_barcode = this.data.partner_item_barcode;
        this.data['goodReceiptQty'] = 1;
        this.data['packBarcodesState'] = true;
        this.spin.hide();
      this.dialogRef.close(this.data);
      }, err => {
        this.cs.commonerrorNew(err);
        this.spin.hide();
      }));
    }
  }
  
  submitNew(){
    this.spin.show();
    console.log(this.printed)
    if(this.printed == false){
      this.toastr.error(
        "Please print the barcode to continue",
        "Notification",{
          timeOut: 2000,
          progressBar: false,
        }
      );
      this.spin.hide();
      this.cs.notifyOther(true);
      return;
    }
    this.printed = false;
    this.dialogRef.close(this.data);
    this.spin.hide();
  }

  printBarcode(action){
    if (this.data.packBarcodes.length == 0) {
      this.spin.show();
      this.sub.add(this.service.packBarcodeV2(this.data.acceptedQty, this.data.damageQty, this.data.warehouseId, this.data.companyCode, this.data.plantId, this.data.languageId).subscribe(res => {
        this.data.packBarcodes = res;
        this.data['barcodeId'] = this.data.partner_item_barcode;
        this.data.partner_item_barcode = this.data.partner_item_barcode;
        this.data['goodReceiptQty'] = 1;
        this.data['noOfDamageLabel'] = this.data.noOfDamageLabel;
        this.data['noOfAcceptedLabel'] = this.data.noOfAcceptedLabel;
        this.data['totalLabel'] = this.data.noOfDamageLabel + this.data.noOfAcceptedLabel;
        this.data['barCodeType'] = this.data.barCodeType;
        this.data['packBarcodesState'] = true;
        let barcodeData: any[] = [];
        let dataa: any[] = [];
        let obj: any = {};
        for(let j=0; j < this.data.noOfAcceptedLabel; j++){
          obj.barcodeId = this.data.partner_item_barcode;
          obj.partner_item_barcode = this.data.partner_item_barcode;
          obj.goodReceiptQty = 1;
          obj.noOfDamageLabel = this.data.noOfDamageLabel;
          obj.noOfAcceptedLabel = this.data.noOfAcceptedLabel;
          obj.totalLabel = this.data.noOfDamageLabel + this.data.noOfAcceptedLabel;
          obj.barCodeType = this.data.barCodeType;
          obj.manufacturerName =  this.data.manufacturerName;
          obj.origin =  this.data.origin;
          obj.itemCode =  this.data.itemCode;
          obj.itemDescription =  this.data.itemDescription;
          obj.partner_item_barcode =  this.data.partner_item_barcode;

          obj.qty =  this.data.noOfAcceptedLabel;
          dataa.push(obj);
         } 
         let obj2: any = {};
        for(let i=0; i< this.data.noOfDamageLabel; i++){
          obj2.barcodeId = this.data.partner_item_barcode;
          obj2.partner_item_barcode = this.data.partner_item_barcode;
          obj2.goodReceiptQty = 1;
          obj2.noOfDamageLabel = this.data.noOfDamageLabel;
          obj2.noOfAcceptedLabel = this.data.noOfAcceptedLabel;
          obj2.totalLabel = this.data.noOfDamageLabel + this.data.noOfAcceptedLabel;
          obj2.barCodeType = this.data.barCodeType;
          obj2.manufacturerName =  this.data.manufacturerName;
          obj2.origin =  this.data.origin;
          obj2.itemCode =  this.data.itemCode;
          obj2.itemDescription =  this.data.itemDescription;
          obj2.partner_item_barcode =  this.data.partner_item_barcode;
          
          obj2.qty =  this.data.noOfDamageLabel;
          dataa.push(obj2);
         } 
        sessionStorage.setItem(
          'barcode',
          JSON.stringify({ BCfor: "barcode", list: dataa })
        );
        if(action == 'preview'){
          window.open('#/barcodeNew', '_blank');
        }
        if(action == 'print'){
          this.printed = true;
          window.open('#/barcode', '_blank');
        }
        
        this.spin.hide();
      }, err => {
        this.cs.commonerrorNew(err);
        this.spin.hide();
      }));
    }else{
      this.data.packBarcodes = [];
      this.spin.show();
      this.sub.add(this.service.packBarcodeV2(this.data.acceptedQty, this.data.damageQty, this.data.warehouseId, this.data.companyCode, this.data.plantId, this.data.languageId).subscribe(res => {
        this.data.packBarcodes = res;
        let dataa: any[] = [];
        let obj: any = {};
        for(let j=0; j < this.data.noOfAcceptedLabel; j++){
          obj.barcodeId = this.data.partner_item_barcode;
          obj.partner_item_barcode = this.data.partner_item_barcode;
          obj.goodReceiptQty = 1;
          obj.noOfDamageLabel = this.data.noOfDamageLabel;
          obj.noOfAcceptedLabel = this.data.noOfAcceptedLabel;
          obj.totalLabel = this.data.noOfDamageLabel + this.data.noOfAcceptedLabel;
          obj.barCodeType = this.data.barCodeType;
          obj.manufacturerName =  this.data.manufacturerName;
          obj.origin =  this.data.origin;
          obj.itemCode =  this.data.itemCode;
          obj.itemDescription =  this.data.itemDescription;
          obj.partner_item_barcode =  this.data.partner_item_barcode;

          obj.qty =  this.data.noOfAcceptedLabel;
          dataa.push(obj);
         } 
         let obj2: any = {};
        for(let i=0; i< this.data.noOfDamageLabel; i++){
          obj2.barcodeId = this.data.partner_item_barcode;
          obj2.partner_item_barcode = this.data.partner_item_barcode;
          obj2.goodReceiptQty = 1;
          obj2.noOfDamageLabel = this.data.noOfDamageLabel;
          obj2.noOfAcceptedLabel = this.data.noOfAcceptedLabel;
          obj2.totalLabel = this.data.noOfDamageLabel + this.data.noOfAcceptedLabel;
          obj2.barCodeType = this.data.barCodeType;
          obj2.manufacturerName =  this.data.manufacturerName;
          obj2.origin =  this.data.origin;
          obj2.itemCode =  this.data.itemCode;
          obj2.itemDescription =  this.data.itemDescription;
          obj2.partner_item_barcode =  this.data.partner_item_barcode;
          
          obj2.qty =  this.data.noOfDamageLabel;
          dataa.push(obj2);
         } 
     
        sessionStorage.setItem(
          'barcode',
          JSON.stringify({ BCfor: "barcode", list: dataa})
        );
        if(action == 'preview'){
          window.open('#/barcodeNew', '_blank');
        }
        if(action == 'print'){
          this.printed = true;
          window.open('#/barcode', '_blank');
        }
        
        this.spin.hide();
      }, err => {
        this.cs.commonerrorNew(err);
        this.spin.hide();
      }));
    }
  }

  searchBarcode(){
    this.spin.show();
    if(this.data.partner_item_barcode == null || this.data.partner_item_barcode == ""){
      let obj: any= {};
      obj.referenceField1 = [this.data.manufacturerName];
      obj.itemCode =  [this.data.itemCode];
      this.reportService.findBarCodeScan(obj).subscribe(res => {
        if(res.length == 1){
        this.data.partner_item_barcode = res[0].barcode;
        this.data['partner_item_barcode1'] = res[0].barcode;
        this.spin.hide();
        }else{
          this.data['partner_item_barcode1'] = this.data.itemCode;
          this.toastr.error(
            "No barcode found",
            "Notification",{
              timeOut: 2000,
              progressBar: false,
            }
          );
          this.spin.hide();
          this.cs.notifyOther(true);
          return;
        }
      },err => {
        this.cs.commonerrorNew(err);
        this.spin.hide();
      })
    }

  }


  showDropdown1: true;
  showDropdown(){
    this.data.interimStorageBin = null;
    this.showDropdown1 = true;
  }

  multiselectStorageList: any[] = [];
  storageBinList1: any[] = [];
  selectedStorageBin: any[] = [];
  onStorageType(searchKey) {
    let searchVal = searchKey?.filter;
    if (searchVal !== '' && searchVal !== null) {
      forkJoin({
        storageList: this.reportService.getStorageDropDown(searchVal.trim()).pipe(catchError(err => of(err))),
      })
        .subscribe(({ storageList }) => {
          if (storageList != null && storageList.length > 0) {
            this.multiselectStorageList = [];
            this.storageBinList1 = storageList;
            this.storageBinList1.forEach(x => this.multiselectStorageList.push({ value: x.storageBin, label: x.storageBin}))
          }
        });
    }
  }


    selectedIndex: number = 1;
    showSubmit = false;
 tabChanged(tabChangeEvent: MatTabChangeEvent): void {
    this.selectedIndex = tabChangeEvent.index;
} 
 nextStep() {


  
  if(this.selectedIndex == 1){
    if(this.data.acceptedQty == null || this.data.acceptedQty == '' || this.data.acceptedQty == 0){
      this.toastr.error(
        "Please fill accepted qty to continue",
        "Notification",{
          timeOut: 2000,
          progressBar: false,
        }
      );
      this.cs.notifyOther(true);
      return;
    } 
    else{
      this.data['noOfDamageLabel'] = this.data.damageQty ? this.data.damageQty: 0; 
      this.data['noOfAcceptedLabel'] = this.data.acceptedQty ? this.data.acceptedQty : 0;
      this.data['totalLabel'] = this.data.acceptedQty + this.data.damageQty;
      this.selectedIndex = this.selectedIndex + 1;
    }
  }
  else{
    this.selectedIndex = this.selectedIndex + 1;
  }
}


 previousStep() {
  this.selectedIndex = this.selectedIndex - 1;
}



findDimensions(){
  let obj: any = {};
  obj.companyCodeId = [this.data.companyCode];
  obj.languageId = [this.data.languageId];
  obj.plantId = [this.data.plantId];
  obj.warehouseId = [this.data.warehouseId];
  obj.itemCode = [this.data.itemCode];
  obj.manufacturerPartNo = [this.data.manufacturerName];
  
  this.basicdataService.search1(obj).subscribe(res => {
    if(res.length == 0){
      this.toastr.error(
        "No record found",
        "Notification",{
          timeOut: 2000,
          progressBar: false,
        }
      );
    }
    if(res.length == 1 && res[0].length == null && res[0].width == null && res[0].height == null){
      this.toastr.error(
        "Please maintain records for the corresponding part no in the basic data master to continue",
        "Notification",{
          timeOut: 2000,
          progressBar: false,
        }
      );
    }
    else{
      this.data['length'] = res[0].length;
      this.data['width'] = res[0].width;
      this.data['height'] = res[0].height;

      this.data['cbmQty']  = (Number(this.data.length ? this.data.length : 0) * Number(this.data.width ? this.data.width : 0) * Number(this.data.height ? this.data.height : 0));
      this.data['acceptedCbmQty'] = (Number(this.data.length ? this.data.length : 0) * Number(this.data.width ? this.data.width : 0) * Number(this.data.height ? this.data.height : 0)* Number(this.data.acceptedQty ? this.data.acceptedQty : 0));
      this.data['rejectedCbmQty'] = (Number(this.data.length ? this.data.length : 0) * Number(this.data.width ? this.data.width : 0) * Number(this.data.height ? this.data.height : 0)* Number(this.data.damageQty ? this.data.damageQty : 0));
    }
  })

}
}
