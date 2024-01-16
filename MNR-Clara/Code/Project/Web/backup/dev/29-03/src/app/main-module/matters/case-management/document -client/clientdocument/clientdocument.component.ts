import { SelectionModel } from "@angular/cdk/collections";
import {
  HttpClient, HttpRequest,
  HttpResponse, HttpEvent, HttpErrorResponse
} from '@angular/common/http'
import { Component, OnInit } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";

import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { ShowStringPopupComponent } from "src/app/common-field/dialog_modules/show-string-popup/show-string-popup.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { PaymentLinkComponent } from "src/app/main-module/accounts/quotation/quotations-list/payment-link/payment-link.component";
import { documentTemplateService } from "src/app/main-module/setting/business/document-template/document-template.service";
import { GeneralMatterService } from "../../General/general-matter.service";
import { SharedPopupComponent } from "../document-clientportal/shared-popup/shared-popup.component";
import { ChecklistPopupComponent } from "./checklist-popup/checklist-popup.component";
import { DocumentChecklistComponent } from "./document-checklist/document-checklist.component";

@Component({
  selector: 'app-clientdocument',
  templateUrl: './clientdocument.component.html',
  styleUrls: ['./clientdocument.component.scss']
})
export class ClientdocumentComponent implements OnInit {

  screenid = 1157;

  myModel = true;

  code: any = this.cs.decrypt(sessionStorage.getItem('matter')).code;
  sub = new Subscription;
  matterNumber: any;
  matterdesc: any;

