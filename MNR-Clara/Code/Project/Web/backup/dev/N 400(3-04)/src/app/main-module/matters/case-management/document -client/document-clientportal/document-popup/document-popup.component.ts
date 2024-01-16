import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscriber, Subscription } from 'rxjs';
import { subscribeOn } from 'rxjs/operators';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { MatterDocumetService } from '../../../matter-document/matter-documet.service';

@Component({
  selector: 'app-document-popup',
  templateUrl: './document-popup.component.html',
  styleUrls: ['./document-popup.component.scss']
})
export class DocumentPopupComponent implements OnInit {

  form = this.fb.group({
    classId: [],
    clientId: [, Validators.required],
    clientUserId: [],
    createdBy: [this.auth.userID],
    createdOn: [],
    deletionIndicator: [0],
    documentDate: [],
    documentNo: [, ],
    documentNoFE: [, Validators.required],
    documentUrl: [],
    documentUrlVersion: [],
    languageId: [, Validators.required],
    matterNumber: [{ value: '', disabled: true }, Validators.required],
    matterNumberFE: [{ value: '', disabled: true }],
    receivedOn: [],
    referenceField1: [],
    referenceField10: [],
    referenceField2: [],
    referenceField3: [],
    referenceField4: [],
    referenceField5: ["Client Portal"],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    resentOn: [],
    sentBy: [],
    sentOn: [],
    statusId: [22],
    updatedBy: [this.auth.userID],
    caseCategoryId: [],
    caseSubCategoryId: [],
    docuemntDate: [],
    matterDocumentId: [],
    sequenceNo: [],
    mailMerge: [{ value: 'true', disabled: true }]
  });

  sub = new Subscription();

  documentcodelist: any[] = [];
  statusIdlist: any[] = [];
  matterNumberList: any[] = [];
  caseCategoryIdList: any[] = [];
  caseSubCategoryIdList: any[] = [];

  selectedItems: any[] = [];
  multiselectdoList: any[] = [];
  multidocList: any[] = [];

  dropdownSettings = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };
  submitted = false;
  mailMerge = false;
  matter: string;
  matterfielddata: any;

  constructor(
    public dialogRef: MatDialogRef<DocumentPopupComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: MatterDocumetService,
    public toastr: ToastrService, private router: Router,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService,
  ) { }

  ngOnInit(): void {
    if (this.data.matterNumber) {
      this.matterfielddata = this.data.matterNumber + ' / ' + this.data.matterdesc 
      // this.matter = ' Matter - (' + this.data.matter + ') - ';
      this.matter = '' + this.data.matterNumber + ' / ' + this.data.matterdesc + ' - ';
      this.form.controls.matterNumber.patchValue(this.data.matter);
      // this.form.controls.matterNumber.disable();
      this.form.controls.matterNumberFE.patchValue( this.matterfielddata);


    }

    this.dropdownlist();

    this.form.controls.matterNumber.patchValue( this.data.matterNumber);
    this.form.controls.matterNumberFE.patchValue( this.matterfielddata);
    // this.form.controls.matterNumber.disable();
    this.get_matterdetails(this.data.matterNumber);

  }

  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([

      // this.cas.dropdownlist.setup.noteText.url,
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.matter.matterNumber.url,
      this.cas.dropdownlist.setup.caseCategoryId.url,
      this.cas.dropdownlist.setup.caseSubcategoryId.url,
      this.cas.dropdownlist.setup.documentcode.url,
    ]).subscribe((results) => {
      this.statusIdlist = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.statusId.key).filter(x => [18, 21].includes(x.key));
      this.matterNumberList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.matter.matterNumber.key, { clientId: this.form.controls.clientId.value });
      this.caseCategoryIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.caseCategoryId.key);
      this.caseSubCategoryIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.caseSubcategoryId.key);
      this.documentcodelist = this.cas.foreachlist(results[4], this.cas.dropdownlist.setup.documentcode.key, { referenceField1: "TRUE", classId: this.classIDList});
      this.documentcodelist.forEach((x: { key: string; value: string; }) => this.multidocList.push({ value: x.key, label: x.value }))
      this.multiselectdoList = this.multidocList;

    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
    this.form.controls.languageId.patchValue("EN");
  }
  classIDList: any;
  get_matterdetails(code: any) {
    this.spin.show();
    this.sub.add(this.service.get_matterdetails(code).subscribe(res => {
      this.form.controls.caseCategoryId.patchValue(res.caseCategoryId);
      this.form.controls.clientId.patchValue(res.clientId);
      this.form.controls.caseSubCategoryId.patchValue(res.caseSubCategoryId);
      // console.log(res.classId)
      this.form.controls.classId.patchValue(res.classId);
      //console.log(this.form.controls.classId.value)
      this.classIDList = res.classId;
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  getErrorMessage(type: string) {
    if (!this.form.valid && this.submitted) {
      if (this.form.controls[type].hasError('required')) {
        return 'Field should not be blank';
      } else {
        return '';
      }
    } else {
      return '';
    }
  }

  public errorHandling = (control: string, error: string = "required") => {

    if (control.includes('.')) {
      const controls = this.form.get(control);
      return controls ? controls.hasError(error) : false;

    }
    return this.form.controls[control].hasError(error);
  }

  onItemSelect(item: any) {
    this.form.controls.documentUrl.patchValue('');
    if (item.value[0])
      this.get_document_template(item.value[0]);
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
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      this.cs.notifyOther(true);
      return;
    }
    if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
      this.form.patchValue({ documentNo: this.selectedItems[0]});
    }
    else {
      this.form.patchValue({ documentNo: null });
    }
    this.cs.notifyOther(false);
    this.spin.show();


    this.sub.add(this.service.createClientPortalDocs(this.form.getRawValue()).subscribe(res => {
      if (this.mailMerge)
        this.toastr.success("Document Mail merged successfully.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
      else
        this.toastr.success("Download and update the document template.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });

      this.spin.hide();
      this.dialogRef.close();
      this.router.navigate(['main/matters/case-management/document_template/' + 'CLIENT_DOCUMENT/' + this.cs.encrypt({ code: res.matterDocumentId, pageflow: 'document' })]);
      this.dialogRef.close();

    }, err => {

      this.cs.commonerror(err);
      this.spin.hide();

    }));

  }
}
