import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { WarehouseService } from '../../warehouse/warehouse.service';
import { StatusidService } from '../statusid.service';
import { CommonApiService } from 'src/app/common-service/common-api.service';

@Component({
  selector: 'app-statusid-new',
  templateUrl: './statusid-new.component.html',
  styleUrls: ['./statusid-new.component.scss']
})
export class StatusidNewComponent implements OnInit {

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
    createdBy: [],
  createdOn: [],
  createdOnFE: [],
  deletionIndicator: [],
  languageId: [,Validators.required],
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
  status: [,Validators.required],
  statusId: [,Validators.required],
  updatedBy: [],
  updatedOn: [],
  updatedOnFE: [],
  });
  panelOpenState = false;
  constructor(
    //public dialogRef: MatDialogRef<any>,
    public dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    public auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,
    private cas: CommonApiService,
    private service: StatusidService,
  ) { }
  ngOnInit(): void {
     this.form.controls.createdBy.disable();
     this.form.controls.createdOnFE.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOnFE.disable();
    this.form.controls.languageId.patchValue(this.auth.languageId);
    this.dropdownlist();
    if (this.data.pageflow != 'New') {
      this.form.controls.statusId.disable();
      this.form.controls.languageId.disable();
      if (this.data.pageflow == 'Display')
    this.form.disable();
     this.fill();
      
  }
  }
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code, this.data.languageId).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
      this.form.controls.createdOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.createdOn.value));
      this.form.controls.updatedOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.updatedOn.value));
      this.spin.hide();
    },
     err => {
    this.cs.commonerrorNew(err);
      this.spin.hide();
    }
    ));
  }

  sub = new Subscription();
  submitted = false;
  languageidList:any[]=[];
   dropdownlist(){
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.languageid.url,
     
    ]).subscribe((results) => {
    this.languageidList = this.cas.foreachlist2(results[0], this.cas.dropdownlist.setup.languageid.key);
   
    
    this.spin.hide();
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }

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
  this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code, this.data.languageId).subscribe(res => {
    this.toastr.success(this.data.code + " updated successfully!","Notification",{
      timeOut: 2000,
      progressBar: false,
    });
    this.spin.hide();
    this.dialogRef.close();

  }, err => {

    this.cs.commonerrorNew(err);
    this.spin.hide();

  }));
}else{
  this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
    this.toastr.success(res.statusId + " Saved Successfully!","Notification",{
      timeOut: 2000,
      progressBar: false,
    });
    this.spin.hide();
    this.dialogRef.close();

  }, err => {
    this.cs.commonerrorNew(err);
    this.spin.hide();

  }));
}

 }
 email = new FormControl('', [Validators.required, Validators.email]);
   public errorHandling = (control: string, error: string = "required") => {
     return this.form.controls[control].hasError(error) && this.submitted;
   }
   getErrorMessage() {
     if (this.email.hasError('required')) {
       return ' Field should not be blank';
     }
     return this.email.hasError('email') ? 'Not a valid email' : '';
   }
}
