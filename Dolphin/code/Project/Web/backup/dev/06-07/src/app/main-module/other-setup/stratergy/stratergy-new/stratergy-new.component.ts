import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { StratergyService } from '../stratergy.service';

@Component({
  selector: 'app-stratergy-new',
  templateUrl: './stratergy-new.component.html',
  styleUrls: ['./stratergy-new.component.scss']
})
export class StratergyNewComponent implements OnInit {

 
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
  form = this.fb.group({
    companyCodeId: [],
    plantId: [],
    warehouseId: [],
    strategyTypeId: [],
    strategyNo: [],
    languageId: [],
    strategyTypeText: [],
    strategyText: [],
    deletionIndicator: [],
    createdBy: [],
    createdOn: [],
    updatedBy: [],
    updatedOn: [],
  });
  panelOpenState = false;
  constructor(
    //public dialogRef: MatDialogRef<any>,
    public dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,
    private service: StratergyService,
  ) { }
  ngOnInit(): void {
    if (this.data.pageflow != 'New') {
      this.form.controls.statusId.disable();
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
  }
  }
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code, this.auth.warehouseId).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
    // this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
    // this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.spin.hide();
    },
     err => {
    this.cs.commonerror(err);
      this.spin.hide();
    }
    ));
  }

  sub = new Subscription();
  submitted = false;

 submit(){
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

if (this.data.code) {
  this.sub.add(this.service.Update(this.form.getRawValue(),this.data.code).subscribe(res => {
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
}else{
  this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
    this.toastr.success(res.code + " Saved Successfully!","Notification",{
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

 }

}


