
  import { Component, Inject, OnInit } from '@angular/core';
  import { FormBuilder, FormControl, Validators } from '@angular/forms';
  import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
  import { NgxSpinnerService } from 'ngx-spinner';
  import { ToastrService } from 'ngx-toastr';
  import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
  import { CommonService } from 'src/app/common-service/common-service.service';
  import { AuthService } from 'src/app/core/core';
import { EmployeeService } from '../employee.service';
  

@Component({
  selector: 'app-employee-new',
  templateUrl: './employee-new.component.html',
  styleUrls: ['./employee-new.component.scss']
})
export class EmployeeNewComponent implements OnInit {

  
    
    form = this.fb.group({
      createdBy: [],
      codeId: [],
      createdOn: [],
      deletionIndicator: [],
      employeeCode:[, ],
      employeeEmail: [, ],
      employeeName:  [, Validators.required],
      employeePhone: [, ],
      employeeSbu:[, ],
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
      updatedBy: [],
      updatedOn: [],
    });
  
    constructor(
      public dialogRef: MatDialogRef<any>,
      @Inject(MAT_DIALOG_DATA) public data: any,
      public toastr: ToastrService,
      private spin: NgxSpinnerService,
      private auth: AuthService,
      private fb: FormBuilder,
      public cs: CommonService,
      private service: EmployeeService ,
      private cas: CommonApiService,
    ) { }
  
    ngOnInit(): void {
   this.dropdownlist()
      this.form.controls.createdBy.disable();
      this.form.controls.createdOn.disable();
      this.form.controls.updatedBy.disable();
      this.form.controls.updatedOn.disable();
      if (this.data.pageflow != 'New') {
        this.form.controls.employeeCode.disable();
        if (this.data.pageflow == 'Display')
          this.form.disable();
        this.fill();
      }
    }
    workOrderSbuList: any[] = [];

    dropdownlist() {
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.setup.sbu.url,
      ]).subscribe((results) => {
        this.workOrderSbuList = this.cas.foreachlist2(results[0], this.cas.dropdownlist.setup.sbu.key);
        //  this.statusList = this.cas.foreachlist1(results[4], this.cas.dropdownlist.setup.accountstatus.key);
      }, (err) => {
        this.toastr.error(err, "");
        this.spin.hide();
      });
    }

    public mask = [/\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]
    sub = new Subscription();
    submitted = false;
  
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
      this.form.controls.createdOn.patchValue(this.cs.dateapiutc0(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapiutc0(this.form.controls.updatedOn.value));
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
        this.toastr.success(res.employeeCode + " Saved Successfully!","Notification",{
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
  
  
  
  
  
