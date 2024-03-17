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
  subjectenglish = "Monty & Ramirez LLP – Consultation – "+(this.data.inquiryNumber)
  subjectsapanish = "Monty & Ramirez LLP – Consulta – "
  test = "Dear (Field 1) Thank you for contacting our law firm. The following are the details for our consultation scheduled for         at        .  Our offices are located at Houston & Dallas.  Please complete the following information prior to the consultation.  If you have any problems completing this form, please let us know.  Once you complete this form, submit it for our review";
  email = new FormControl('', [Validators.required, Validators.email]);
  input: any;
  datetday = this.cs.today();
  sub = new Subscription();

  englishBodyText = 'Your appointment is scheduled for ___________________at ____. '
  englishBodyText3 = 'We are located on the <b>4th floor</b> of the Torre Latina at 150 W. Parker Rd. Houston, TX 77076.'
  englishBodyText4 = 'Please arrive 15 minutes prior to your appointment.'
  englishBodyText5 = 'In preparation for your appointment, please fill out a questionnaire using the following link:'
  englishBodyText6 = 'The cost of the consultation is $_____ ___ .'
  englishBodyText7 = 'We accept debit or credit cards and cash.  '
  englishBodyText8 = 'If you have any questions feel free to call us at 713.289.4546, our office hours are 8:30 a.m. to 5:30 p.m. Monday through Friday.  '
  englishBodyText1="Please bring the requested documents _____________________or send them to customerservice@montyramirezlaw.com."
  spanishBodyText = 'Su cita está programada para el ___________________ a las _________ con la abogada, ____<b>ATTORNEY NAME</b>____ '
  spanishBodyText3 = 'Nos ubicamos en el <b>4to piso</b> de la Torre Latina en 150 W. Parker Rd. Houston, TX 77076.'
  spanishBodyText4 = 'Por favor llegue 15 minutos antes de su cita.'
  spanishBodyText5 = 'En preparación a su cita favor de llenar el cuestionario usando el siguiente enlace:'
  spanishBodyText6 = 'El costo de la consulta es de $_______ .'
  spanishBodyText7 = 'Aceptamos tarjetas de débito o crédito y en efectivo.   '
  spanishBodyText8 = 'Si tiene alguna pregunta no dude llamarnos al 713.289.4546, nuestro horario de oficina es de 8:30 a.m. a 5:30 p.m. de Lunes a Viernes.  '
  spanishBodyText1="Favor de traer documentos en relación con su caso, como copias de _____________________."
  englishHeader = 'Thank you for contacting Monty & Ramirez LLP.'
  spanishHeader = 'Gracias por contactar a Monty & Ramirez LLP.'
  
  englishClick = 'Click here to open Intake Form'
  spanishClick = 'Haga clic aquí para abrir el formulario de admisión'
  englishThanks = 'Thank you';
  spanishThanks = 'Gracias';
   
  form = this.fb.group(
    {
      bodyText: ['<p><b><font size="2">Field_1,</font></b></p><p><font size="2">header&#160;</font></p><p><font size="2">bodyText</font></p><p><font size="2">bodyText3</font></p><p><font size="2">bodyText4</font></p><p><font size="2">bodyText5</font></p><p><a href="linkurlform" target="_blank"><font size="2">clickhere</font></a></p><p><font size="2">bodyText1</font></p><p><font size="2">bodyText6</font></p><p><font size="2">bodyText7</font></p><p><font size="2">bodyText8</font></p><p><font size="2">thanks,</font></p><p><b><font size="2">USER</font></b></p><p><font size="2">Monty &amp; Ramirez</font></p>',
      ],
      ccAddress: ['intake@montyramirezlaw.com'],
      fromAddress: ['intake@montyramirezlaw.com'],
      subject: ['Monty & Ramirez LLP –Consultation –'+(this.data.inquiryNumber)],
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
  inq:any;
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
      this.form.controls.bodyText.patchValue(this.form.controls.bodyText.value.replace('bodyText', this.spanishBodyText).replace('bodyText3', this.spanishBodyText3).replace('bodyText4', this.spanishBodyText4).replace('bodyText5', this.spanishBodyText5).replace('header', this.spanishHeader).replace('clickhere', this.spanishClick).replace('bodyText1',this.spanishBodyText1).replace('bodyText6',this.spanishBodyText6).replace('bodyText7',this.spanishBodyText7).replace('bodyText8',this.spanishBodyText8).replace('USER',this.auth.userfullName).replace('thanks',this.spanishThanks));
   this.form.controls.subject.patchValue('Monty & Ramirez LLP – Consulta-'+this.data.inquiryNumber)
    }else{
      console.log('english')
      this.form.controls.bodyText.patchValue(this.form.controls.bodyText.value.replace('bodyText', this.englishBodyText).replace('bodyText3', this.englishBodyText3).replace('bodyText4', this.englishBodyText4).replace('bodyText5', this.englishBodyText5).replace('header', this.englishHeader).replace('clickhere', this.englishClick).replace('bodyText1',this.englishBodyText1).replace('bodyText6',this.englishBodyText6).replace('bodyText7',this.englishBodyText7).replace('bodyText8',this.englishBodyText8).replace('USER',this.auth.userfullName).replace('thanks',this.englishThanks));
    
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
