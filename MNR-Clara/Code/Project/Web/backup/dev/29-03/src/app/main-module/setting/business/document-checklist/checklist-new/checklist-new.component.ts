import {
  HttpClient, HttpErrorResponse, HttpRequest, HttpResponse
} from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { DomSanitizer } from '@angular/platform-browser';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { documentTemplateService } from '../../document-template/document-template.service';

@Component({
  selector: 'app-checklist-new',
  templateUrl: './checklist-new.component.html',
  styleUrls: ['./checklist-new.component.scss']
})
export class ChecklistNewComponent implements OnInit {

  form = this.fb.group({
    createdBy: [this.auth.userID, [Validators.required]],
    classId: [2, [Validators.required]],
    languageId: [, [Validators.required]],
    documentStatus: ["ACTIVE", [Validators.required]],
    documentFileDescription: [,],
    updatedBy: [this.auth.userID, [Validators.required]],
    caseCategoryId: [, Validators.required],
    caseSubCategoryId: [, Validators.required],
    documents: [[],]
  });

  sub = new Subscription();

  caseCategoryIdList: any[] = [];
  classIdList: any[] = [];
  languageIdList: any[] = [];
  caseSubCategoryIdList: any[] = [];

  selectedCaseCategoryItems: any[] = [];
  multiSelectCaseCategoryList: any[] = [];
  multiCaseCategoryList: any[] = [];

  selectedSubCaseCategoryItems: any[] = [];
  multiSelectSubCaseCategoryList: any[] = [];
  multiSubCaseCategoryList: any[] = [];

