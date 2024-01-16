import { Component, OnInit, Inject } from "@angular/core";
import { FormControl, FormBuilder, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DialogExampleComponent, DialogData } from "src/app/common-field/dialog-example/dialog-example.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { GeneralMatterService } from "../../General/general-matter.service";
import { MattersNotesService } from "../matters-notes.service";


@Component({
  selector: 'app-matternotes-new',
  templateUrl: './matternotes-new.component.html',
  styleUrls: ['./matternotes-new.component.scss']
})
export class MatternotesNewComponent implements OnInit {
  screenid: 1101 | undefined;


  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  form = this.fb.group({
    classId: [this.auth.classId],
    clientId: [],
    createdBy: [this.auth.userID],
    createdOn: [],
    deletionIndicator: [0],
    languageId: [this.auth.languageId],
    matterNumber: [, Validators.required],
    noteTypeId: [, Validators.required],
    notesDescription: [, Validators.required],
    notesNumber: [],
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
    statusId: [18],
    updatedBy: [this.auth.userID],
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
    public dialogRef: MatDialogRef<MatternotesNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: MattersNotesService, private matterService: GeneralMatterService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
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

    if (this.data.matter) {
     // this.matter = ' Matter - (' + this.data.matter + ') - ';
      this.matter = '' + this.data.matter + ' / ' + this.data.matterdesc + ' - ';
      this.form.controls.matterNumber.patchValue(this.data.matter);
      // this.form.controls.matterNumber.disable();

    }
    this.getMatterDetails();

    if (this.data.pageflow != 'New') {
      if (this.data.pageflow == 'Display') {
        this.form.disable();
        this.isbtntext = false;

      }
      this.fill();
      this.btntext = "Update";

    }





  }
  noteTypeIdlist: any[] = [];
  statusIdlist: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.noteTypeId.url,

   
    this.cas.dropdownlist.setup.statusId.url,
      // this.cas.dropdownlist.matter.matterNumber.url,
    ]).subscribe((results) => {
      this.noteTypeIdlist = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.noteTypeId.key);
      // this.taskTypeList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.taskTypeCode.key);
      this.statusIdlist = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.statusId.key).filter(x => [18, 21].includes(x.key));
      // this.matterNumberList = this.cas.foreachlist_searchpage(results[3], this.cas.dropdownlist.matter.matterNumber.key);
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
    this.form.controls.languageId.patchValue("EN");
  }
  isbtntext = true;
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });

      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  getMatterDetails() {
    this.spin.show();
    this.sub.add(this.matterService.Get(this.form.controls.matterNumber.value).subscribe(res => {
      this.form.controls.clientId.patchValue(res.clientId);
      // this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.spin.hide();
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


    // this.form.controls.deadlineDate.patchValue(this.cs.date_task_api(this.form.controls.deadlineDate.value));
    // this.form.controls.reminderDate.patchValue(this.cs.date_task_api(this.form.controls.reminderDate.value));
    // this.form.controls.courtDate.patchValue(this.cs.date_task_api(this.form.controls.courtDate.value));


    this.form.patchValue({ updatedby: this.auth.username });
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
        this.toastr.success(res.notesNumber + " saved successfully!", "Notification", {
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