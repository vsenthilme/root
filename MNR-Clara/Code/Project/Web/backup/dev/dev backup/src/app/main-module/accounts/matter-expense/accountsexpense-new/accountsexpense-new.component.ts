import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ExpensesNewComponent } from "src/app/main-module/matters/case-management/expenses/expenses-new/expenses-new.component";
import { MatterExpensesService } from "src/app/main-module/matters/case-management/expenses/matter-expenses.service";
import { GeneralMatterService } from "src/app/main-module/matters/case-management/General/general-matter.service";
import { ExpenseService } from "src/app/main-module/setting/business/expense-code/expense.service";

  interface SelectItem {
    id: string;
    itemName: string;
  }

  @Component({
    selector: 'app-accountsexpense-new',
    templateUrl: './accountsexpense-new.component.html',
    styleUrls: ['./accountsexpense-new.component.scss']
  })
  export class AccountsexpenseNewComponent implements OnInit {

   // screenid: 1170 | undefined;
  
  
    sub = new Subscription();
    email = new FormControl('', [Validators.required, Validators.email]);
  
  
    form = this.fb.group({
      billType: [, ],
      caseCategoryId: [],
      caseInformationNo: [],
      caseSubCategoryId: [],
      classId: [],
      clientId: [],
      costPerItem: [],
      createdBy: [this.auth.userID],
      createdOn: [],
      deletionIndicator: [0],
      expenseAccountNumber: [],
      expenseAmount: [],
      expenseCode: [, Validators.required],
      expenseDescription: [],
      expenseType: [, Validators.required],
      languageId: [this.auth.languageId],
      matterNumber: [, Validators.required],
      numberofItems: [1, Validators.required],
      rateUnit: [],
      referenceField1: [,],
      referenceField10: [],
      referenceField2: [],
      referenceField3: [],
      referenceField4: [],
      referenceField5: [],
      referenceField6: [],
      referenceField7: [],
      referenceField8: [],
      referenceField9: [],
      statusId: [38],
      updatedBy: [this.auth.userID],
      updatedOn: [],
      writeOff: [],
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
      public dialogRef: MatDialogRef<ExpensesNewComponent>,
      @Inject(MAT_DIALOG_DATA) public data: any,
      private service: MatterExpensesService, private matterService: GeneralMatterService,
      public toastr: ToastrService,
      private cas: CommonApiService,
      private spin: NgxSpinnerService,
      private auth: AuthService, private fb: FormBuilder,
      private cs: CommonService, private mexpenseservice: ExpenseService,
    ) { }
    matter: any;
    btntext = "Save"
    ngOnInit(): void {
  
      this.form.controls.createdBy.disable()
      this.form.controls.createdOn.disable()
      this.form.controls.updatedBy.disable()
      this.form.controls.updatedOn.disable()
  
      this.form.controls.costPerItem.disable();
      this.form.controls.referenceField5.disable();
  
      this.auth.isuserdata();
      this.dropdownlist();
      // this.form.controls.expenseCode.valueChanges.subscribe(x => {
      //   this.form.controls.costPerItem.patchValue(0);
      //   if (x)
      //     this.sub.add(this.mexpenseservice.Get(x).subscribe(res => {
      //       this.form.controls.costPerItem.patchValue(res.costPerItem);
      //       this.form.controls.referenceField5.patchValue(res.costType);
      //       this.calculatAmount();
      //       this.spin.hide();
      //     }, err => {
      //       this.cs.commonerror(err);
      //       this.spin.hide();
      //     }));
      // });
      this.form.controls.numberofItems.valueChanges.subscribe(x => { this.calculatAmount(); });
      if (this.data.matter) {
            // this.matter = ' Matter - (' + this.data.matter + ') - ';
            this.matter = '' + this.data.matter + ' / ' + this.data.matterdesc + ' - ';
        this.form.controls.matterNumber.patchValue(this.data.matter);
        // this.form.controls.matterNumber.disable();
  
      }
      this.getMatterDetails();
  
      if (this.data.pageflow != 'New') {
        this.form.controls.referenceField5.disable();
        this.form.controls.costPerItem.disable();
        if (this.data.pageflow == 'Display') {
          this.form.disable();
          this.isbtntext = false;
  
        }
        this.newPage = false;
      //  this.fill();
        this.btntext = "Update";
  
      }
    }
    calculatAmount() {
      this.form.controls.expenseAmount.patchValue(this.form.controls.numberofItems.value as number * this.form.controls.costPerItem.value as number);
  
    };
    expenseCodelist: any[] = [];
    statusIdlist: any[] = [];
  
    selectedItems: SelectItem[] = [];
    multiselectexpenseList: SelectItem[] = [];
    multiexpenseList: SelectItem[] = [];
  
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
      this.cas.getalldropdownlist([this.cas.dropdownlist.setup.expenseCode.url,
  
      // this.cas.dropdownlist.setup.noteText.url,
      this.cas.dropdownlist.setup.statusId.url,
        // this.cas.dropdownlist.matter.matterNumber.url,
      ]).subscribe((results) => {
        this.expenseCodelist = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.expenseCode.key);
        this.expenseCodelist.forEach((x: { key: string; value: string; }) => this.multiexpenseList.push({id: x.key, itemName:  x.value}))
        this.multiselectexpenseList = this.multiexpenseList;
        // this.taskTypeList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.taskTypeCode.key);
        this.statusIdlist = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.statusId.key).filter(x => [18, 21].includes(x.key));
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
        this.multiexpenseList.forEach(element => {
          if(element.id == res.expenseCode){
            console.log(this.form)
            this.form.controls.expenseCode.patchValue([element]);
          }
        });
  
        this.form.controls.writeOff.patchValue(res.writeOff ? 'true' : 'false');
  
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
        this.form.controls.statusId.patchValue(38);
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
      if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
        this.form.patchValue({expenseCode: this.selectedItems[0].id });
      }
      this.form.removeControl('updatedOn');
      this.form.removeControl('createdOn');
  
  
      // this.form.controls.deadlineDate.patchValue(this.cs.date_task_api(this.form.controls.deadlineDate.value));
      // this.form.controls.reminderDate.patchValue(this.cs.date_task_api(this.form.controls.reminderDate.value));
      // this.form.controls.courtDate.patchValue(this.cs.date_task_api(this.form.controls.courtDate.value));
  
  
      this.form.patchValue({ updatedby: this.auth.username });
      if (this.data.code) {
        this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code).subscribe(res => {
          this.toastr.success(res.expenseCode + " updated successfully!","Notification",{
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
          this.toastr.success(res.expenseCode + " saved successfully!", "Notification", {
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
      console.log(item);
      console.log(this.selectedItems);
  
      this.form.controls.costPerItem.patchValue(0);
      if (item.id)
        this.sub.add(this.mexpenseservice.Get(item.id).subscribe(res => {
          this.form.controls.costPerItem.patchValue(res.costPerItem);
          this.form.controls.referenceField5.patchValue(res.costType);
          this.calculatAmount();
          this.spin.hide();
        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }));
  
  }
  
  }