  dropdownSettings = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };

  submitted = false;
  displayedColumns: string[] = ['sino', 'documentName', 'documentUpload', 'upload'];
  dataSource = new MatTableDataSource<any>();
  files: File[] = [];

  constructor(
    public dialog: MatDialog,
    private service: documentTemplateService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService, public HttpClient: HttpClient,
    public dialogRef: MatDialogRef<ChecklistNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private documentTemplateService: documentTemplateService,
    private sanitizer: DomSanitizer,
  ) { }

  ngOnInit(): void {
    this.dropdownlist();
  }

  getsubcaseCategoryId(code: string) {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.caseSubcategoryId.url,
    ]).subscribe((results: any) => {
      this.caseSubCategoryIdList = results[0];
      this.multiSubCaseCategoryList = [];
      this.caseSubCategoryIdList.forEach((element: any) => {
        if (element.caseCategoryId == +code) {
          this.multiSubCaseCategoryList.push({ id: element.caseSubcategoryId, itemName: element.subCategory })

        }
      })
      this.multiSelectSubCaseCategoryList = this.multiSubCaseCategoryList;
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();

  }



  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.classId.url,
      this.cas.dropdownlist.setup.caseCategoryId.url,
      this.cas.dropdownlist.setup.languageId.url,
      this.cas.dropdownlist.setup.caseSubcategoryId.url
    ]).subscribe((results) => {

      this.classIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.classId.key);
      this.caseCategoryIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.caseCategoryId.key);
      this.caseCategoryIdList.forEach((x: { key: string; value: string; }) => this.multiCaseCategoryList.push({ id: x.key, itemName: x.value }))
      this.multiSelectCaseCategoryList = this.multiCaseCategoryList;
      this.languageIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.languageId.key);
      this.caseSubCategoryIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.caseSubcategoryId.key);
      this.caseSubCategoryIdList.forEach((x: { key: string; value: string; }) => this.multiSubCaseCategoryList.push({ id: x.key, itemName: x.value }))
      this.multiSelectSubCaseCategoryList = this.multiSubCaseCategoryList;

      if (this.data.type == 'Edit') {
        let filterObj: any = {};
        filterObj.caseCategoryId = [this.data.documentData.caseCategoryId];
        filterObj.caseSubCategoryId = [this.data.documentData.caseSubCategoryId];
        filterObj.checkListNo = [this.data.documentData.checkListNo];
        filterObj.classId = [this.data.documentData.classId];
        this.sub.add(this.service.filterChecklistDocument(filterObj).subscribe(res => {
          let documentArray: any[] = [];
          if (res.length > 0) {
            res.forEach((doc: any) => {
              doc['files'] = [];
              documentArray.push(doc);
            })
          }
          for (let i = 0; i < this.classIdList.length; i++) {
            if (this.classIdList[i].key == this.data.documentData.calssId) {
              this.data.documentData.classId = this.classIdList[i].key;
              break;
            }
          }
          for (let i = 0; i < this.multiSelectCaseCategoryList.length; i++) {
            if (this.multiSelectCaseCategoryList[i].id == this.data.documentData.caseCategoryId) {
              this.data.documentData.caseCategoryId = [this.multiSelectCaseCategoryList[i]];
              break;
            }
          }
          for (let j = 0; j < this.multiSelectSubCaseCategoryList.length; j++) {
            if (this.multiSelectSubCaseCategoryList[j].id == this.data.documentData.caseSubCategoryId) {
              this.data.documentData.caseSubCategoryId = [this.multiSelectSubCaseCategoryList[j]];
              break;
            }
          }
          this.form.patchValue(this.data.documentData);
          this.form.controls.documents.patchValue(documentArray);
          this.dataSource.data = documentArray;
          this.spin.hide();
        }, err => {
          this.spin.hide();
        }));
      }

    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
    this.form.controls.languageId.patchValue("EN");
  }


  public errorHandling = (control: string, error: string = "required") => {
    if (control.includes('.')) {
      const controls = this.form.get(control);
      return controls ? controls.hasError(error) : false;
    }
    return this.form.controls[control].hasError(error);
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
  saveDocuments() {
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
    let documentArray: any[] = [];
    if (this.form.controls.documents.value.length > 0) {
      this.form.controls.documents.value.forEach((document: any, i: number) => {
        let documentObject: any = {};
        if (document.files.length > 0) {
          documentObject.classId = this.form.controls.classId.value;
          documentObject.caseCategoryId = this.form.controls.caseCategoryId.value[0].id;
          documentObject.caseSubCategoryId = this.form.controls.caseSubCategoryId.value[0].id;
          documentObject.documentName = document.documentName;
          documentObject.documentUrl = document.documentUrl;
          documentObject.languageId = this.form.controls.languageId.value;
          documentObject.statusId = this.form.controls.documentStatus.value;
          documentObject.sequenceNo = i + 1;
          if (this.data.type == 'Edit') {
            documentObject.checklistNo = this.data.documentData.checkListNo;
          }
          documentArray.push(documentObject);
        } else {
          document.classId = this.form.controls.classId.value;
          document.caseCategoryId = this.form.controls.caseCategoryId.value[0].id;
          document.caseSubCategoryId = this.form.controls.caseSubCategoryId.value[0].id;
          document.statusId = this.form.controls.documentStatus.value;
          document.languageId = this.form.controls.languageId.value;
          document.sequenceNo = i + 1;
          documentArray.push(document);
        }
      })
    }
    this.sub.add(this.service.createChecklistDocument(documentArray).subscribe(res => {
      console.log(res)
      this.toastr.success(res[0].checkListNo + " saved successfully!", "Notification", {
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

  onItemSelect1(item: any) {
    this.getsubcaseCategoryId(item.id);
  }

  deleteDocument(element, index) {
    if (element.files.length > 0 || (element.documentUrl.trim() != "" && element.documentUrl != null)) {
      this.sub.add(this.service.deleteChecklistDocument(element).subscribe(res => {
        console.log(res)
        this.toastr.success("Document deleted successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.dataSource.data.splice(index, 1);
        this.dataSource._updateChangeSubscription();
        this.form.controls.documents.value.splice(index, 1);
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    } else {
      this.dataSource.data.splice(index, 1);
      this.dataSource._updateChangeSubscription();
      this.form.controls.documents.value.splice(index, 1);
    }
  }

  uploadDocument(element: any, index) {
    if (element.documentName == null || element.documentName.trim() == '') {
      this.toastr.error(
        "Please fill the required DocumentName to upload",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
      this.cs.notifyOther(true);
      return;
    }
    if (element.files.length > 0) {
      this.spin.show();
      const config = new HttpRequest('POST', `/doc-storage/upload?location=document/checklist`, element.myFormData, {
        reportProgress: true
      })
      this.HttpClient.request(config)
        .subscribe(event => {
          if (event instanceof HttpResponse) {
            let body: any = event.body;
            element.documentUrl = body.file;
            console.log(this.form.controls.documents.value)
            if (this.data.type == 'New') {
              this.form.controls.documents.value[index] = element;
            }
            this.spin.hide();
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
  }
  addDocument() {
    this.dataSource.data.push({ documentName: '', documentUrl: '', files: [], myFormData: FormData });
    this.form.controls.documents.patchValue(this.dataSource.data);
    this.dataSource._updateChangeSubscription();
  }

  async download(element) {
    const blob = await this.documentTemplateService.getClientChecklistFile(element.documentUrl)
      .catch((err: HttpErrorResponse) => {
        this.cs.commonerror(err);
      });
    this.spin.hide();
    if (blob) {
      const blobOb = new Blob([blob], {
        type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      });
      let fileUrldownload = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blobOb));
      // http://remote.url.tld/path/to/document.doc&embedded=true
      let docurl = window.URL.createObjectURL(blob);

    }
  }
}
