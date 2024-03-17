import { Component, Inject, OnInit } from '@angular/core';
import { ReportsService } from '../../reports/reports.service';
import { forkJoin, of } from 'rxjs';
import { AuthService } from 'src/app/core/core';
import { catchError } from 'rxjs/operators';
import { FormBuilder, Validators } from '@angular/forms';
import { CommonService } from 'src/app/common-service/common-service.service';
import { Basicdata1Service } from '../../Masters -1/masters/basic-data1/basicdata1.service';
import { PutawayService } from '../../Inbound/putaway/putaway.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-create-inventory',
  templateUrl: './create-inventory.component.html',
  styleUrls: ['./create-inventory.component.scss']
})
export class CreateInventoryComponent implements OnInit {

  constructor(private ReportsService: ReportsService, private auth : AuthService, private fb: FormBuilder, private cs: CommonService,
    private Basicdata1Service: Basicdata1Service, private toastr: ToastrService, private spin: NgxSpinnerService, private PutawayService : PutawayService,
    public dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }


  form = this.fb.group({
    allocatedQuantity: [],
    batchSerial: [],
    binClassId: [],
    caseCode: [],
    companyCodeId: [this.auth.companyId, ],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    description: [],
    expiryDate: [],
    inventoryQuantity: [],
    manufacturerPartNo: [],
    inventoryUom: [],
    itemCode: [],
    languageId: [this.auth.languageId,],
    manufacturerDate: [],
    packBarcodes: [, Validators.required],
    palletCode: [],
    plantId: [this.auth.plantId,],
    referenceField1: [],
    referenceField10: [],
    referenceField2: [],
    referenceField3: [],
    referenceField4: [],
    referenceField5: [],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    referenceOrderNo: [],
    specialStockIndicatorId: [1,],
    stockTypeId: [1,],
    storageBin: [],
    storageMethod: [],
    variantCode: [],
    variantSubCode: [],
    warehouseId: [this.auth.warehouseId, ],
  });

  ngOnInit(): void {
  }

  multiselectItemCodeList: any[] = [];
  itemCodeList: any[] = [];
  multiselectItemCodeList1: any[] = [];
itemCodeList1: any[] = [];
  onItemType(searchKey) {
    let searchVal = searchKey?.filter;
    if (searchVal !== '' && searchVal !== null) {
      forkJoin({
        itemList: this.ReportsService.getItemCodeDropDown2(searchVal.trim(),this.auth.companyId,this.auth.plantId,this.auth.warehouseId,this.auth.languageId).pipe(catchError(err => of(err))),
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


  multiselectStorageList: any[] = [];
  storageBinList1: any[] = [];
  selectedStorageBin: any[] = [];
  onStorageType(searchKey) {
    let searchVal = searchKey?.filter;
    if (searchVal !== '' && searchVal !== null) {
      forkJoin({
        storageList: this.ReportsService.getStorageDropDown2(searchVal.trim(),this.auth.companyId,this.auth.plantId,this.auth.warehouseId,this.auth.languageId).pipe(catchError(err => of(err))),
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

  showHiddenFields = false;
  showHiddenFields1 = false;
  itemCodeChanged(e){
    this.spin.show();
    this.Basicdata1Service.search({itemCode: [e.value], warehouseId: [this.auth.warehouseId]}).subscribe(res => {
      this.form.controls.referenceField9.patchValue(res[0].manufacturerPartNo);
      this.form.controls.referenceField8.patchValue(res[0].description);
      this.showHiddenFields = true;
     this.spin.hide();
    }, err => {
      this.spin.hide();
      this.cs.commonerror(err)
    });
  }

  multiStorageSectionId: any[] = [];
  storageBinchange(e){
    this.spin.show();
    this.PutawayService.findStorageBinNew({storageBin: [e.value], warehouseId: [this.auth.warehouseId]}).subscribe(res => {
     res.forEach( x => {
      this.multiStorageSectionId.push({
        value: x.storageSectionId,
        label: x.storageSectionId
      });
      
     this.multiStorageSectionId = this.cs.removeDuplicatesFromArrayNewstatus(this.multiStorageSectionId);
     if(this.multiStorageSectionId.length == 1){
      this.form.controls.referenceField10.patchValue(x.storageSectionId);
     }
     })
     this.showHiddenFields1 = true;
     this.spin.hide();
    }, err => {
      this.spin.hide();
      this.cs.commonerror(err)
    });
  }

  submit(){
    if ((this.form.invalid)) {
      this.spin.show();
      this.toastr.error("Please fill the required fields to continue", "", {
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
      return;
    }

    this.spin.show();
    this.ReportsService.createInventory(this.form.getRawValue()).subscribe(res => {
      this.toastr.success("Inventory Saved Successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
      this.dialogRef.close();
      }, err => {
        this.spin.hide();
        this.cs.commonerror(err)
      });
  }
}
