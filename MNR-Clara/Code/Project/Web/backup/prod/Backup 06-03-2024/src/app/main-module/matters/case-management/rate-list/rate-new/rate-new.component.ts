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
import { TimekeeperService } from 'src/app/main-module/setting/business/timekeeper/timekeeper.service';
import { GeneralMatterService } from '../../General/general-matter.service';
import { MatterRateService } from '../matter-rate.service';

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-rate-new',
  templateUrl: './rate-new.component.html',
  styleUrls: ['./rate-new.component.scss']
})
export class RateNewComponent implements OnInit {
  screenid: 1109 | undefined;


  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);


  form = this.fb.group({
    assignedRatePerHour: [, Validators.required],
    caseCategoryId: [],
    caseSubCategoryId: [],
    classId: [],
    clientId: [],
    createdBy: [this.auth.userID],
    createdOn: [],
    defaultRatePerHour: [,],
    deletionIndicator: [],
    languageId: [],
    matterNumber: [, Validators.required],
    rateUnit: [,],
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
    timeKeeperCode: [, Validators.required],
    //timeKeeperCodeFE: [, Validators.required],
    updatedBy: [this.auth.userID],
    statusIddes: [],
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
    public dialogRef: MatDialogRef<RateNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: MatterRateService, private matterService: GeneralMatterService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService, private timekeeperService: TimekeeperService,
  ) { }
  matter: any;
  btntext = "Save"
  ngOnInit(): void {

    this.form.controls.createdOn.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.updatedOn.disable();
    this.form.controls.updatedBy.disable();


    this.auth.isuserdata();
    this.dropdownlist();
    this.form.controls.defaultRatePerHour.disable();
    // this.form.controls.timeKeeperCode.valueChanges.subscribe(x => {
    //   console.log(x)
    //   this.form.controls.rateUnit.patchValue(0);
    //   if (x) {
    //     this.spin.show();

    //     this.sub.add(this.timekeeperService.Get(x).subscribe(res => {
    //       this.form.controls.defaultRatePerHour.patchValue(res.defaultRate);
    //       this.form.controls.assignedRatePerHour.patchValue(res.defaultRate);

    //       this.spin.hide();
    //     }, err => {
    //       this.cs.commonerror(err);
    //       this.spin.hide();
    //     }));
    //   }
    // });


    if (this.data.matter) {
      // this.matter = ' Matter - (' + this.data.matter + ') - ';
      this.matter = '' + this.data.matter + ' / ' + this.data.matterdesc + ' - ';
      this.form.controls.matterNumber.patchValue(this.data.matter);
      // this.form.controls.matterNumber.disable();

    }
    this.getMatterDetails();

    if (this.data.pageflow != 'New') {
   //   this.form.controls.timeKeeperCode.disable();
      if (this.data.pageflow == 'Display') {
        this.form.disable();
        this.isbtntext = false;

      }
      this.fill();
      this.btntext = "Update";
      this.form.controls.timeKeeperCodeFE.disable();

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
      console.log(res.timeKeeperCode)
      this.form.patchValue(res, { emitEvent: false });
   

      // this.multitimekkeperList.forEach(element => {
      //   if(element.value == res.timeKeeperCode){
      //     console.log(this.form)
      //     this.form.controls.timeKeeperCodeFE.patchValue([element.value]);
      //   }
      // });

      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));

      this.sub.add(this.timekeeperService.Get(res.timeKeeperCode).subscribe(res => {
        this.form.controls.defaultRatePerHour.patchValue(res.defaultRate);
  
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));

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
    console.log(this.selectedItems[0])
    // if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
    //   console.log(this.selectedItems[0])
    //   this.form.patchValue({timeKeeperCode: this.selectedItems[0] });
    //   console.log(this.form.controls.timeKeeperCode.value)
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

  onItemSelect(item:any){
    console.log(this.form.controls.timeKeeperCode.value);
    console.log(this.selectedItems);

    this.form.controls.rateUnit.patchValue(0);
    if (this.form.controls.timeKeeperCode.value) {
      this.spin.show();

      this.sub.add(this.timekeeperService.Get(this.form.controls.timeKeeperCode.value).subscribe(res => {
        this.form.controls.defaultRatePerHour.patchValue(res.defaultRate);
        this.form.controls.assignedRatePerHour.patchValue(null);

        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
}
}