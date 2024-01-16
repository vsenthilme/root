import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Validators, FormBuilder, FormControl } from "@angular/forms";
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { PaymentService } from '../payment.service';
import { GeneralMatterService } from 'src/app/main-module/matters/case-management/General/general-matter.service';

@Component({
  selector: 'app-payment-crud-new',
  templateUrl: './payment-crud-new.component.html',
  styleUrls: ['./payment-crud-new.component.scss']
})
export class PaymentCrudNewComponent implements OnInit {

  //screenid: 1101 | undefined;


  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  form = this.fb.group({
    
    classId:          [, ],
    clientId:         [],
    createdBy:        [],
    createdOn:        [],
    invoiceNumber:    [],
    languageId:       ['EN',],
    matterNumber:     [, Validators.required],
    paymentAmount:    [, Validators.required],
    paymentCode:      [],
    paymentDate:     [, Validators.required],
    paymentId:        [],
    paymentNumber:    [],
    paymentText:      [],
    postingDate:      [, ],
    referenceField1:  [],
    referenceField10: [],
    referenceField2:  [],
    referenceField3:  [],
    referenceField4:  [],
    referenceField5:  [],
    referenceField6:  [],
    referenceField7:  [],
    referenceField8:  [],
    referenceField9:  [],
    statusId:         [],
    transactionType:  [],
    updatedBy: [],
    updatedOn: [],
  });


  submitted = false;
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


  constructor(
    public dialogRef: MatDialogRef<PaymentCrudNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: PaymentService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private matterService: GeneralMatterService,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService,
  ) { }
  matter: any;
  btntext = "Save"
  ngOnInit(): void {

    this.form.controls.createdOn.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    this.auth.isuserdata();
    this.dropdownlist();
   
  }
  matterNumberList: any[] = [];
  classList: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.matter.dropdown.url,
      this.cas.dropdownlist.matter.matterNumberList.url,
    ]).subscribe((results: any) => {
      results[0].classList.forEach((x: any) => {
        
        this.classList.push({ value: x.key, label: x.key + '-' + x.value });
      })
      results[1].matterDropDown.forEach(matter => {
        this.matterNumberList.push({ value: matter.matterNumber, label: (matter.matterNumber + "-" + matter.matterDescription), client: matter.clientId, });
      });
      if (this.data.pageflow != 'New') {
        if (this.data.pageflow == 'Display') {
          this.form.disable();
          this.isbtntext = false;
        }
        this.fill();
        this.btntext = "Update";
  
      }
      this.spin.hide();
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
  }
  isbtntext = true;

  onItemSelect(item) {
    this.matterService.SearchNew({matterNumber: [item.value]}).subscribe(res => {
      if(res[0].classId == "LABOR&EMPLOYMENT"){
        this.form.controls.classId.patchValue(1); 
        if(res[0].classId == "IMMIGRATION"){
        this.form.controls.classId.patchValue(2); 
        }
      }
    })
    this.matterNumberList.forEach(x => {
      if(x.value == item.value){
        this.form.controls.clientId.patchValue(x.client);
      }
    })
  }

  fill() {
  //  this.spin.show();
    this.sub.add(this.service.Search({paymentId: [this.data.code]}).subscribe(res => {
      this.form.patchValue(res[0], { emitEvent: false });
      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
  //  this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  submit() {
    this.submitted = true;
    if (this.form.invalid) {
         this.toastr.error(
        "Please fill the required fields to continue",
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
    }
    else {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.toastr.success(res.paymentId + " saved successfully!", "Notification", {
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
  };
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

}