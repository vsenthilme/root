import {
  HttpClient, HttpRequest,
  HttpResponse, HttpEvent, HttpErrorResponse
} from '@angular/common/http'
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ExpenseService } from 'src/app/main-module/setting/business/expense-code/expense.service';
import { GeneralMatterService } from '../../General/general-matter.service';
import { ReceiptNoService } from '../receipt-no.service';


interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-receipt-new',
  templateUrl: './receipt-new.component.html',
  styleUrls: ['./receipt-new.component.scss']
})
export class ReceiptNewComponent implements OnInit {
  screenid: 1133 | undefined;

  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  require: any;
  todaydate = new Date();
  form = this.fb.group({

    approvalDate: [],
    approvalDateFE: [],
    approvalReceiptDate: [],
    approvalReceiptDateFE: [],
    approvedOn: [],
    approvedOnFE: [],
    classId: [, Validators.required],
    clientId: [, Validators.required],
    createdBy: [this.auth.userID],
    createdOn: [],
    deletionIndicator: [0],
    documentType: [, Validators.required],
    dateGovt: [],
    dateGovtFE: [],
    eligibiltyDate: [,],
    eligibiltyDateFE: [,],
    expirationDate: [],
    expirationDateFE: [],
    languageId: [],
    matterNumber: [, Validators.required],
    receiptDate: [, ],
    receiptDateFE: [, Validators.required],
    receiptFile: [],
    receiptNo: [, Validators.required],
    receiptNote: [, Validators.required],
    receiptNoticeDate: [,],
    receiptNoticeDateFE: [,],
    receiptType: [],
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
    reminderDate: [,],
    reminderDateFE: [,],
    reminderDays: [,],
    statusId: [42, Validators.required],
    updatedBy: [],
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
  uploadbutton = true
  newPage = true;
  approved = false;
  constructor(
    public dialogRef: MatDialogRef<ReceiptNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: ReceiptNoService, private matterService: GeneralMatterService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService, public HttpClient: HttpClient,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService, private mexpenseservice: ExpenseService,
  ) { }
  matter: any;
  btntext = "Save"
  ngOnInit(): void {
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.reminderDateFE.disable();
    this.form.controls.statusId.disable();
    this.form.controls.receiptDateFE.patchValue(this.cs.todayCallApi());
    this.form.controls.reminderDays.valueChanges.subscribe(x => { if (x) this.calculatdate(); });



    this.form.controls.eligibiltyDateFE.valueChanges.subscribe(x => {
      if (x) {
        this.calculatdate();
      }
    });

    this.form.controls.statusId.valueChanges.subscribe(x => {
      if (x == 10){
      this.approved = true;
      }
      else{
        this.approved=false;
      }
    }
    
    );



    this.auth.isuserdata();
    this.dropdownlist();

    if (this.data.matter) {
      // this.matter = ' Matter - (' + this.data.matter + ') - ';
      this.matter = '' + this.data.matter + ' / ' + this.data.matterdesc + ' - ';
      this.form.controls.matterNumber.patchValue(this.data.matter);
      this.form.controls.matterNumber.disable();

    }
    this.getMatterDetails();

    if (this.data.pageflow != 'New') {
      if (this.data.pageflow == 'Display') {
        this.uploadbutton = false
        this.form.disable();
        this.isbtntext = false;

      }
      this.form.controls.receiptNo.disable();
      this.form.controls.statusId.enable();
      this.newPage = false;
      //this.fill();
      this.btntext = "Update";
      //  this.form.controls.documentType.disable();
      // this.form.controls.eligibiltyDate.setValidators(Validators.required);
      // this.form.controls.reminderDays.setValidators(Validators.required);
      this.form.updateValueAndValidity();
    }
  }
  calculatdate() {
    let reminderDate: Date = new Date(this.form.controls.eligibiltyDateFE.value);
    reminderDate.setDate(reminderDate.getDate() - Number(this.form.controls.reminderDays.value));
    this.form.controls.reminderDateFE.patchValue(reminderDate);
  };
  documentTypelist: any[] = [];
  statusIdlist: any[] = [];
  receiptTypelist: any[] = [];

  selectedItems: SelectItem[] = [];
  multiselectdocumentList: any[] = [];
  multidocumentList: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectreceiptList: SelectItem[] = [];
  multireceiptList: SelectItem[] = [];

  dropdownSettings = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };


  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.documentType.url,

    // this.cas.dropdownlist.setup.noteText.url,
    this.cas.dropdownlist.setup.statusId.url,
      // this.cas.dropdownlist.matter.matterNumber.url,
    ]).subscribe((results) => {
      this.documentTypelist = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.documentType.key);
      this.documentTypelist.forEach((x: { key: string; value: string; }) => this.multidocumentList.push({ value: x.key, label: x.value }))
      this.multiselectdocumentList = this.multidocumentList;
      // this.taskTypeList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.taskTypeCode.key);
      this.statusIdlist = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.statusId.key).filter(x => [42, 10].includes(x.key));
      // this.matterNumberList = this.cas.foreachlist_searchpage(results[3], this.cas.dropdownlist.matter.matterNumber.key);
      if (!this.newPage) {
        this.fill();
      }
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
    this.form.controls.languageId.patchValue("EN");
  }
  isbtntext = true;
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code.matterNumber, this.data.code.classId, this.data.code.receiptNo, this.data.code.languageId).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });

      this.form.controls.receiptDateFE.patchValue(this.cs.day_callapiSearch(this.form.controls.receiptDate.value));
      this.form.controls.receiptNoticeDateFE.patchValue(this.cs.day_callapiSearch(this.form.controls.receiptNoticeDate.value));
      this.form.controls.dateGovtFE.patchValue(this.cs.day_callapiSearch(this.form.controls.dateGovt.value));
      this.form.controls.eligibiltyDateFE.patchValue(this.cs.day_callapiSearch(this.form.controls.eligibiltyDate.value));
      this.form.controls.reminderDateFE.patchValue(this.cs.day_callapiSearch(this.form.controls.reminderDate.value));
      this.form.controls.expirationDateFE.patchValue(this.cs.day_callapiSearch(this.form.controls.expirationDate.value));
      this.form.controls.approvalDateFE.patchValue(this.cs.day_callapiSearch(this.form.controls.approvalDate.value));
      this.form.controls.approvalReceiptDateFE.patchValue(this.cs.day_callapiSearch(this.form.controls.approvalReceiptDate.value));
      this.form.controls.approvedOnFE.patchValue(this.cs.day_callapiSearch(this.form.controls.approvedOn.value));
      // this.multidocumentList.forEach(element => {
      //   if (element.value == res.documentType) {
      //     console.log(this.form)
      //     this.form.controls.documentType.patchValue([element.value]);
      //   }
      // });

      // let multiinquiryList: any[]=[]
      // multiinquiryList.push(res.documentType);
      // console.log(multiinquiryList)
      // this.form.patchValue({documentType: multiinquiryList });

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

      this.form.controls.statusId.patchValue(42);
      this.form.controls.classId.patchValue(res.classId);
      this.form.controls.languageId.patchValue(res.languageId);
      this.form.controls.clientId.patchValue(res.clientId);
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  files: File[] = [];
  myFormData!: FormData;
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

    this.form.controls.receiptDate.patchValue(this.cs.dateNewFormat(this.form.controls.receiptDateFE.value));
    this.form.controls.receiptNoticeDate.patchValue(this.cs.dateNewFormat(this.form.controls.receiptNoticeDateFE.value));
    this.form.controls.dateGovt.patchValue(this.cs.dateNewFormat(this.form.controls.dateGovtFE.value));

    this.form.controls.eligibiltyDate.patchValue(this.cs.dateNewFormat(this.form.controls.eligibiltyDateFE.value));
    this.form.controls.reminderDate.patchValue(this.cs.dateNewFormat(this.form.controls.reminderDateFE.value));
    this.form.controls.expirationDate.patchValue(this.cs.dateNewFormat(this.form.controls.expirationDateFE.value));
    this.form.controls.approvalDate.patchValue(this.cs.dateNewFormat(this.form.controls.approvalDateFE.value));
    this.form.controls.approvalReceiptDate.patchValue(this.cs.dateNewFormat(this.form.controls.approvalReceiptDateFE.value));
    this.form.controls.approvedOn.patchValue(this.cs.dateNewFormat(this.form.controls.approvedOnFE.value));

