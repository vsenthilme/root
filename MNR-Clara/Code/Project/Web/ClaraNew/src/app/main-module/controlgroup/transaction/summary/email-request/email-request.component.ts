import { DatePipe } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { Editor } from 'ngx-editor';
import { OwnershipService } from '../../ownership/ownership.service';
import { SendEmailApprovalComponent } from '../../proposed/send-email-approval/send-email-approval.component';


@Component({
  selector: 'app-email-request',
  templateUrl: './email-request.component.html',
  styleUrls: ['./email-request.component.scss']
})
export class EmailRequestComponent implements OnInit {
 
  subjectenglish = "Updated Employee Count"
  email = new FormControl('', [Validators.required, Validators.email]);
  input: any;
  datetday = this.cs.todayapi();
  sub = new Subscription();

 // englishBodyText = 'The following are the details for our consultation scheduled for _______ at _______. Our offices are located at Houston &amp; Dallas. Please complete the following information prior to the consultation. If you have any problems completing this form, please let us know. Once you complete this form, submit it for our review.'
 englishBodyText = "Dear Customer, Please find the attached Employee Count report updated based on the request";
  englishHeader =  "Dear Customer, Please find the attached Employee Count report updated based on the request";

  englishClick = 'Click here to open Intake Form'
  englishThanks = 'Thanks &amp; Regards';


  //<p><a href="linkurlform" target="_blank"><font size="2">clickhere</font></a><br></p>
  //<p><font size="2">bodyText</font></p>
  form = this.fb.group(
    {
      bodyText: ['<p><font size="2">Dear Field_1</font></p><p><font size="2">header&#160;</font></p><p><font size="2">thanks,</font></p><p><font size="2">M&amp;R Clara</font></p>',
      ],
      ccAddress: [],
      fromAddress: ['intake@montyramirezlaw.com'],
      subject: ['Updated Employee Count'],
      toAddress: [, [Validators.required, Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")]],
    });
  //
  editor: Editor = new Editor();
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
  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    // if (this.email.hasError('required')) {
    //   return ' Field should not be blank';
    // }
    return this.email.hasError('email') ? 'Not a valid email' : this.email.hasError('email') ? ' Field should not be blank' : '';
  }
  panelOpenState = false;
  constructor(
    public dialogRef: MatDialogRef<SendEmailApprovalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any, private auth: AuthService,
    private spin: NgxSpinnerService, public toastr: ToastrService,
    private fb: FormBuilder, private cs: CommonService, private service: OwnershipService) { }
  ngOnInit(): void {
    this.auth.isuserdata();
    this.fill();
    this.editor = new Editor();
    this.form.controls.fromAddress.disable();

    console.log(this.data)
  }


  ngOnDestroy(): void {

  }
  fill() {
    this.form.controls.toAddress.patchValue('mwillars@montyramirezlaw.com');
    if (this.data.alternateEmail)
      this.form.controls.ccAddress.patchValue(this.data.alternateEmail);

    this.form.controls.bodyText.patchValue(this.form.controls.bodyText.value.replace('Field_1', 'Madeleine Willars')) //this.data.firstName    //.replace('linkurlform', this.data.formurl))
    this.form.controls.bodyText.patchValue(this.form.controls.bodyText.value.replace('header', this.englishHeader))
  }
  onNoClick(): void {
    this.dialogRef.close();
  }
  submit() {

    this.submitted = true;
    if (this.form.invalid) {
      this.toastr.error(
        "Please fill the required fields to continue",
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

    this.sub.add(this.service.sendEmailWithAttachment(this.form.getRawValue(), this.data.element.referenceField1).subscribe(res => {
      this.toastr.success("Email sent successfully", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.dialogRef.close("email Sent");
      this.spin.hide();

    }, err => {
      this.toastr.error("Email failed to send", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      this.cs.commonerror(err);
      this.spin.hide();

    }));
  }

}
