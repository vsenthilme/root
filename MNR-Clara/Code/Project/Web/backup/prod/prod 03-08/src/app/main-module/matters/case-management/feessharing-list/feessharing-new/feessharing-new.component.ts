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
import { MatterFeesSharingService } from '../matter-fees-sharing.service';

interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-feessharing-new',
  templateUrl: './feessharing-new.component.html',
  styleUrls: ['./feessharing-new.component.scss']
})
export class FeessharingNewComponent implements OnInit {
  screenid: 1111 | undefined;


  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);


  form = this.fb.group({

    //timeKeeperCodeFE: [, Validators.required],
    timeKeeperCode: [, ],
    caseCategoryId: [],
    caseSubCategoryId: [],
    classId: [],
    clientId: [],
    createdBy: [],
    createdOn: [this.auth.userID],
    deletionIndicator: [],
    feeSharingPercentage: [],
    languageId: [],
    matterNumber: [, Validators.required],
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
    statusId: [],
 
    updatedBy: [],
    updatedOn: [this.auth.userID],
    statusIddes: [],
    name: [],

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
  newPage = true;

  constructor(
    public dialogRef: MatDialogRef<FeessharingNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: MatterFeesSharingService, private matterService: GeneralMatterService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService, private mexpenseservice: ExpenseService,
  ) { }
  matter: any;
  btntext = "Save"
  ngOnInit(): void {

    this.form.controls.createdOn.disable();
    this.form.controls.createdBy.disable();
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
    //  this.fill();
    this.dropdownlist();
    this.newPage = false;
      this.btntext = "Update";
      this.form.controls.timeKeeperCode.disable();

    }
  }

  timeKeeperCodelist: any[] = [];
  statusIdlist: any[] = [];

  selectedItems: any[] = [];
  multiselecttimekkeperList: any[] = [];
  multitimekkeperList: any[] = [];

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
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.timeKeeperCode.url,

      // this.cas.dropdownlist.setup.noteText.url,
      // this.cas.dropdownlist.setup.statusId.url,
      // this.cas.dropdownlist.matter.matterNumber.url,
    ]).subscribe((results) => {
      this.timeKeeperCodelist = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.timeKeeperCode.key);
      this.timeKeeperCodelist.forEach((x: { key: string; value: string; }) => this.multitimekkeperList.push({value: x.key, label:  x.value}))
      this.multiselecttimekkeperList = this.multitimekkeperList;
      if(!this.newPage){
        this.fill();
      }
      // this.taskTypeList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.taskTypeCode.key);
      // this.statusIdlist = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.statusId.key).filter(x => [18, 21].includes(x.key));
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
    this.sub.add(this.service.Get(this.data.timeKeeperCode, this.data.code).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });

      // let multiinquiryList: any[]=[]
      // multiinquiryList.push(res.timeKeeperCode);
      // console.log(multiinquiryList)
      // this.form.patchValue({timeKeeperCode: multiinquiryList });
      
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

      this.form.patchValue(res, { emitEvent: false });
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

    // if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
    //   this.form.patchValue({timeKeeperCode: this.selectedItems[0]});
    // }

    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');


    // this.form.controls.deadlineDate.patchValue(this.cs.date_task_api(this.form.controls.deadlineDate.value));
    // this.form.controls.reminderDate.patchValue(this.cs.date_task_api(this.form.controls.reminderDate.value));
    // this.form.controls.courtDate.patchValue(this.cs.date_task_api(this.form.controls.courtDate.value));


    this.form.patchValue({ updatedby: this.auth.username });
    if (this.data.code) {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.timeKeeperCode, this.data.code).subscribe(res => {
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
        this.toastr.success(res.timeKeeperCode + " saved successfully!", "Notification", {
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