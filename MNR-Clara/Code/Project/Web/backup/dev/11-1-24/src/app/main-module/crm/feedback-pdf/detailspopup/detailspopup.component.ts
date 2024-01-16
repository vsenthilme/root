import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DialogExampleComponent } from 'src/app/common-field/dialog-example/dialog-example.component';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ClassElement } from 'typescript';
import { SpanishFeedbackpdfService } from '../spanish-feedbackpdf.service';
import { HttpClient } from '@angular/common/http';
import { InquiresService } from '../../inquiries/inquires.service';
import { IntakeService } from '../../intake-snap-main/intake.service';

@Component({
  selector: 'app-detailspopup',
  templateUrl: './detailspopup.component.html',
  styleUrls: ['./detailspopup.component.scss']
})
export class DetailspopupComponent implements OnInit {
  public mask = [/\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]

  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  form = this.fb.group({
    contactNumberNew: [,Validators.required],
    contactNumber: [,Validators.required],
    intakeFormNumber: [],
    url: [],
    name: [],
    formName: [],
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
  formgr: ClassElement | undefined;

  panelOpenState = false;
  constructor(
    public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: SpanishFeedbackpdfService,
    public toastr: ToastrService,
    private http: HttpClient,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private serivceInquiry: InquiresService,
    private serivceIntake: IntakeService,
    private auth: AuthService,
    private fb: FormBuilder,
    private cs: CommonService,
  ) { }
  ngOnInit(): void {
    console.log(this.data)
    this.generateURL();
    this.getNumber();

    this.form.controls.name.disable();
    this.form.controls.intakeFormNumber.disable();
    this.form.controls.formName.disable();

    this.form.controls.name.patchValue(this.data.code.name);
    this.form.controls.intakeFormNumber.patchValue(this.data.code.intakeFormNumber);
    this.form.controls.formName.patchValue(this.data.code.intakeFormId_des);
    
  }
  submit() {
    this.submitted = true;
    const str = this.form.controls.contactNumberNew.value.split("-");
    let number =  str[0] + str[1] + str[2];
    this.form.controls.contactNumber.patchValue(number);
    
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



    let obj: any = {};
    obj.contactNumber = this.form.controls.contactNumber.value;
    obj.url = this.form.controls.url.value;

    let obj1: any = {};
    obj1.feedbackStatus = "Form Sent";
    obj1.statusId = this.data.code.statusId;

    this.sub.add(this.service.smsFeedBack(this.form.controls.intakeFormNumber.value, obj).subscribe(res => {
      this.toastr.success("Survey form sent to the client successfullty","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.sub.add(this.serivceIntake.Update(obj1, this.form.controls.intakeFormNumber.value).subscribe(res => {

      }, err =>{
        this.cs.commonerror(err);
        this.spin.hide();
      }));
      this.spin.hide();
      this.dialogRef.close();

    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();

    }));
  };

  onNoClick(): void {
    this.dialogRef.close();
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }


  formPage: any;
  generateURL(){
    this.spin.show();
    if(this.data.code.intakeFormId == 3){
      this.formPage = 'spnfeedback';
    }else{
      this.formPage = 'feedback';
    }
    const apiUrl = 'https://api.tinyurl.com/create?api_token=2ilUBE7d9iVBqUwY68o0v4XpxbBgtE5jXTeChNGWLAOkvXZiBcnMFyChvzNJ';

      const requestBody = {
        url: window.location.href.replace('/main/crm/inquiryform', '/mr/'+ this.formPage + '/' + this.cs.encrypt(this.data.code))
      };

    let link = window.location.href.replace('/main/crm/inquiryform', '/mr/'+ this.formPage + '/' + this.cs.encrypt(this.data.code));
   
    this.http.post(apiUrl, requestBody).subscribe((response: any) => {
      const shortenedURL = response.data.tiny_url;
      this.form.controls.url.patchValue(shortenedURL);
      this.spin.hide();
    }, (err) => {
     this.cs.commonerror(err);
     this.spin.hide();
    });
    
  }

  getNumber(){
    this.spin.show();
    this.serivceInquiry.Get(this.data.code.inquiryNumber).subscribe(res => {
      if(res){
        this.form.controls.contactNumberNew.patchValue(res.contactNumber);
      }
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    })
  }
}




