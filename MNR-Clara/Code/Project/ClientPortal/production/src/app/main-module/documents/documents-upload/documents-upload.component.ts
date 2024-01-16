import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { UploadComponent } from "src/app/common-field/dialog_modules/upload/upload.component";
import { CommonService } from "src/app/common-service/common-service.service";
import { DocumentTemplateService } from "../document-template.service";
import { MatDialog } from "@angular/material/dialog";
import { HttpClient, HttpErrorResponse, HttpRequest, HttpResponse } from '@angular/common/http';
import { MatSort } from "@angular/material/sort";
import { MatPaginator } from "@angular/material/paginator";
import { DomSanitizer } from "@angular/platform-browser";
import { AuthService } from "src/app/core/core";
import { DownloadConfirmComponent } from "../download-confirm/download-confirm.component";
import { FormArray, FormBuilder } from "@angular/forms";

@Component({
  selector: 'app-documents-upload',
  templateUrl: './documents-upload.component.html',
  styleUrls: ['./documents-upload.component.scss']
})
export class DocumentsUploadComponent implements OnInit {
  matterNumber: string;
  clientId: string;
  matterHeaderId: string;
  checkListNo: string;
  sub = new Subscription();

  displayedColumns: string[] = ['updatedOn', 'partner', 'download', 'date', 'statusId','no', 'action'];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);
  disableSubmit: boolean;

  constructor(
    public activatedRoute: ActivatedRoute,
    private service: DocumentTemplateService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    public fb: FormBuilder,
    private sanitizer: DomSanitizer,
    public dialog: MatDialog,
    private cs: CommonService,
    public HttpClient: HttpClient,
    private router: Router,
    private auth: AuthService
  ) { }


  ngOnInit(): void {
    this.disableSubmit = true;
    this.activatedRoute.params.subscribe(routeParams => {
      this.matterNumber = routeParams.matterNumber;
      this.clientId = routeParams.clientId;
      this.checkListNo = routeParams.checkListNo;
      this.matterHeaderId = routeParams.matterHeaderId;
      console.log(this.matterNumber)
      console.log(this.clientId)
      console.log(this.checkListNo)
      this.getMatterDetails();
    });
  }
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
  getMatterDetails() {
    let caseCategoryIdList: any[] = [];
    let caseSubCategoryIdList: any[] = [];

    this.spin.show();

    this.sub.add(this.service.getMatterDetails(this.matterNumber).subscribe(res => {

      let docArray: any[] = [];
      let obj = { checkListNo: [this.checkListNo], matterNumber: [this.matterNumber], matterHeaderId: [this.matterHeaderId] }
      this.sub.add(this.service.searchMatterDocListHeader(obj).subscribe(docData => {
        if (docData[0].matterDocLists.length > 0) {
          docData[0].matterDocLists.forEach((element: any) => {
            // if (element.caseCategoryId == res.caseCategoryId && element.caseSubCategoryId == res.caseSubCategoryId) {
            element['files'] = [];
            element['myFormData'] = FormData;
            docArray.push(element);
            // }

           
          })
        }
        if (docArray.length > 0) {
          this.dataSource.data = docArray;
          
        console.log(this.dataSource.data);

            for(let i=0; i<this.dataSource.data.length; i++){
              if(this.dataSource.data[i].statusId != 60){
                this.disableSubmit = true;
                break;
              }
              this.disableSubmit = false;
            }
         
        }
        this.spin.hide();
      }));
      this.spin.hide();

    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }


  refreshTable(){
    let docArray: any[] = [];
    let obj = { checkListNo: [this.checkListNo], matterNumber: [this.matterNumber], matterHeaderId: [this.matterHeaderId] }
    this.sub.add(this.service.searchMatterDocListHeader(obj).subscribe(docData => {
      if (docData[0].matterDocLists.length > 0) {
        docData[0].matterDocLists.forEach((element: any) => {
          // if (element.caseCategoryId == res.caseCategoryId && element.caseSubCategoryId == res.caseSubCategoryId) {
          element['files'] = [];
          element['myFormData'] = FormData;
          docArray.push(element);
          // }

        })
      }
      if (docArray.length > 0) {
        this.dataSource.data = docArray;
        
      console.log(this.dataSource.data);
          for(let i=0; i<this.dataSource.data.length; i++){
            if(this.dataSource.data[i].statusId != 60){
              this.disableSubmit = true;
              break;
            }
            this.disableSubmit = false;
          }
      }
      this.spin.hide();
    }));
  }
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    if (this.isAllSelected()) {
      this.selection.clear();
      return;
    }

    this.selection.select(...this.dataSource.data);
  }

  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.partner + 1}`;
  }

  upload() {
    this.spin.show();
    // const dialogRef = this.dialog.open(UploadComponent, {
    //   disableClose: true,
    //   width: '30%',
    //   maxWidth: '80%',
    //   position: { top: '6.5%' },
    // });
    // dialogRef.afterClosed().subscribe(() => {
    // });
    let obj: any = {};
    obj.caseCategoryId = this.dataSource.data[0].caseCategoryId;
    obj.matterHeaderId = this.dataSource.data[0].matterHeaderId;
    obj.caseSubCategoryId = this.dataSource.data[0].caseSubCategoryId;
    obj.checkListNo = this.dataSource.data[0].checkListNo;
    obj.clientId = this.clientId;
    obj.matterNumber = this.matterNumber;

    this.sub.add(this.service.createClientPortalChecklistDocument(obj).subscribe(res => {
      this.toastr.success("Document sent successfully to M&R", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
      this.router.navigate(['/main/documents/documents']);
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  //download() {
  // const blob = await this.service.getfile1(this.classId,  window.encodeURIComponent(this.fileName), this.locationfile)
  // .catch((err: HttpErrorResponse) => {

  //   this.cs.commonerror(err);
  //   this.fileName = "";
  // });
  //}
  submitDone = false;
  uploadDocument(element: any, upload) {
    this.submitDone = true;
    if (element.files.length > 0) {
      this.spin.show();
      const config = new HttpRequest('POST', `/doc-storage/upload?location=clientportal/temp/${this.auth.clientId}/${this.matterNumber}/${element.checkListNo}`, element.myFormData, {
        reportProgress: true
      })
      this.HttpClient.request(config)
        .subscribe(event => {
          if (event instanceof HttpResponse) {

            let body: any = event.body;
           // element['documentUrl'] = body.file;
            console.log(element);
            this.spin.hide();
            let obj: any = {};
            let obj1: any = [{}];
            let objArray: any[] = [];
            obj.caseSubCategoryId = element.caseSubCategoryId;
            obj.checkListNo = element.checkListNo;
            obj.classId = element.classId;
            obj.clientId = element.clientId;
            obj.languageId = element.languageId;
            obj.matterNumber = element.matterNumber;
            obj.statusId = upload == "upload" ? 60 : 63;
            let matterDocLine: any[] = [];
            matterDocLine.push({
              caseCategoryId: element.caseCategoryId,
              caseSubCategoryId: element.caseSubCategoryId,
              checkListNo: element.checkListNo,
              classId: element.classId,
              clientId: element.clientId,
              documentName: element.documentName,
              documentUrl: body.file,
              languageId: element.languageId,
              matterNumber: element.matterNumber,
              matterText: element.matterText,
              sequenceNumber: element.sequenceNumber,
              statusId: upload == "upload" ? 60 : 63
            });
            obj.matterDocLists = matterDocLine;
         
          console.log(obj)
         //  this.service.Update(obj, this.matterNumber, element.checkListNo, element.classId, element.clientId, element.languageId).subscribe(updateMatterDoc => {
            this.service.UpdateNew(obj, element.matterHeaderId).subscribe(updateMatterDoc => {
                this.refreshTable();
            })
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
  documentNo!: string;
  locationfile = '';
  fileName = '';
  classId = '';
  docurl: any;
  fileUrldownload: any;

  download(element: any) {
    console.log(element)
    this.sub.add(this.service.searchMatterDocListHeader({ checkListNo: [this.checkListNo] }).subscribe(async ress => {
      this.spin.hide();
      this.documentNo = ress.documentNo;
      //this.locationfile = `document/checklist`;
      this.locationfile = 'clientportal/temp/' + this.auth.clientId + '/' + this.matterNumber + '/' + this.checkListNo;
      this.spin.show();
      const blob = await this.service.getClientCheckListDocument(element.documentUrl, this.locationfile)
        .catch((err: HttpErrorResponse) => {
          this.cs.commonerror(err);
        });
      this.spin.hide();
      if (blob) {
        const blobOb = new Blob([blob], {
          type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        });
        this.fileUrldownload = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blobOb));
        this.docurl = window.URL.createObjectURL(blob);
      }
      this.downloadConfirm(element)
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }


  downloadConfirm(element): void {
    // if (this.selection.selected.length === 0) {
    //   this.toastr.warning("Kindly select any Row", "Notification", {
    //     timeOut: 2000,
    //     progressBar: false,
    //   });
    //   return;
    // }
    const dialogRef = this.dialog.open(DownloadConfirmComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
      position: { top: '9%', },
      data: { url: this.fileUrldownload, fileName: element.documentUrl}
    });

    dialogRef.afterClosed().subscribe(result => {
     // this.search();
    });
  }
}

