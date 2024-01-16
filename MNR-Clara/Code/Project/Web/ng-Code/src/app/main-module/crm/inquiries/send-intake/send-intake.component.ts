import { Component, OnInit, Inject, OnDestroy } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Editor } from "ngx-editor";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { IntakeService } from "../../intake-snap-main/intake.service";
import { InquiresService } from "../inquires.service";
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';


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
  subjectenglish = " introduction to initial Consultation with Monty & Ramirez"
  subjectsapanish = "Introduccion para consulta inicial con Monty & Ramirez"
  test = "Dear (Field 1) Thank you for contacting our law firm. The following are the details for our consultation scheduled for         at        .  Our offices are located at Houston & Dallas.  Please complete the following information prior to the consultation.  If you have any problems completing this form, please let us know.  Once you complete this form, submit it for our review";
  email = new FormControl('', [Validators.required, Validators.email]);
  input: any;
  datetday = this.cs.today();
  sub = new Subscription();

  englishBodyText = 'The following are the details for our consultation scheduled for _______ at _______. Our offices are located at Houston &amp; Dallas. Please complete the following information prior to the consultation. If you have any problems completing this form, please let us know. Once you complete this form, submit it for our review.'
  spanishBodyText = 'Los siguientes son los detalles de nuestra consulta programada para _______ a las _______. Nuestras oficinas están ubicadas en Houston &amp; Dallas. Por favor complete la siguiente información antes de la consulta. Si tiene algún problema para completar este formulario, por favor háganoslo saber. Una vez que complete este formulario, envíelo para que lo revisemos.'
  
  englishHeader = 'Thank you for contacting our law firm'
  spanishHeader = 'Gracias por ponerse en contacto con nuestro bufete de abogados.'

  englishClick = 'Click here to open Intake Form'
  spanishClick = 'Haga clic aquí para abrir el formulario de admisión'
  englishThanks = 'Thanks &amp; Regards';
  spanishThanks = 'Gracias &amp; Saludos';

  form = this.fb.group(
    {
      bodyText: ['<p><font size="2">Dear Field_1</font></p><p><font size="2">header&#160;</font></p><p><font size="2">bodyText</font></p><p><a href="linkurlform" target="_blank"><font size="2">clickhere</font></a><br></p><p><font size="2">thanks,</font></p><p><font size="2">M&amp;R Clara</font></p>',
      ],
      ccAddress: ['intake@montyramirezlaw.com'],
      fromAddress: ['intake@montyramirezlaw.com'],
      subject: ['Introduction to initial Consultation with Monty & Ramirez'],
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
    private serviceIntake: IntakeService,
    private fb: FormBuilder, private cs: CommonService, private service: InquiresService) { }
  ngOnInit(): void {
    console.log(this.data)
    this.auth.isuserdata();
    this.fill();
    this.editor = new Editor();
this.form.controls.fromAddress.disable();
  }

  // make sure to destory the editor
  ngOnDestroy(): void {

  }
  fill() {
    console.log(this.data)
    this.form.controls.toAddress.patchValue(this.data.email);
    if (this.data.alternateEmail)
      this.form.controls.ccAddress.patchValue(this.data.alternateEmail);
    if (this.data.intakeFormId == '')
      this.form.controls.toAddress.patchValue('Introduccion para consulta inicial con Monty & Ramirez');

    this.form.controls.bodyText.patchValue(this.form.controls.bodyText.value.replace('Field_1', this.data.firstName).replace('linkurlform', this.data.formurl));


    if(this.data.intakeFormId == 3 ){
      console.log('spanish')
    this.form.controls.bodyText.patchValue(this.form.controls.bodyText.value.replace('bodyText', this.spanishBodyText).replace('header', this.spanishHeader).replace('clickhere', this.spanishClick).replace('thanks',this.spanishThanks));
    }else{
      console.log('english')
      this.form.controls.bodyText.patchValue(this.form.controls.bodyText.value.replace('bodyText', this.englishBodyText).replace('header', this.englishHeader).replace('clickhere', this.englishClick).replace('thanks',this.englishThanks));
    }
    // this.form.controls.bodyText.patchValue('hello');
    // this.form.controls.subject.patchValue('hello');
  }
  onNoClick(): void {
    this.dialogRef.close();
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

    // this.form.patchValue({ updatedby: this.auth.userID, updatedOn: this.cs.todayapi() });
    this.sub.add(this.service.sendmail(this.form.getRawValue()).subscribe(res => {
      this.toastr.success("Intake form sent successfully to the client's email", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.dialogRef.close();
      this.spin.hide();

    }, err => {
      this.toastr.error("Intake form not sent to the Client's email", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      this.cs.commonerror(err);
      this.spin.hide();

    }));
  }


  cancel() {
    this.spin.show();

    // this.form.patchValue({ updatedby: this.auth.userID, updatedOn: this.cs.todayapi() });
    this.sub.add(this.serviceIntake.Delete(this.data.intakeFormNumber, this.data.inquiryNumber).subscribe(res => {
      this.spin.hide();
      this.spin.show();
      this.sub.add(this.service.Assign(this.data, this.data.inquiryNumber).subscribe(res => {
        this.dialogRef.close();
        this.spin.hide();

      }, err => {


        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }, err => {

      this.cs.commonerror(err);
      this.spin.hide();

    }));

  }
}
