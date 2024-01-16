import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ClientDocumentService } from '../client-document.service';

@Component({
  selector: 'app-document-new',
  templateUrl: './document-new.component.html',
  styleUrls: ['./document-new.component.scss']
})
export class DocumentNewComponent implements OnInit {
  screenid: 1092 | undefined;

  form = this.fb.group({
    approvedOn: [],
    classId: [],
    clientId: [, Validators.required],
    clientUserId: [],
    createdBy: [this.auth.userID],
    createdOn: [],
    deletionIndicator: [0],
    documentDate: [],
    documentNo: [, Validators.required],
    documentUrl: [],
    documentUrlVersion: [],
    languageId: [, Validators.required],
    matterNumber: [,],
    receivedOn: [],
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
    resentOn: [],
    sentBy: [],
    sentOn: [],
    statusId: [19],
    updatedBy: [this.auth.userID],
    updatedOn: [],
    caseCategoryId: [],
  });

  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);


  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  filteredcasecat: any;
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
    public dialogRef: MatDialogRef<DocumentNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: ClientDocumentService,
    public toastr: ToastrService, private router: Router,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService,
  ) { }
  client: any;
  btntext = "Save"
  ngOnInit(): void {
  
    this.form.controls.documentUrl.disable();

    this.auth.isuserdata();
    this.dropdownlist();

    this.form.controls.matterNumber.valueChanges.subscribe(x => {
      this.form.controls.caseCategoryId.patchValue('');
      if (x)
        this.get_matterdetails(x);
    }
    );
    this.form.controls.documentNo.valueChanges.subscribe(x => {
      this.form.controls.documentUrl.patchValue('');
      if (x)
        this.get_document_template(x);
    }
    );


    // this.form.controls.caseCategoryId.valueChanges.subscribe(x => {
    //   this.form.controls.documentNo.patchValue('');
    //   if (x)
    //     this.documentcode();
    // }
    // );

    // this.form.controls.caseSubCategoryId.valueChanges.subscribe(x => {
    //   this.form.controls.documentNo.patchValue('');
    //   if (x)
    //     this.documentcode();
    // }
    // );
    if (this.data.clientId) {
      console.log(this.data)
 //     this.client = ' Client - (' + this.data.clientId + ') - ';
      this.client = '' + this.data.clientId + ' / ' + this.data.clientName + ' - ';
      this.form.controls.clientId.patchValue(this.data.clientId);
      // this.form.controls.matterNumber.disable();

    }

    if (this.data.pageflow != 'New') {
      if (this.data.pageflow == 'Display') {
        this.form.disable();
        this.isbtntext = false;

      }
      this.fill();
      this.btntext = "Update";

    }





  }

  // documentcode() {
  //   this.spin.show();
  //   this.cas.getalldropdownlist([this.cas.dropdownlist.setup.documentcode.url,

  //   ]).subscribe((results) => {
  //     this.documentcodelist = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.documentcode.key, {
  //       // caseSubCategoryId: this.form.controls.caseSubCategoryId.value,
  //       caseCategoryId: this.form.controls.caseCategoryId.value
  //     });
  //   }, (err) => {
  //     this.toastr.error(err, "");
  //   });
  //   this.spin.hide();
  // }

  documentcodelist: any[] = [];
  statusIdlist: any[] = [];
  matterNumberList: any[] = [];
  caseCategoryIdList: any[] = [];
  caseSubCategoryIdList: any[] = [];
  classIDList: any;
  dropdownlist() {
    this.spin.show();

    this.sub.add(this.service.GetClient(this.data.clientId).subscribe(res => {
      this.classIDList = res.classId;
      this.form.controls.classId.patchValue(res.classId)
      this.spin.hide();

    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));


    this.cas.getalldropdownlist([

      // this.cas.dropdownlist.setup.noteText.url,
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.matter.matterNumber.url,
      this.cas.dropdownlist.setup.caseCategoryId.url,
      this.cas.dropdownlist.setup.caseSubcategoryId.url,
      this.cas.dropdownlist.setup.documentcode.url,

    ]).subscribe((results) => {
      // this.documentcodelist = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.documentcode.key);
      // this.taskTypeList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.taskTypeCode.key);
      this.statusIdlist = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.statusId.key).filter(x => [18, 21].includes(x.key));

      this.matterNumberList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.matter.matterNumber.key, { clientId: this.form.controls.clientId.value });
      this.caseCategoryIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.caseCategoryId.key);
      this.caseSubCategoryIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.caseSubcategoryId.key);
    //  this.documentcodelist = this.cas.foreachlist(results[4], this.cas.dropdownlist.setup.documentcode.key, { referenceField1: "TRUE"});
    console.log(this.filteredcasecat)
         this.documentcodelist = this.cas.foreachlist(results[4], this.cas.dropdownlist.setup.documentcode.key, { referenceField1: "TRUE" , classId: this.classIDList});
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
    this.form.controls.languageId.patchValue("EN");
  }
  isbtntext = true;
  mailMerge = false;
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });

      // this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      // this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  get_matterdetails(code: any) {
    this.spin.show();
    this.sub.add(this.service.get_matterdetails(code).subscribe(res => {
      this.form.controls.caseCategoryId.patchValue(res.caseCategoryId);
      this.filteredcasecat = this.form.controls.caseCategoryId.value
      this.dropdownlist()
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  get_document_template(code: any) {
    this.spin.show();
    this.sub.add(this.service.Get_documentTemplate(code).subscribe(res => {
      this.form.controls.documentUrl.patchValue(res.documentUrl);
      this.spin.hide();
      this.mailMerge = res.mailMerge;
      if (!res.mailMerge) {
        this.form.controls.statusId.patchValue(22);
      }
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


    this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {


      if (this.mailMerge)
        this.toastr.success("Document mail merged successfully.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
      else
        this.toastr.success("Download and update the document template.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });

      this.spin.hide();

      this.router.navigate(['main/client/document/' + this.cs.encrypt({ code: res.clientDocumentId, pageflow: 'document' })]);
      this.dialogRef.close();

    }, err => {

      this.cs.commonerror(err);
      this.spin.hide();

    }));

  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

}



