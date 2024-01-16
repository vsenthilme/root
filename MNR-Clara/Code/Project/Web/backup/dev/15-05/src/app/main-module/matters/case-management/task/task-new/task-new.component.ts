import { Component, OnInit, Inject } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DialogExampleComponent, DialogData } from "src/app/common-field/dialog-example/dialog-example.component";
import { AddStringArrayPopupComponent } from "src/app/common-field/dialog_modules/add-string-array-popup/add-string-array-popup.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { TaskMatterService } from "../task-matter.service";

interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-task-new',
  templateUrl: './task-new.component.html',
  styleUrls: ['./task-new.component.scss']
})
export class TaskNewComponent implements OnInit {
  screenid: 1115 | undefined;
  input: any;

  todaydate = new Date();
  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  form = this.fb.group({

    caseCategoryId: [,],
    caseSubCategoryId: [,],
    classId: [,],
    clientId: [,],
    courtDate: [,],
    deadlineCalculationDays: [,],
    deadlineDate: [,Validators.required],
    deletionIndicator: [0,],
    languageId: [,],
    matterNumber: [,],
    priority: [,],
    referenceField1: [,],
    referenceField10: [,],
    referenceField2: [,],
    referenceField3: [,],
    referenceField4: [,],
    referenceField5: [, ],
    referenceField6: [,],
    referenceField7: [,],
    referenceField8: [,],
    referenceField9: [,],
    reminderCalculationDays: [, ],
    reminderDate: [,],
    statusId: [,],
   // taskAssignedToFE: [, Validators.required],
    taskAssignedTo: [, Validators.required],
    taskCompletedBy: [,],
    taskCompletedOn: [,],
    taskDescription: [, Validators.required],
    taskEmailId: [, [Validators.required]],
    taskName: [, Validators.required],
    taskNumber: [,],
    //taskTypeCodeFE: [, Validators.required],
    taskTypeCode: [, ],
    timeEstimate: [,],
    createdOn: [],
    createdBy: [],
    updatedOn: [],
    updatedBy: []
  });

  selectedOption = 'deadlinedays';
  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  taskTypeListt: any[] = [];
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
  emailArray:any[] = [];
  newPage = true;

  constructor(
    public dialogRef: MatDialogRef<TaskNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: TaskMatterService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService,
    public dialog: MatDialog,
  ) { }
  matter: any;
  btntext = "Save"
  ngOnInit(): void {
    
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    //inset my MK 2/3/22
    // this.form.controls.deadlineDate.disable();
    this.form.controls.reminderDate.disable();
    //end of inset
    // this.form.controls.taskAssignedTo.valueChanges.subscribe(val => {
    //   if (val) this.taskAssignedToemail(val);
    // });
    this.form.controls.reminderCalculationDays.valueChanges.subscribe(val => {
      this.datecalculate();
    });

    this.form.controls.deadlineCalculationDays.valueChanges.subscribe(val => {
      this.datecalculate();
    });
    this.form.controls.deadlineDate.valueChanges.subscribe(val => {
      this.datecalculate();
    });
    this.form.controls.courtDate.valueChanges.subscribe(val => {
      this.datecalculate();
    });
    this.form.controls.matterNumber.valueChanges.subscribe(val => {
      if (val) this.getdeadline();
    });
    this.form.controls.taskTypeCode.valueChanges.subscribe(val => {
      if (val) this.getdeadline();
    });
    this.auth.isuserdata();
   this.dropdownlist();
this.getAllMatter();

    if (this.data.matter) {
      // this.matter = ' Matter - (' + this.data.matter + ') - ';
      this.matter = '' + this.data.matter + ' / ' + this.data.matterdesc + ' - ';
      this.form.controls.matterNumber.patchValue(this.data.matter);
      // this.form.controls.matterNumber.disable();

    }


    if (this.data.pageflow != 'New') {
      if (this.data.pageflow == 'Display') {
        this.form.disable();
        this.isbtntext = false;

      }
  this.dropdownlist();
  this.getAllMatter();
      this.newPage = false;
      this.btntext = "Update";

      this.form.controls.taskNumber.disable();
    }





  }
  userlist: any[] = [];
  statusIdlist: any[] = [];
  matterNumberList: any[] = [];


