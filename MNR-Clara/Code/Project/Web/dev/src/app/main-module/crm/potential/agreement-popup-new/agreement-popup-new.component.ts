import { Component, OnInit, Inject } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DialogExampleComponent, DialogData } from "src/app/common-field/dialog-example/dialog-example.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { AgreementDocumentComponent } from "../agreement-document/agreement-document.component";
import { PotentialService } from "../potential.service";

interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-agreement-popup-new',
  templateUrl: './agreement-popup-new.component.html',
  styleUrls: ['./agreement-popup-new.component.scss']
})
export class AgreementPopupNewComponent implements OnInit {
  screenid: 1077 | undefined;
  sub = new Subscription();

  form = this.fb.group({
    potentialClientId: [, Validators.required],
    agreementCode: [, Validators.required],
    caseCategoryId: [, Validators.required],
    emailId: [, [Validators.required, Validators.email ]],
    firstNameLastName: [, Validators.required],
    inquiryNumber: [, Validators.required],
    intakeFormId: [, Validators.required],
    statusId: [, Validators.required],
  });
  panelOpenState = false;

  email = new FormControl('', [Validators.required, Validators.email]);
  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    // if (this.email.hasError('required')) {
    //   return ' Field should not be blank';
    // }
    return this.email.hasError('email') ? 'Not a valid email' : this.email.hasError('required') ? ' Field should not be blank' : '';
  }


  constructor(
    private router: Router,
    public dialogRef: MatDialogRef<AgreementPopupNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private auth: AuthService,
    private fb: FormBuilder,
    private spin: NgxSpinnerService, public toastr: ToastrService,
    private cas: CommonApiService,
    private cs: CommonService,
    private service: PotentialService) { }
  ngOnInit(): void {
    this.form.controls.potentialClientId.disable();
    this.form.controls.inquiryNumber.disable();
    this.form.controls.intakeFormId.disable();
    this.auth.isuserdata();
    this.dropdownlist();
    this.form.patchValue(this.data);
    this.form.controls.firstNameLastName.patchValue(this.data.firstName);
  }
  caseCategoryIdList: any[] = [];
  agreementList: any[] = [];

  selectedItems: SelectItem[] = [];
  multiselectcasesubList: any[] = [];
  multicasesubList: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectagreementList: any[] = [];
  multiagreementList: any[] = [];

  dropdownSettings = {
    singleSelection: true,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };


  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.caseCategoryId.url,
      this.cas.dropdownlist.setup.agreementCode.url
    ]).subscribe((results) => {
      this.caseCategoryIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.caseCategoryId.key);
      this.caseCategoryIdList.forEach((x: { key: string; value: string; }) => this.multicasesubList.push({value: x.key, label:  x.value}))
      this.multiselectcasesubList = this.multicasesubList;
      this.agreementList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.agreementCode.key);
      this.agreementList.forEach((x: { key: string; value: string; }) => this.multiagreementList.push({value: x.key, label:  x.value}))
      this.multiselectagreementList = this.multiagreementList;
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();

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

    // if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
    //   this.form.patchValue({caseCategoryId: this.selectedItems[0] });
    // }

    // if (this.selectedItems2 != null && this.selectedItems2 != undefined && this.selectedItems2.length > 0) {
    //   this.form.patchValue({agreementCode: this.selectedItems2[0] });
    // }

    this.sub.add(this.service.Agreement(this.form.getRawValue(), this.form.controls.potentialClientId.value).subscribe(res => {


      this.sub.add(this.service.Get_agreementTemplate(this.form.controls.agreementCode.value).subscribe(res => {
        if (res.mailMerge)
          this.toastr.success("Agreement mailmerged successfully.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        else
          this.toastr.success("Download and update the agreement template.", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });

      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
      this.spin.hide();
      this.dialogRef.close();
      this.router.navigate(['main/crm/agreementdocument/' + this.cs.encrypt({ agreementCode: res.agreementCode, potentialClientId: res.potentialClientId, pageflow: 'agreement' })]);


    }, err => {

      this.cs.commonerror(err);
      this.spin.hide();

    }));

  }

}

