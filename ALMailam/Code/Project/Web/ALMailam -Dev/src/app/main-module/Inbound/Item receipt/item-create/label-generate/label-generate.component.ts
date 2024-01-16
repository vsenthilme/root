import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription, forkJoin, of } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ItemReceiptService } from '../../item-receipt.service';
import { ReportsService } from 'src/app/main-module/reports/reports.service';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-label-generate',
  templateUrl: './label-generate.component.html',
  styleUrls: ['./label-generate.component.scss']
})
export class LabelGenerateComponent implements OnInit {
  constructor(public dialogRef: MatDialogRef<any>, private service: ItemReceiptService,     @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    // private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private cs: CommonService, private reportService: ReportsService) { }


    acceptedQty: any;
    damageQty: any;
    varianceQty: any;

  ngOnInit(): void {
    if(this.data.partner_item_barcode == null || this.data.partner_item_barcode == ''){
      this.searchBarcode();
    }
  }

  getVariance() {
    let num: number = (Number(this.data.acceptedQty ? this.data.acceptedQty : 0) + Number(this.data.damageQty ? this.data.damageQty : 0));
    this.data.varianceQty = Number(this.data.orderQty) as number - num;
  }

  sub = new Subscription();


  submit() {

    if(this.data.partner_item_barcode == null || this.data.partner_item_barcode == ''){
      this.toastr.error(
        "Please fill barcode field to continue",
        "Notification",{
          timeOut: 2000,
          progressBar: false,
        }
      );
  
      this.cs.notifyOther(true);
      return;
    }

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

    if(this.data.acceptedQty == null || this.data.acceptedQty == '' || this.acceptedQty == 0){
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
    if(this.data.damageQty == null || this.data.damageQty == ''){
      this.data.damageQty = 0;
    }
    if (this.data.packBarcodes.length == 0) {
      this.spin.show();
      this.sub.add(this.service.packBarcodeV2(this.data.acceptedQty, this.data.damageQty, this.data.warehouseId, this.data.companyCode, this.data.plantId, this.data.languageId).subscribe(res => {

        // res.forEach(x => {
        //   x.barcode = this.data.partner_item_barcode;
        // })

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


  searchBarcode(){
    // this.spin.show();
    // this.reportService.barCodeScan().subscribe(res => {
    //   let filter = res.filter(x => (x.referenceField1 == this.data.manufacturerName) && (x.itemCode == this.data.itemCode));
    //   if(filter.length == 1){
    //     console.log(filter);
    //   this.data.partner_item_barcode = filter[0].barcode;
    //   this.spin.hide();
    //   }else{
    //     this.toastr.error(
    //       "No barcode found",
    //       "Notification",{
    //         timeOut: 2000,
    //         progressBar: false,
    //       }
    //     );
    //     this.spin.hide();
    //     this.cs.notifyOther(true);
    //     return;
    //   }
    // },err => {
    //   this.cs.commonerrorNew(err);
    //   this.spin.hide();
    // })

    this.spin.show();
    if(this.data.partner_item_barcode == null || this.data.partner_item_barcode == ""){
      let obj: any= {};
      obj.referenceField1 = [this.data.manufacturerName];
      obj.itemCode =  [this.data.itemCode];
      this.reportService.findBarCodeScan(obj).subscribe(res => {
        if(res.length == 1){
        this.data.partner_item_barcode = res[0].barcode;
        this.spin.hide();
        }else{
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
            console.log(3)
            this.multiselectStorageList = [];
            this.storageBinList1 = storageList;
            this.storageBinList1.forEach(x => this.multiselectStorageList.push({ value: x.storageBin, label: x.storageBin}))
          }
        });
    }
  }


}
