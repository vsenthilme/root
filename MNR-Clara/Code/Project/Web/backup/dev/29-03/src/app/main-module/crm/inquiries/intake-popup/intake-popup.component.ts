import { Component, OnInit, Inject } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DialogExampleComponent, DialogData } from "src/app/common-field/dialog-example/dialog-example.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { InquiresService } from "../inquires.service";
import { SendIntakeComponent } from "../send-intake/send-intake.component";


@Component({
  selector: 'app-intake-popup',
  templateUrl: './intake-popup.component.html',
  styleUrls: ['./intake-popup.component.scss']
})
export class IntakePopupComponent implements OnInit {
  screenid: 1066 | undefined;

  panelOpenState = false;
  sub = new Subscription();
  form = this.fb.group({
    inquiryNumber: [],
    firstName: [, [Validators.maxLength(20), Validators.pattern('[a-zA-Z \S.-]+')]],
    contactNumber: [,[Validators.required,Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    email: [, [Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")]],
    alternateEmail: [, [Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")]],
    intakeFormId: [, Validators.required],
    referenceField8: [],
    deletionIndicator: [0],
    formurl: [],
    pageflow: ['New'],
    intakeFormNumber: [],
    statusId: []
  });


  email = new FormControl('', [Validators.required, Validators.email]);
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
  constructor(
    private dialog: MatDialog,
    public dialogRef: MatDialogRef<IntakePopupComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private cs: CommonService,
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,

    private auth: AuthService,
    private service: InquiresService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService, public toastr: ToastrService,) { }
  ngOnInit(): void {
    //inset by MK 01-03-22
    this.form.controls.inquiryNumber.disable();
    //end
    this.auth.isuserdata();
    this.fill();
    this.dropdownlist();

  }
  fill() {
    this.form.patchValue(
      this.data
    );

  }
  intakeFormIdList: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.intakeFormId.url]).subscribe((results: any) => {
      let rs = results[0];

      this.intakeFormIdList = this.cas.foreachlist(rs.filter((x: any) => x.classId == this.data.classId), this.cas.dropdownlist.setup.intakeFormId.key, { clientTypeId: 1 });

    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
  }
  openSend() {


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

    this.router.url;
    this.route.url;
    window.location.href;
    //if (this.form.controls.intakeFormId.value == '')



    let formname = this.cs.customerformname(this.form.controls.intakeFormId.value);



    if (formname == '') {
      this.toastr.error(
        "Select from is invalid.",
        ""
      );

      this.cs.notifyOther(true);
      return;
    }




    this.form.controls.pageflow.patchValue('newweb');
    this.form.controls.statusId.patchValue(7);//for send



    this.spin.show();
    this.sub.add(this.service.updateInquiryIntake(this.form.getRawValue(), this.form.controls.inquiryNumber.value).subscribe(res => {
      // this.dialogRef.close();

      this.sub.add(this.service.updateStatus_Intake({
        referenceField8: this.data.referenceField8,
        statusId: 7//for send
      }, this.form.controls.inquiryNumber.value).subscribe(res => {
        this.spin.hide();

        this.form.controls.intakeFormNumber.patchValue(res.intakeFormNumber);
        this.form.controls.formurl.patchValue(window.location.href.replace('main/crm/inquiryvalidate', 'mr/' + formname + '/' + this.cs.encrypt(this.form.getRawValue())));

        this.dialogRef.close();
        const dialogRef2 = this.dialog.open(SendIntakeComponent, {
          disableClose: true,
          width: '50%',
          maxWidth: '80%',
          // position: { top: '-6.7%', },
          data: this.form.getRawValue()
        });
console.log(this.data)
        dialogRef2.afterClosed().subscribe(result => {
        });

      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));



    }, err => {

      this.cs.commonerror(err);
      this.spin.hide();
    }));

  }

  openFill() {



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
    this.form.controls.statusId.patchValue(25);//for fill

    this.sub.add(this.service.updateInquiryIntake(this.form.getRawValue(), this.form.controls.inquiryNumber.value).subscribe(res => {
      this.spin.hide();
      this.sub.add(this.service.updateStatus_Intake({
        referenceField8: this.data.referenceField8,
        statusId: 25//for send

      }, this.form.controls.inquiryNumber.value).subscribe(res => {
        this.spin.hide();

        this.form.controls.intakeFormNumber.patchValue(res.intakeFormNumber);

        this.dialogRef.close();
        let formname = this.cs.customerformname(this.form.controls.intakeFormId.value);


        if (formname == '') {
          this.toastr.error(
            "Select from is invalid.",
            ""
          );

          this.cs.notifyOther(true);
          return;
        }
        this.router.navigate(['/main/crm/' + formname + '/' + this.cs.encrypt(this.form.getRawValue())]);

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