  selectedItems: SelectItem[] = [];
  multiselectassignList: any[] = [];
  multiassignList: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselecttaskList: any[] = [];
  multitaskList: any[] = [];

  dropdownSettings = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };


  taskTypeList: any;

  getAllMatter() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.matter.matterNumberList.url,
    ]).subscribe((results: any) => {
      this.spin.hide();
      results[0].matterDropDown.forEach((x: any) => {
        this.matterNumberList.push({ value: x.matterNumber, label: x.matterNumber + '-' + x.matterDescription });
      })
    }, (err) => {
      this.spin.hide();
      this.toastr.error(err, "");
    });
  }

  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.userId.url,

    this.cas.dropdownlist.setup.taskTypeCode.url,
    this.cas.dropdownlist.setup.statusId.url,
   // this.cas.dropdownlist.matter.matterNumber.url,
    ]).subscribe((results) => {

      
      this.userlist = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.userId.key);
      this.userlist.forEach((x: { key: string; value: string; }) => this.multiassignList.push({ value: x.key, label: x.value }))
      this.multiselectassignList = this.multiassignList;

      // this.taskTypeList = this.cas.foreachlist1(results[1], this.cas.dropdownlist.setup.taskTypeCode.key);
      // this.taskTypeList.forEach((x: { key: string; value: string; }) => this.multitaskList.push({ value: x.key, label: x.value }))
      // this.multiselecttaskList = this.multitaskList;
      this.taskTypeList = results[1];
      console.log(this.taskTypeList)
      this.taskTypeList.forEach((x: { taskTypeCode: string; taskTypeDescription: string;  referenceField1: string;}) => 
      this.multitaskList.push({ value: x.taskTypeCode, label: x.taskTypeDescription + ' - ' + x.referenceField1}));
      this.multiselecttaskList = this.multitaskList;

      this.statusIdlist = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.statusId.key).filter(x => [31, 32].includes(x.key));

     // this.matterNumberList = this.cas.foreachlist_searchpage(results[3], this.cas.dropdownlist.matter.matterNumber.key);
 

      if(!this.newPage){
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
    this.sub.add(this.service.Get(this.data.code).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
      if (this.form.controls.taskEmailId.value) {
        console.log(this.form.controls.taskEmailId.value)
        let stringEmail: String = this.form.controls.taskEmailId.value;
        let array = stringEmail.split(',');
        array.forEach((element: any) => {
          this.emailArray.push({ email: element });
        })

        this.form.controls.taskEmailId.patchValue(array.length > 0 ? array[0] : res.taskEmailId );
      }
      // this.multiassignList.forEach(element => {
      //   if (element.id == res.taskAssignedTo) {
      //     console.log(this.form)
      //     this.form.controls.taskAssignedToFE.patchValue([element]);
      //   }
      // });
      // this.multitaskList.forEach(element => {
      //   if (element.value == res.taskTypeCode) {
      //     console.log(this.form)
      //     console.log(element.value)
      //     this.form.controls.taskTypeCode.patchValue(element.value);
      //   }
      // });

      // let multitasktypeList: any[]=[]
      // multitasktypeList.push(res.taskTypeCode);
      // console.log(multitasktypeList)
      // this.form.patchValue({taskTypeCodeFE: multitasktypeList });


//       let multiinquiryList1: any[]=[]
//       multiinquiryList1.push(res.taskAssignedTo);
//       this.form.patchValue({taskAssignedToFE: multiinquiryList1 });

//  this.multitaskList.forEach(element => {
//         if (element.id == res.taskTypeCode) {
//           console.log([element])
//           this.form.controls.taskTypeCodeFE.patchValue([element]);
//         }
//       });

 


      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  saved = false;
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
    this.spin.show();
    this.saved =true;
    this.cs.notifyOther(false);

    // if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
    //   this.form.patchValue({ taskAssignedTo: this.selectedItems[0]});
    // }
    // if (this.selectedItems2 != null && this.selectedItems2 != undefined && this.selectedItems2.length > 0) {
    //   this.form.patchValue({ taskTypeCode: this.selectedItems2[0].id});
    // }
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');


    // this.form.controls.deadlineDate.patchValue(this.cs.date_task_api(this.form.controls.deadlineDate.value));
    // this.form.controls.reminderDate.patchValue(this.cs.date_task_api(this.form.controls.reminderDate.value));
    // this.form.controls.courtDate.patchValue(this.cs.date_task_api(this.form.controls.courtDate.value));

    // let mailString = "";
    // this.emailArray.forEach((email: any, i) => {
    //   if (i == 0) {
    //     mailString = email.email;
    //   } else {
    //     if (mailString != "") {
    //       mailString = mailString + ',' + email.email;
    //     }
    //   }
    // })
    // this.form.controls.taskEmailId.patchValue(mailString);
    // console.log(this.form.controls.taskEmailId.value)

    this.form.patchValue({ updatedby: this.auth.username });
    if (this.data.code) {
      this.spin.show();
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code).subscribe(res => {
        this.toastr.success(this.data.code + " updated successfully!", "Notification", {
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
      this.spin.show();
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.toastr.success(res.taskNumber + " saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
       // this.spin.hide();
        this.dialogRef.close();

      }, err => {
        this.cs.commonerror(err);
       /// this.spin.hide();

      }));
    }
  };
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
  datecalculate() {

    if (this.form.controls.deadlineDate.value) {
      let courtDate: Date = new Date(this.form.controls.deadlineDate.value);
      courtDate.setDate(courtDate.getDate() - Number(this.form.controls.reminderCalculationDays.value));
      let deadlineDate = new Date(courtDate);
      this.form.controls.reminderDate.patchValue(deadlineDate);
      //this.addDays(new Date(courtDate), this.form.controls.deadlineCalculationDays.value));
      // this.form.controls.reminderDate.patchValue(this.addDays(new Date(this.form.controls.deadlineDate.value), this.form.controls.reminderCalculationDays.value));
    }
  }

  taskAssignedToemail(user: any): void {

    this.spin.show();
    this.sub.add(this.service.GetUser(user).subscribe(res => {
      this.form.controls.taskEmailId.patchValue(res.emailId);
      this.emailArray[0] = {email: res.emailId};
      // this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      // this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  getdeadline() {

  //  this.spin.show();
    this.sub.add(this.service.GetMatterDetails(this.form.controls.matterNumber.value).subscribe(res => {
      this.form.controls.caseCategoryId.patchValue(res.caseCategoryId);
    //  this.spin.hide();
//this.spin.show();


      this.sub.add(this.service.GetdeadlineCalculationDays(this.form.controls.matterNumber.value).subscribe(res => {

     //   this.spin.hide();
        let element = res.filter((x: any) => x.caseCategoryId == this.form.controls.caseCategoryId.value)
          // let element = res.filter((x: any) => x.caseCategoryId == this.form.controls.caseCategoryId.value &&
          // x.taskTypeCode == this.form.controls.taskTypeCode.value)
        if (element.length > 0)
          //this.form.controls.deadlineCalculationDays.patchValue(element[0].deadLineCalculationDays);
          this.form.controls.reminderCalculationDays.patchValue(element[0].deadLineCalculationDays);
      }, err => {
        this.cs.commonerror(err);
   //     this.spin.hide();
      }));
    }, err => {
      this.cs.commonerror(err);
 //     this.spin.hide();
    }));
  }

  onItemSelect(item: any) {
    console.log(item);
    console.log(this.selectedItems);

    if (this.form.controls.taskAssignedTo.value) this.taskAssignedToemail(this.form.controls.taskAssignedTo.value);

  }

  addEmail() {
    const dialogRef = this.dialog.open(AddStringArrayPopupComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: this.emailArray
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(this.emailArray)
      let mailString = "";
      this.emailArray.forEach((email: any, i) => {
        if (i == 0) {
          mailString = email.email;
        } else {
          if (mailString != "") {
            mailString = mailString + ',' + email.email;
          }
        }
      })
    this.form.controls.taskEmailId.patchValue(mailString);
    console.log(this.form.controls.taskEmailId.value)
    });
  }

}


