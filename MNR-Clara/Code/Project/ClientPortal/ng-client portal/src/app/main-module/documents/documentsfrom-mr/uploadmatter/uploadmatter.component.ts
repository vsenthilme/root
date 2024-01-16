import { HttpClient, HttpRequest, HttpResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscriber, Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { MatterService } from 'src/app/main-module/matters/matter.service';
import { DocumentTemplateService } from '../../document-template.service';

@Component({
  selector: 'app-uploadmatter',
  templateUrl: './uploadmatter.component.html',
  styleUrls: ['./uploadmatter.component.scss']
})
export class UploadmatterComponent implements OnInit {

  btntext = "Save"
  selectedMatter: string;
  files: File[] = [];
  myFormData!: FormData;
  documentUrl = '';
  sub = new Subscription();
  matterDetails: any = {};

  multiMatterList: any[] = [];

  constructor(
    public dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    public httpClient: HttpClient,
    private service: DocumentTemplateService,
    private matterService: MatterService,
    private auth: AuthService
  ) { }

  ngOnInit(): void {
console.log(this.auth.clientUserId)
    this.sub.add(this.matterService.filterMatter({ clientId: [this.auth.clientId] }).subscribe(res => {
      this.multiMatterList = res;
      this.spin.hide();
    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
  }

  getMatter() {
    this.sub.add(this.service.getMatterDetails(this.selectedMatter).subscribe(matter => {
      this.matterDetails = matter;
      console.log(this.matterDetails)
    }));
  }

  removeDuplicateMatter(list: any = []) {
    let dataObj: any = {};
    for (let i = 0; i < list.length; i++) {
      dataObj[list[i]['matterNumber']] = list[i];
    }
    list = new Array();
    for (let key in dataObj) {
      list.push(dataObj[key]);
    }
    return list;
  }

  upload() {
    this.spin.show();
    if (this.documentUrl == '') {
      this.toastr.error(
        "Please upload document to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      this.cs.notifyOther(true);
      return;
    }
    console.log(this.matterDetails)
    let documentObject: any = {};
    documentObject.classId = this.matterDetails.classId;
    documentObject.clientId = this.matterDetails.clientId;
    documentObject.caseCategoryId = this.matterDetails.caseCategoryId;
    documentObject.caseSubCategoryId = this.matterDetails.caseSubCategoryId;
    documentObject.matterNumber = this.selectedMatter;
    documentObject.documentUrl = this.documentUrl;
    documentObject.deletionIndicator = 0;
    documentObject.clientUserId = this.auth.clientUserId;
    documentObject.languageId = 'EN';
    documentObject.statusId = 23;

    this.sub.add(this.service.createClinetPortalmatterDocument(documentObject).subscribe(res => {
      this.toastr.success("CheckList document successfully!", "Notification", {
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

  locationfile = '';
  fileName = '';
  classId = '';

  uploadDocument() {
    // if (!this.selectedMatter) {
    //   this.toastr.error(
    //     "Please select Matter to upload",
    //     "Notification", {
    //     timeOut: 2000,
    //     progressBar: false,
    //   }
    //   );
    //   this.cs.notifyOther(true);
    //   return;
    // }

    if (this.files.length > 0) {
      this.spin.show();
      this.sub.add(this.service.getMatterDetails(this.selectedMatter).subscribe(async ress => {

        this.locationfile = 'clientportal/' + ress.clientId + '/' + ress.matterNumber;
        this.classId = ress.classId

        let url = '/mnr-management-service/matterdocument/' + ress.matterNumber + '/mailmerge/manual?' +
          'location=' + this.locationfile +
          // '&documentNumber=' + ress.documentNo +
          '&classId=' + ress.classId;
        const config = new HttpRequest('POST', `/doc-storage/upload?` + `location=${this.locationfile}&classId=${this.classId}`, this.myFormData, {
          reportProgress: true
        })
        this.httpClient.request(config)
          .subscribe(event => {
            if (event instanceof HttpResponse) {

              let body: any = event.body;
              this.documentUrl = body.file;
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
      }));
    }
  }
}