  action() {
    this.myModel = true;
    this.router.navigate(['/main/matters/case-management/clientdocument']);
  }


  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  toggleFloat() {

    this.isShowDiv = !this.isShowDiv;
    this.toggle = !this.toggle;

    if (this.icon === 'expand_more') {
      this.icon = 'chevron_left';
    } else {
      this.icon = 'expand_more'
    }
    this.showFloatingButtons = !this.showFloatingButtons;
    console.log('show:' + this.showFloatingButtons);
  }
  showFiller = false;
  animal: string | undefined;
  id: string | undefined;
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  constructor(public dialog: MatDialog, private auth: AuthService, private router: Router,
    private cs: CommonService,
    private service: GeneralMatterService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    public HttpClient: HttpClient,
    public fb: FormBuilder,
    private excel: ExcelService,
    private documentTemplateService: documentTemplateService,
    private cas: CommonApiService,) { }
  new(): void {

    const dialogRef = this.dialog.open(ChecklistPopupComponent, {
      disableClose: true,
      width: '70%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { matterNumber: this.matterNumber, matterdesc: this.matterdesc }
    });

    dialogRef.afterClosed().subscribe(result => {
     // window.location.reload();
this.getAllMatterCheckListNew();
    });
  }
  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    this.matterNumber = this.cs.decrypt(sessionStorage.getItem('matter')).code;
    this.matterdesc = this.cs.decrypt(sessionStorage.getItem('matter')).code1;
    this.getAllMatterCheckListNew();
  }

  displayedColumns: string[] = ['select', 'documenttype', 'expirationdate', 'eligibility', 'remainder', 'remainderdate','documents'];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);

  /** Whether the number of selected elements matches the total number of rows. */
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.documenttype + 1}`;
  }
  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  reset() {
    window.location.reload();
  }
  statusList: any[] = [];
  getAllMatterCheckList() {

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.statusList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.getAllMatterDocList(this.code).subscribe(listData => {
        listData.forEach(data => {
          data['statusIdName'] = this.statusList.find(y => y.key == data.statusId)?.value;
        });
        listData =  listData.filter(x => x.deletionIndicator != 1)
        this.dataSource.data = listData;
        this.dataSource._updateChangeSubscription();
        this.spin.hide();
      }, (err: any) => {
        if (err.error) {
          if (err.error.error.includes("No value present")) {
            this.spin.hide();
            return;
          }
        }
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.toastr.error(err, "");
    });
  }

  getAllMatterCheckListNew() {

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.statusList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.findMatterDocListHeader(this.code).subscribe(listData => {
        listData.forEach(data => {
          data['statusIdName'] = this.statusList.find(y => y.key == data.statusId)?.value;
        });
        listData =  listData.filter(x => x.deletionIndicator != 1)
        this.dataSource.data = listData;
        this.dataSource._updateChangeSubscription();
        this.spin.hide();
      }, (err: any) => {
        if (err.error) {
          if (err.error.error.includes("No value present")) {
            this.spin.hide();
            return;
          }
        }
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.toastr.error(err, "");
    });
  }

  documentNo!: string;
  files: File[] = [];
  postUrl = '/doc-storage/upload';
  myFormData!: FormData;
  locationfile = '';
  isupload = true;
  classId = '';

  uploadFiles() {

    this.sub.add(this.service.Get(this.code).subscribe(ress => {

      this.spin.hide();
      console.log(ress)
      console.log(ress.clientId)
      // res.agreementUrl = '/A001-Agreement-Document_processed_v5.docx';
      // res.agreementUrl = "001-agreement-document_signed.pdf";
      // this.fileName = res.agreementUrl
      this.documentNo = ress.documentNo;
      this.classId = ress.classId
      //      this.locationfile = 'document/' + ress.documentNo + '_' + ress.matterNumber;//+ '_' + code;
      //      this.locationfile = 'document/' + ress.clientId + '/' + ress.matterNumber + '/' + "temp";

      this.locationfile = 'clientportal/' + ress.clientId + '/' + ress.matterNumber;


      if (this.files.length > 0) {
        this.spin.show();
        //  const config = new HttpRequest('POST', `/doc-storage/upload?` + 'location=' + this.locationfile,  this.myFormData, {
        const config = new HttpRequest('POST', `/doc-storage/upload?` + `location=${this.locationfile}&classId=${this.classId}`, this.myFormData, {
          reportProgress: true
        })
        this.HttpClient.request(config)
          .subscribe(event => {
            // this.httpEvent = event


            if (event instanceof HttpResponse) {
              this.spin.hide();
              let body: any = event.body;
              this.toastr.success("Document uploaded successfully.", "Notification", {
                timeOut: 2000,
                progressBar: false,
              });
              this.isupload = false;

            }
          },
            error => {
              this.spin.hide();

              this.cs.commonerror(error);

            })
      }
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  getLocationURL(): void {

    const dialogRef = this.dialog.open(SharedPopupComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      // data: { link: data.referenceField10, type: "CLIENT_DOCUMENT", title: "Share Folder Location" }
    });

    dialogRef.afterClosed().subscribe(result => {

      // if (result) {
      //   this.spin.show();
      //   this.sub.add(this.service.Update({ referenceField10: result, statusId: 52 }, data.quotationNo,data.quotationRevisionNo).subscribe((res: any) => {
      //     this.spin.hide();
      //     data.referenceField10 = result;

      //     this.toastr.success(data.quotationNo + " updated successfully!");

      //   }, err => {
      //     this.cs.commonerror(err);
      //     this.spin.hide();
      //   }));
      // }

    });
  }

  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Checklist No": x.checkListNo,
        "Status": x.statusIdName,
        "Sent By ": x.createdBy,
        'Sent On': this.cs.dateapi(x.createdOn),
        'Received Date': this.cs.dateapi(x.updatedOn),
      });

    })
    this.excel.exportAsExcel(res, "Client Portal Checklist");
  }

  searhform = this.fb.group({
    documentNo: [],
    ereceivedOn: [],
    esentOn: [],
    sentBy: [],
    sreceivedOn: [],
    statusId: [],
    ssentOn: [],
  });

  search() {


    this.searhform.controls.endCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.endCreatedOn.value));
    this.searhform.controls.startCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.startCreatedOn.value));


    this.spin.show();


    this.spin.hide();

    this.spin.show();
    this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {

      res.forEach((x) => {
        // x.noteTypeId = this.noteTypeIdlist.find(y => y.key == x.noteTypeId)?.value;
        //  x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
      })
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));

    this.spin.hide();
  }

  delete() {
    if (this.selection.selected.length > 0) {
      console.log(this.selection.selected)
      this.spin.show();
      this.sub.add(this.service.deleteMAtterDocList(this.selection.selected[0].matterHeaderId).subscribe(res => {
        this.getAllMatterCheckListNew();
        this.toastr.success("Deleted successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    } else {
      this.toastr.error("Please choose a document to continue", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
    }
  }


  getDocument(element){
    const dialogRef = this.dialog.open(DocumentChecklistComponent, {
      disableClose: true,
      width: '60%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { document: element.matterDocLists, matterNumber: this.matterNumber, matterdesc: this.matterdesc, checklistNo: element.checkListNo }
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }

}