if(this.form.controls.statusId.value == 10 && !this.form.controls.eligibiltyDateFE.value && this.form.controls.documentType.value != "FOIAS" && this.form.controls.documentType.value != "I-140s"){

    this.toastr.error(
      "Please Fill Eligiblity Date to Continue",
      "Notification", {
      timeOut: 2000,
      progressBar: false,
    }
    );
    this.cs.notifyOther(false);
    return;
}
if(this.form.controls.statusId.value == 10 && !this.form.controls.reminderDays.value && this.form.controls.documentType.value != "FOIAS" && this.form.controls.documentType.value != "I-140s"){

  this.toastr.error(
    "Please Fill Reminder Days to Continue",
    "Notification", {
    timeOut: 2000,
    progressBar: false,
  }
  );
  this.cs.notifyOther(false);
  return;
}
//this.spin.show();
    // if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
    //   this.form.patchValue({ documentType: this.selectedItems[0]});
    // }
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');



    // this.form.controls.reminderDate.patchValue(this.cs.date_task_api(this.form.controls.reminderDate.value));
    // this.form.controls.courtDate.patchValue(this.cs.date_task_api(this.form.controls.courtDate.value));


    this.form.patchValue({ updatedby: this.auth.username });



    if (this.data.code) {
      if (this.files.length > 0) {
        this.spin.show();
        const config = new HttpRequest('POST', `/doc-storage/upload?location=receipt/${this.form.controls.clientId.value}/${this.form.controls.matterNumber.value}`, this.myFormData, {
          reportProgress: true
        })
        this.HttpClient.request(config)
          .subscribe((event: any) => {
            // this.httpEvent = event
            if (event instanceof HttpResponse) {
              this.spin.hide();
              this.toastr.success("Document uploaded successfully.", "Notification", {
                timeOut: 2000,
                progressBar: false,
              });
              this.form.controls.referenceField8.patchValue(event.body.file);
              this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code.receiptNo,
                this.form.controls.classId.value, this.form.controls.matterNumber.value,
                this.form.controls.documentType.value, this.form.controls.languageId.value
              ).subscribe(res => {
                this.dialogRef.close();
                this.toastr.success(res.receiptNo + " updated successfully!", "Notification", {
                  timeOut: 2000,
                  progressBar: false,
                });
                this.spin.hide();
              }, err => {
                this.cs.commonerror(err);
                this.spin.hide();

              }));
            }
          },
            error => {
              this.spin.hide();
              this.cs.commonerror(error);
            })
      } else {
        console.log(this.form.getRawValue())
        this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code.receiptNo,
          this.form.controls.classId.value, this.form.controls.matterNumber.value,
          this.form.controls.documentType.value, this.form.controls.languageId.value
        ).subscribe(res => {
          this.dialogRef.close();
          this.toastr.success(res.receiptNo + " updated successfully!", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
          this.spin.hide();
        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }));
      }
    }
    else {
      if (this.files.length > 0) {
        this.spin.show();
        const config = new HttpRequest('POST', `/doc-storage/upload?location=receipt/` + this.form.controls.clientId.value + '/' + this.form.controls.matterNumber.value, this.myFormData, {
          reportProgress: true
        })
        this.HttpClient.request(config)
          .subscribe((event: any) => {
            // this.httpEvent = event
            if (event instanceof HttpResponse) {
              this.form.controls.referenceField8.patchValue(event.body.file);
              this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
                this.spin.hide();
                this.toastr.success(res.receiptNo + " saved successfully!", "Notification", {
                  timeOut: 2000,
                  progressBar: false,
                });
                this.dialogRef.close();
              }, err => {
                this.cs.commonerror(err);
                this.spin.hide();
              }));
              this.toastr.success("Document uploaded successfully.", "Notification", {
                timeOut: 2000,
                progressBar: false,
              });
            }
          },
            error => {
              this.spin.hide();

              this.cs.commonerror(error);

            })
      }
      else {
        this.toastr.error("Please upload the document.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
      }
      // }, err => {
      //   this.cs.commonerror(err);
      //   this.spin.hide();

      // }));
    }
  };
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

}