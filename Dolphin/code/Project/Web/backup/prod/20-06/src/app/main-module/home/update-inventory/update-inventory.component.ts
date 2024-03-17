import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription, forkJoin, of } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { InhouseTransferService } from '../../make&change/inhouse-transfer/inhouse-transfer.service';
import { MasterService } from 'src/app/shared/master.service';
import { catchError } from 'rxjs/operators';

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
  inventoryUom: any;
  allocatedQuantity: number;
  inventoryQuantity: number;
  description: any;
  mfrPartNo: any;
  storageSection: any;
  showHiddenFields: boolean;



  constructor(private inventory: InhouseTransferService, private spin: NgxSpinnerService, public cs: CommonService,   public auth: AuthService,
    public toastr: ToastrService, private masterService: MasterService,
    public dialogRef: MatDialogRef<UpdateInventoryComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    ) { }
  
  sub = new Subscription();

  ngOnInit(): void {
    this.showHiddenFields = false;
    this.dropdownfill();
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
      if(res[0].referenceField10){this.storageSection = String(res[0].referenceField10);}
console.log(this.multiSelectStorageSectionID);
      console.log(this.storageSection);
      if(res[0].referenceField8){this.description = res[0].referenceField8;} 
      if(res[0].packBarcodes){this.packBarcodes = res[0].packBarcodes;} 
      if(res[0].specialStockIndicatorId){this.specialStockIndicatorId = res[0].specialStockIndicatorId;} 
      if(res[0].stockTypeId){this.stockTypeId = res[0].stockTypeId;} 
      if(res[0].storageBin){this.storageBin = res[0].storageBin;} 
      if(res[0].inventoryUom){this.inventoryUom = res[0].inventoryUom;} 
      if(res[0].inventoryQuantity){this.inventoryQuantity = res[0].inventoryQuantity;} 

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
    this.cs.commonerror(err);
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

multiSelectStorageSectionID1: any[] = [];
multiSelectStorageSectionID: any[] = [];
dropdownfill() {
  this.spin.show();
  forkJoin({
    storageSection: this.masterService.getStorageSectionMasterDetails().pipe(catchError(err => of(err)))
  })
    .subscribe(({storageSection }) => {
      this.multiSelectStorageSectionID1 = storageSection.filter((x: any) => x.warehouseId == this.auth.warehouseId);
      this.multiSelectStorageSectionID1.forEach(x => this.multiSelectStorageSectionID.push({ value: x.storageSectionId, label: x.storageSectionId}));
      this.multiSelectStorageSectionID = this.cs.removeDuplicatesFromArrayNewstatus(this.multiSelectStorageSectionID)

    }, (err) => {
      this.toastr.error(
        err,
        ""
      );
    });
  this.spin.hide();

}

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
    obj.referenceField10 = String;
    if (this.allocatedQuantity != null) {
      obj.allocatedQuantity = Number(this.allocatedQuantity);
    }
    if (this.inventoryQuantity != null) {
      obj.inventoryQuantity = Number(this.inventoryQuantity);
    }
    if (this.description != null) {
      obj.referenceField8 = (this.description);
    }
    if (this.mfrPartNo != null) {
      obj.referenceField9 = (this.mfrPartNo);
    }
    if (this.storageSection != null) {
      obj.referenceField10 = (this.storageSection);
    }
    this.sub.add(this.inventory.inventoryUpdate(this.stockTypeId, this.itemCode, this.packBarcodes, this.specialStockIndicatorId,this.storageBin, this.auth.warehouseId, obj).subscribe(res => {
      this.toastr.success("Inventory updated successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      if (this.inventoryQuantity != null && this.inventoryQuantity != 0) {
      let obj1: any = {};
        obj1.balanceOHQty = this.inventoryQuantity != null ? Number(this.inventoryQuantity) : 0;
        obj1.batchSerialNumber = 1;
        obj1.companyCodeId = this.auth.companyId;
        obj1.description = this.description != null ? this.description : '';
        obj1.inventoryUom = this.inventoryUom != null ? this.inventoryUom : '';;
        obj1.itemCode = this.itemCode != null ? this.itemCode : '';
        obj1.languageId = this.auth.languageId;
        obj1.movementDocumentNo = 1;
        obj1.movementQty = this.inventoryQuantity != null ? this.inventoryQuantity : 0;
        obj1.movementType = 4;
        obj1.packBarcodes = this.packBarcodes != null ? this.packBarcodes : '';
        obj1.plantId = this.auth.plantId;
        obj1.refDocNumber = 1;
        obj1.specialStockIndicator = this.specialStockIndicatorId != null ? this.specialStockIndicatorId : '';
        obj1.stockTypeId = this.stockTypeId != null ? this.stockTypeId : 0;
        obj1.storageBin = this.storageBin != null ? this.storageBin : '';
        obj1.submovementType = 1;
        obj1.warehouseId = this.auth.warehouseId;

      this.sub.add(this.inventory.createInventoryMovement(obj1).subscribe(res => {

      }));
      }
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
      this.cs.commonerror(err);
      this.spin.hide();
    }));

  }

}
