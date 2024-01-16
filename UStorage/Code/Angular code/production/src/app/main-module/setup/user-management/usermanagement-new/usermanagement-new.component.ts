import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { UsermanagementService } from '../usermanagement.service';

@Component({
  selector: 'app-usermanagement-new',
  templateUrl: './usermanagement-new.component.html',
  styleUrls: ['./usermanagement-new.component.scss']
})
export class UsermanagementNewComponent implements OnInit {

      
  form = this.fb.group({
     city: [],
        company: [],
        country: [],
        email: [, ],
        firstname: [],
        id:  [],
        lastname: [],
        password:  [, [Validators.required]],
        role: [, [Validators.required]],
        state: [],
        username:  [, [Validators.required]],
        userTypeId: [],
        phoneNo: [],
        status: [],
  });

  constructor(
    public dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,
    private service: UsermanagementService
  ) { }
  public mask = [/\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]
  ngOnInit(): void {

    // this.form.controls.createdBy.disable();
    // this.form.controls.createdOn.disable();
    // this.form.controls.updatedBy.disable();
    // this.form.controls.updatedOn.disable();
    if (this.data.pageflow != 'New') {
      this.form.controls.id.disable();
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
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
  
  sub = new Subscription();
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

  panelOpenState = false;

  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code).subscribe(res => {
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
  
  if (this.data.code) {
    this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code).subscribe(res => {
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
      this.toastr.success(res.id + " Saved Successfully!","Notification",{
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




