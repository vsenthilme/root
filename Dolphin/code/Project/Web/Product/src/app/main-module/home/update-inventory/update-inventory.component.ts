import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { InhouseTransferService } from '../../make&change/inhouse-transfer/inhouse-transfer.service';

@Component({
  selector: 'app-update-inventory',
  templateUrl: './update-inventory.component.html',
  styleUrls: ['./update-inventory.component.scss']
})
export class UpdateInventoryComponent implements OnInit {
  itemCode: any;
  packBarcodes: any;
  specialStockIndicatorId: any;
  stockTypeId: any;
  storageBin: any;
  allocatedQuantity: number;
  inventoryQuantity: number;
  description: any;
  mfrPartNo: any;
  showHiddenFields: boolean;



  constructor(private inventory: InhouseTransferService, private spin: NgxSpinnerService, public cs: CommonService,   public auth: AuthService,
    public toastr: ToastrService,
    public dialogRef: MatDialogRef<UpdateInventoryComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    ) { }
  
  sub = new Subscription();

  ngOnInit(): void {
    this.showHiddenFields = false;
  }
  multiPackBarcodes: any[] = [];
  multispecialStockIndicatorId: any[] = [];
  multistockTypeId: any[] = [];
  multistorageBin: any[] = [];

 
findItemCode(){
  this.spin.show();
  this.multiPackBarcodes = [];
  this.multispecialStockIndicatorId = [];
  this.multistockTypeId = [];
  this.multistorageBin = [];

  this.sub.add(this.inventory.GetInventory({itemCode: [this.itemCode]}).subscribe(res => {

    if(res.length == 0){
      this.toastr.error(
        "No item code found",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      )
      return;
    }

    if(res.length > 0){
    this.showHiddenFields = true;
    }

      res.forEach((element: {packBarcodes: string, specialStockIndicatorId: string, stockTypeId:string, storageBin:string}) => {
      if(res[0].referenceField9){this.mfrPartNo = res[0].referenceField9;}
      if(res[0].referenceField8){this.description = res[0].referenceField8;} 
      if(res[0].packBarcodes){this.packBarcodes = res[0].packBarcodes;} 
      if(res[0].specialStockIndicatorId){this.specialStockIndicatorId = res[0].specialStockIndicatorId;} 
      if(res[0].stockTypeId){this.stockTypeId = res[0].stockTypeId;} 
      if(res[0].storageBin){this.storageBin = res[0].storageBin;} 

        this.multiPackBarcodes.push({value: element.packBarcodes, label: element.packBarcodes});
        this.multiPackBarcodes = this.cs.removeDuplicatesFromArrayNewstatus(this.multiPackBarcodes);

        this.multispecialStockIndicatorId.push({value: element.specialStockIndicatorId, label: element.specialStockIndicatorId});
        this.multispecialStockIndicatorId = this.cs.removeDuplicatesFromArrayNewstatus(this.multispecialStockIndicatorId);

        this.multistockTypeId.push({value: element.stockTypeId, label: element.stockTypeId});
        this.multistockTypeId = this.cs.removeDuplicatesFromArrayNewstatus(this.multistockTypeId);

        this.multistorageBin.push({value: element.storageBin, label: element.storageBin});
        this.multistorageBin = this.cs.removeDuplicatesFromArrayNewstatus(this.multistorageBin);
      });
      this.spin.hide();
  }, err => {
    this.cs.commonerrorNew(err);
    this.spin.hide();
  }));
}


//validateSearch(){
  // if(!this.description || !this.mfrPartNo){
  //   this.toastr.error(
  //     "Please search the item code to continue",
  //     "Notification", {
  //     timeOut: 2000,
  //     progressBar: false,
  //   }
  //   )
  //   return;
  // }
//}
  submit() {

      if(!this.itemCode){
    this.toastr.error(
      "Please enter item code to continue",
      "Notification", {
      timeOut: 2000,
      progressBar: false,
    }
    )
    return;
  }

    let obj: any = {};

    obj.allocatedQuantity = Number;
    obj.inventoryQuantity = Number;
    obj.referenceField8 = String;
    obj.referenceField9 = String;
    if (this.allocatedQuantity != null) {
      obj.allocatedQuantity = Number(this.allocatedQuantity);
    }
    if (this.inventoryQuantity != null) {
      obj.inventoryQuantity = Number(this.inventoryQuantity);
    }
    obj.referenceField8 = this.description;
    obj.referenceField9 = this.mfrPartNo;

    this.sub.add(this.inventory.inventoryUpdate(this.stockTypeId, this.itemCode, this.packBarcodes, this.specialStockIndicatorId,this.storageBin, this.auth.warehouseId, obj).subscribe(res => {
      this.toastr.success("Inventory updated successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });

      this.itemCode = null;
      this.description = null;
      this.mfrPartNo = null;
      this.packBarcodes = null;
      this.specialStockIndicatorId = null;
      this.stockTypeId = null;
      this.storageBin = null;
      this.allocatedQuantity = 0;
      this.inventoryQuantity = 0;
     // this.dialogRef.close();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));

  }

}
