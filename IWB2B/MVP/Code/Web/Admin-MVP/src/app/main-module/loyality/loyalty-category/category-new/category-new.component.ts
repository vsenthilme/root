import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { MessageService } from 'primeng/api';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ProfileNewComponent } from 'src/app/main-module/customers/customer-profile/profile-new/profile-new.component';
import { LoyaltyService } from '../../loyalty.service';

@Component({
  selector: 'app-category-new',
  templateUrl: './category-new.component.html',
  styleUrls: ['./category-new.component.scss']
})
export class CategoryNewComponent implements OnInit {

  
  
  form = this.fb.group({
    rangeId: [],
    category: [],
    categoryId: [],
    companyId: ["1000",],
    createdBy: [],
    createdOn: [],
    creditUnit: [],
    creditValuePoint: [],
    deletionIndicator: [],
    languageId: ["EN",],
    pointsFrom: [],
    pointsTo: [],
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
    status: ["Active",],
    updatedBy: [],
    updatedOn: [],
  });

  constructor(
    public dialogRef: MatDialogRef < CategoryNewComponent > ,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,
    public dialog: MatDialog,
    private router: Router, 
    private service: LoyaltyService,
    private messageService: MessageService) {
    
    }


  sub = new Subscription();
  submitted = false;
  btnText = 'Save'
  ngOnInit(): void {
    console.log(this.data)
    if (this.data.pageflow != 'New') {
      this.btnText = 'Update'
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
    }

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
  

  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code.rangeId).subscribe(res => {
        this.form.patchValue(res, {
          emitEvent: false
        });
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

  save() {
    this.submitted = true;
    if (this.form.invalid) {
      this.toastr.error(
        "Please fill required fields to continue",
        "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
      );

      this.cs.notifyOther(true);
      return;
    }

    this.cs.notifyOther(false);
    this.spin.show();
if(this.form.controls.categoryId.value == "1"){
  this.form.controls.category.patchValue('Silver')
}
if(this.form.controls.categoryId.value == "2"){
  this.form.controls.category.patchValue('Gold')
}
if(this.form.controls.categoryId.value == "3"){
  this.form.controls.category.patchValue('Diamond')
}
if(this.form.controls.categoryId.value == "4"){
  this.form.controls.category.patchValue('Platinum')
}
    
    this.form.controls.createdOn.patchValue(this.cs.day_callapiSearch(this.form.controls.createdOn.value));
    this.form.controls.updatedOn.patchValue(this.cs.day_callapiSearch(this.form.controls.updatedOn.value));

    if (this.data.code) {
      this.sub.add(this.service.Update(this.data.code.rangeId, this.form.getRawValue()).subscribe(res => {
        
        this.messageService.add({key: 'br', severity:'success', summary:'Success', detail: res.rangeId  + " updated successfully"});
        this.spin.hide();
        this.dialogRef.close();

      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    } else {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.messageService.add({key: 'br', severity:'success', summary:'Success', detail: res.rangeId  + " saved successfully"});
        this.spin.hide();
        this.dialogRef.close();

      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }

  }


}

