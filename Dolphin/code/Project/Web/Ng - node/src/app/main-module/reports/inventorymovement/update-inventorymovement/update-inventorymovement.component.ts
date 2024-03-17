import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, Validators, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DialogExampleComponent } from 'src/app/common-field/innerheader/dialog-example/dialog-example.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { HandlingUnitElement, HandlingUnitService } from 'src/app/main-module/Masters -1/other-masters/handling-unit/handling-unit.service';
import { ReportsService } from '../../reports.service';

@Component({
  selector: 'app-update-inventorymovement',
  templateUrl: './update-inventorymovement.component.html',
  styleUrls: ['./update-inventorymovement.component.scss']
})
export class UpdateInventorymovementComponent implements OnInit {

  screenid: 1033 | undefined;
  
  sub = new Subscription();
  //creation of Form
   email = new FormControl('', [Validators.required, Validators.email, Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")]);
  form = this.fb.group({
    balanceOHQty: [],
    batchSerial: [],
    caseCode: [],
    createdOn: [],
    deletionIndicator: [],
    description: [],
    inventoryUom: [],
    itemCode: [],
    movementDocumentNo: [],
    movementQty: [],
    movementQtyValue: [],
    movementType: [],
    packBarcodes: [],
    palletCode: [],
    refDocNumber: [],
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
    specialStockIndicator: [],
    stockTypeId: [],
    storageBin: [],
    storageMethod: [],
    submovementType: [],
    variantCode: [],
    variantSubCode: [],
    languageId: ['EN'],
    warehouseId: [this.auth.warehouseId],
    plantId: ['1001'],
    statusId: ["1"],
    companyCodeId: ['1000'],
    createdBy: [this.auth.username],
    updatedBy: [this.auth.username],
    updatedOn: [],
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
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  // statusList: any[] = [
  //   { key: "Active", value: 'Active' },
  //   { key: "InActive", value: 'InActive' }];
    
  formgr: HandlingUnitElement | undefined;

  panelOpenState = false;
  constructor(
    public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    private fb: FormBuilder,
    private cs: CommonService,
    private service: ReportsService,
  ) { }
  ngOnInit(): void {
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    if (this.data.pageflow != 'New') {
      this.form.disable();
      this.form.controls.movementQty.enable();
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
    }
  }



  fill() {
    this.spin.show();
    this.sub.add(this.service.getInventoryMovement1(this.data.code).subscribe(res => {9
      this.form.patchValue(res, { emitEvent: false });
      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.spin.hide();
    },
    err => {
    this.cs.commonerror(err);
      this.spin.hide();
    }
    ));
  }

  submit() {
    this.submitted = true;
    if (this.form.invalid) {
      this.toastr.error(
        "Please fill required fields to continue",
        "Notification",{
          timeOut: 2000,
          progressBar: false,
        }
      );

      this.cs.notifyOther(true);
      return;
    }

    this.cs.notifyOther(false);
    this.spin.show();
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    this.form.patchValue({ updatedby: this.auth.username });
    if (this.data.code) {
      this.sub.add(this.service.updateInventoryMovement(this.data.code, this.form.getRawValue()).subscribe(res => {
        this.toastr.success(this.data.code + " updated successfully!","Notification",{
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.dialogRef.close();

      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
    // else {
    //   this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
    //     this.toastr.success(res.handlingUnit + " Saved Successfully!","Notification",{
    //       timeOut: 2000,
    //       progressBar: false,
    //     });
    //     this.spin.hide();
    //     this.dialogRef.close();

    //   }, err => {
    //     this.cs.commonerror(err);
    //     this.spin.hide();

    //   }));
    // }
  };

  onNoClick(): void {
    this.dialogRef.close();
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
}




