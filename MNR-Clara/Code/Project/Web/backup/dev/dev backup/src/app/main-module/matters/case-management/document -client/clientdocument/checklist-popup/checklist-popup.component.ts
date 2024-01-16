import { SelectionModel } from '@angular/cdk/collections';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Validators } from 'ngx-editor';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { documentTemplateService } from 'src/app/main-module/setting/business/document-template/document-template.service';
import { GeneralMatterService } from '../../../General/general-matter.service';

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-checklist-popup',
  templateUrl: './checklist-popup.component.html',
  styleUrls: ['./checklist-popup.component.scss']
})
export class ChecklistPopupComponent implements OnInit {

  code: any = this.cs.decrypt(sessionStorage.getItem('matter')).code;

  sub = new Subscription();

  form = this.fb.group({

    checkListNo: [{ value: '', disabled: true }],
    checkListNoFE: [{ value: '', disabled: false }],
    caseCategoryId: [{ value: '', disabled: true }],
    caseSubCategoryId: [{ value: '', disabled: true }],
    caseCategoryName: [{ value: '', disabled: true }],
    caseSubCategoryName: [{ value: '', disabled: true }],
    matterNumber: [{ value: '', disabled: true }],
    matterDescription: [{ value: '', disabled: true }],
    classId: [{ value: '', disabled: true }],
    clientId: [{ value: '', disabled: true }],
    statusId: [{ value: '', disabled: true }],
    languageId:['EN']
  });
  matter: string;

  constructor(
    public dialogRef: MatDialogRef<ChecklistPopupComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private cs: CommonService,
    private service: GeneralMatterService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    public fb: FormBuilder,
    private documentTemplateService: documentTemplateService,
    private cas: CommonApiService,
  ) { }

  ngOnInit(): void {
    if (this.data.matterNumber) {
      // this.matter = ' Matter - (' + this.data.matter + ') - ';
      this.matter = '' + this.data.matterNumber + ' / ' + this.data.matterdesc + ' - ';
      this.form.controls.matterNumber.patchValue(this.data.matter);
      // this.form.controls.matterNumber.disable();

    }
    this.getMatterDetails();
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
  displayedColumns: string[] = ['sino', 'documentName', 'checkListTemplate']; ///YETTOBEPROD
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);

  
  selectedItems: SelectItem[] = [];
  multiselectchecklistnoList: SelectItem[] = [];
  multichecklistnoList: SelectItem[] = [];

  
  dropdownSettings = {
    singleSelection: true,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };




  getMatterDetails() {
    let caseCategoryIdList: any[] = [];
    let caseSubCategoryIdList: any[] = [];
    let checklistnoList: any[] = [];
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.caseCategoryId.url,
      this.cas.dropdownlist.setup.caseSubcategoryId.url,
      this.cas.dropdownlist.setup.checklist.url
    ]).subscribe((results) => {
      caseCategoryIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.caseCategoryId.key);
      caseSubCategoryIdList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.caseSubcategoryId.key);
      checklistnoList = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.setup.checklist.key);
      checklistnoList.forEach((x: { key: string; value: string; }) => this.multichecklistnoList.push({id: x.key, itemName:  x.value}))
      this.multiselectchecklistnoList = this.multichecklistnoList;

      this.spin.hide();
      this.sub.add(this.service.Get(this.code).subscribe(res => {
        this.form.patchValue(res, { emitEvent: false });
        
     
        let docArray: any[] = [];
        this.sub.add(this.documentTemplateService.getChecklistDocument().subscribe(docData => {
          if (docData.length > 0) {
            docData.forEach((element: any) => {
              if (element.caseCategoryId == this.form.controls.caseCategoryId.value && element.caseSubCategoryId == this.form.controls.caseSubCategoryId.value) {
                docArray.push(element);
              }
            })
          }
          if (docArray.length > 0) {
            this.form.controls.checkListNo.setValue(docArray[0].checkListNo);
         //   this.form.controls.checkListNoFE.patchValue([this.form.controls.checkListNo.value]);
            this.multichecklistnoList.forEach(element => {
              if(element.id == docArray[0].checkListNo){
                this.form.controls.checkListNo.patchValue(element.id);
              }
            });
            docArray.forEach(x => {
              x['matterNumber']= this.form.controls.matterNumber.value;
              x['caseSubCategoryId']= this.form.controls.caseSubCategoryId.value;
              x['caseCategoryId']= this.form.controls.caseCategoryId.value;
              x['checkListNo']= this.form.controls.checkListNo.value;
              x['clientId']= this.form.controls.clientId.value;
              x['statusId']= 22;
              x['languageId']= 'EN';
              x['sequenceNumber']= x.sequenceNo;
            })
            this.dataSource.data = docArray;
          }
          this.spin.hide();
          this.form.controls.caseCategoryName.setValue(caseCategoryIdList.find(y => y.key == this.form.controls.caseCategoryId.value)?.value);
          this.form.controls.caseSubCategoryName.setValue(caseSubCategoryIdList.find(y => y.key == this.form.controls.caseSubCategoryId.value)?.value);
        }));
        this.spin.hide();

      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
  }

  saveMatterChecklist(){

    // if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
    //   this.form.patchValue({checkListNo: this.selectedItems[0].id });
    // }

console.log(this.dataSource.data)
    this.sub.add(this.service.saveMatterCheckList(this.dataSource.data).subscribe(res => {
      this.toastr.success(this.form.controls.checkListNo.value + " saved successfully!", "Notification", {
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
}
