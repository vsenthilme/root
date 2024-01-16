import { Component, OnInit, Inject } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { disable } from "@rxweb/reactive-form-validators";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { TimekeeperService } from "../timekeeper.service";

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-timekeeper-display',
  templateUrl: './timekeeper-display.component.html',
  styleUrls: ['./timekeeper-display.component.scss']
})
export class TimekeeperDisplayComponent implements OnInit {
  screenid: 1018 | undefined;
  email = new FormControl('', [Validators.required, Validators.email]);
  input: any;

  sub = new Subscription();
  form = this.fb.group({
    classId: [, [Validators.required]],
    defaultRate: [, [Validators.required]],
    deletionIndicator: [0],
    languageId: [, [Validators.required]],
    rateUnit: [0],
    timekeeperCode: [, [Validators.required]],
    timekeeperName: [, [Validators.required]],
    timekeeperStatus: ["Active", [Validators.required]],
    userTypeId: [, [Validators.required]],

    createdBy: [this.auth.userID, [Validators.required]],
    createdOn: [],
    updatedOn: [],
    updatedBy: [this.auth.userID, [Validators.required]],
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
    public dialogRef: MatDialogRef<TimekeeperDisplayComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: TimekeeperService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService,
  ) { }
  ngOnInit(): void {
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();


    this.form.controls.timekeeperName.disable();
    this.dropdownSettings.disabled=true;
    this.auth.isuserdata();
    this.dropdownlist();
    if (this.data.pageflow != 'New') {
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
      if (this.data.pageflow != 'Copy') {
        this.form.controls.timekeeperCode.disable();
        this.form.controls.classId.disable();
        this.dropdownSettings.disabled=true;
        this.dropdownSettings1.disabled=true;
    this.form.controls.timekeeperName.disable();
    this.form.controls.userTypeId.disable();
      }
    }
    this.form.controls.classId.valueChanges.subscribe(x => { this.classdropdownlist(x) });
  }

  languageIdList: any[] = [];
  classIdList: any[] = [];
  userIdList: any[] = [];
  userTypeList: any[] = [];

  selectedItems: SelectItem[] = [];
  multiselecttimekeeperList: SelectItem[] = [];
  multitimekeeperList: SelectItem[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectuserIdList: SelectItem[] = [];
  multiuserIdList: SelectItem[] = [];
  
  dropdownSettings = {
    singleSelection: true,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };
  dropdownSettings1 = {
    singleSelection: true,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.languageId.url,
    this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.setup.userTypeId.url,
    ]).subscribe((results) => {
      this.languageIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.languageId.key);
      this.classIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.classId.key);
      this.userTypeList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.userTypeId.key);
      this.userTypeList.forEach((x: { key: string; value: string; }) => this.multiuserIdList.push({id: x.key, itemName:  x.value}))
      this.multiselectuserIdList = this.multiuserIdList;

    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
    this.form.controls.languageId.patchValue("EN");
  }

  classdropdownlist(x: any) {
    this.userIdList = [];
    if (x) {
      this.spin.show();
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.setup.userId.url,
      ]).subscribe((results) => {

        this.userIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.userId.key, { classId: x });
        this.userIdList.forEach((x: { key: string; value: string; }) => this.multitimekeeperList.push({id: x.key, itemName:  x.key}))
        this.multiselecttimekeeperList = this.multitimekeeperList;
      }, (err) => {
        this.toastr.error(err, "");
      });
      this.spin.hide();
    }
  }
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
      if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
        this.form.controls.timekeeperCode.patchValue([{id: res.timekeeperCode,itemName: res.timekeeperCode}]);
      }


      this.multiuserIdList.forEach(element => {
        if(element.id == res.userTypeId){
          console.log(this.form)
          this.form.controls.userTypeId.patchValue([element]);
        }
      });

      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.spin.hide();
      this.classdropdownlist(this.form.controls.classId.value)

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
    if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
      this.form.patchValue({timekeeperCode: this.selectedItems[0].id });
    }
    if (this.selectedItems2 != null && this.selectedItems2 != undefined && this.selectedItems2.length > 0) {
      this.form.patchValue({userTypeId: this.selectedItems2[0].id });
    }
    this.form.patchValue({ updatedby: this.auth.username });
    if (this.data.code && this.data.pageflow != 'Copy') {
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
        this.toastr.success(res.timekeeperCode + " updated successfully!", "Notification", {
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
  getTimekeeperdetails(user: any): void {

    this.spin.show();
    this.sub.add(this.service.getTimekeeper(user).subscribe(res => {
console.log(res)
   this.form.controls.timekeeperName.patchValue(res.fullName);

   this.multiuserIdList.forEach(element => {
    if(element.id == res.userTypeId){
      console.log(this.form)
      this.form.controls.userTypeId.patchValue([element]);
    }
  });

  //this.form.controls.userTypeId.patchValue([{id: res.userTypeId,itemName: res.userTypeId}]);
      // this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      // this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  onItemSelect(item: any) {

    if (item.id) this.getTimekeeperdetails(item.id);

  }

}