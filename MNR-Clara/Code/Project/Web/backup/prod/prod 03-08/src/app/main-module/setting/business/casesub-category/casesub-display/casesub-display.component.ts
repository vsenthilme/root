
import { Component, OnInit, Inject, OnDestroy, ViewChild } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DialogExampleComponent, DialogData } from "src/app/common-field/dialog-example/dialog-example.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { CaseSubCategoryElement, CaseSubCategoryService } from "../case-sub-category.service";


interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-casesub-display',
  templateUrl: './casesub-display.component.html',
  styleUrls: ['./casesub-display.component.scss']
})
export class CasesubDisplayComponent implements OnInit {
  screenid: 1024 | undefined;
  email = new FormControl('', [Validators.required, Validators.email]);
  input: any;
  casesublist: any;

  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }

    return this.email.hasError('email') ? 'Not a valid email' : '';
  }


  sub = new Subscription();

  form = this.fb.group({

    createdBy: [this.auth.userID, [Validators.required]],
    categoryDescription: [,],
    classId: [, [Validators.required]],
    languageId: [, [Validators.required]],
    categoryStatus: ["Active", [Validators.required]],
    caseCategory: [,],
    updatedBy: [this.auth.userID, [Validators.required]],
    createdOn: [],
    updatedOn: [],
    deletionIndicator: [0],
    subCategory:[, Validators.required],
    subCategoryDescription: [,],
    caseCategoryId: [, ],
    caseCategoryIdFE: [, Validators.required],
    caseSubcategoryId: [],
    taxType: [,]
  });


  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
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
  formgr: CaseSubCategoryElement | undefined;

  panelOpenState = false;
  constructor(
    public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: CaseSubCategoryService,
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
    this.auth.isuserdata();
    this.dropdownlist();
    if (this.data.pageflow != 'New') {
      if (this.data.pageflow == 'Display'){

   this.dropdownSettings.disabled=true;
        this.form.disable();
      }
      this.fill();
      this.form.controls.caseSubcategoryId.disable();
      this.form.controls.classId.disable();
      this.form.controls.caseCategoryId.disable();
    }
  }


  caseCategoryIdList: any[] = [];
  classIdList: any[] = [];
  languageIdList: any[] = [];


  selectedItems: SelectItem[] = [];
  multiselectcasesubList: SelectItem[] = [];
  multicasesubList: SelectItem[] = [];

  
  dropdownSettings = {
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
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.setup.caseCategoryId.url
    ]).subscribe((results) => {

      this.classIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.classId.key);
      this.caseCategoryIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.caseCategoryId.key);
      this.caseCategoryIdList.forEach((x: { key: string; value: string; }) => this.multicasesubList.push({id: x.key, itemName:  x.value}))
      this.multiselectcasesubList = this.multicasesubList;
    }, (err) => {
      this.toastr.error(err, "");
    });

    this.spin.hide();
    this.form.controls.languageId.patchValue("EN");
  }

  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code.caseSubcategoryId, this.data.code.classId, this.data.code.languageId, this.data.code.caseCategoryId).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
      this.multicasesubList.forEach(element => {
        if(element.id == res.caseCategoryId){
          console.log(this.form)
          this.form.controls.caseCategoryIdFE.patchValue([element]);
        }
      });

     
      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
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
    if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
      this.form.patchValue({caseCategoryId: this.selectedItems[0].id });
    }
    this.form.patchValue({ updatedby: this.auth.username });
    if (this.data.code) {
    
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code.caseSubcategoryId,
    
        this.form.controls.classId.value, this.form.controls.languageId.value, this.form.controls.caseCategoryId.value

      ).subscribe(res => {
        this.toastr.success(res.caseSubcategoryId + " updated successfully!","Notification",{
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
        this.toastr.success(res.caseSubcategoryId + " saved successfully!", "Notification", {
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

  onNoClick(): void {
    this.dialogRef.close();
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }


  onItemSelect(item:any){
    console.log(item);
    console.log(this.selectedItems);
}
OnItemDeSelect(item:any){
    console.log(item);
    console.log(this.selectedItems);
}
onSelectAll(items: any){
    console.log(items);
}
onDeSelectAll(items: any){
    console.log(items);
}
}




