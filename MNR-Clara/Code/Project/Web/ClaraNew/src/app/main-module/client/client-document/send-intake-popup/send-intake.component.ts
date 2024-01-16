import { DatePipe } from "@angular/common";
import { Component, OnInit, Inject, OnDestroy } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Editor } from "ngx-editor";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { ClientDocumentService } from "../client-document.service";


export interface DialogData {
  animal: string;
  name: string;
}


@Component({
  selector: 'app-send-intake',
  templateUrl: './send-intake.component.html',
  styleUrls: ['./send-intake.component.scss']
})
export class SendIntakeComponent implements OnInit, OnDestroy {
  subjectenglish = "M&R Client Portal Credentials"
  subjectsapanish = "Introduccion para consulta inicial con Monty & Ramirez"
  test = "Dear (Field 1) Please find below the credentials of M&R Client Portal. https://mrclara.com/mnrclientapp/#/  Please enter you registered Mobile Number and enter the OTP(One Time Received) received in your phone for login into the Appilcation" ;
  email = new FormControl('', [Validators.required, Validators.email]);
  input: any;
  datetday = this.datepipe.transform(new Date(), "MM-dd-yyyy");
  sub = new Subscription();

  form = this.fb.group(
    {
      bodyText: ['<p><font size="2">Dear Field_1</font></p><p><font size="2"> Below, please find the credentials for the M&R Client Portal.&#160;</font></p><p><font size="2">https://mrclara.com/mnrclientapp/#/</font></a><br></p><p><font size="2">Please enter your registered mobile number or email address, then enter the OTP (One Time Password) you receive to log in to the portal. If you have any questions, please contact our office at (281) 493-5529.</font></a><br></p><p><font size="2">Thanks &amp; Regards,</font></p><p><font size="2">Team M&R</font></p>',
    ],
      ccAddress: ['intake@montyramirezlaw.com'],
      fromAddress: ['intake@montyramirezlaw.com'],
      subject: ['M&R Client Portal Credentials'],
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
    public dialogRef: MatDialogRef<SendIntakeComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any, private auth: AuthService,
    private spin: NgxSpinnerService, public toastr: ToastrService,
    private fb: FormBuilder, private cs: CommonService,
    public datepipe: DatePipe, private service: ClientDocumentService
  ) { }
  ngOnInit(): void {
    this.auth.isuserdata();
    this.fill();
    this.editor = new Editor();
    this.form.controls.fromAddress.disable();
  }

  // make sure to destory the editor
  ngOnDestroy(): void {

  }
  fill() {
    this.form.controls.toAddress.patchValue(this.data.emailId);
    if (this.data.alternateEmail)
      this.form.controls.ccAddress.patchValue(this.data.alternateEmail);

     this.form.controls.bodyText.patchValue(this.form.controls.bodyText.value.replace('Field_1', this.data.firstName).replace('linkurlform', this.data.formurl));
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
    this.sub.add(this.service.sendMailClient(this.form.getRawValue()).subscribe(res => {
      this.toastr.success("Client Portal credentials sent successfully to the client's Email", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.dialogRef.close();
      this.spin.hide();

    }, err => {
      this.toastr.error("Client Portal credentials sent successfully to the client's Email", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      this.cs.commonerror(err);
      this.spin.hide();

    }));
  }


  cancel() {
    this.dialogRef.close();
  }
}
