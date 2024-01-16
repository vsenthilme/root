import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import {
  HttpClient, HttpRequest,
  HttpResponse, HttpEvent, HttpErrorResponse
} from '@angular/common/http'
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { MatterDocumetService } from "../../matter-document/matter-documet.service";
import { DocumentPopupComponent } from "./document-popup/document-popup.component";
import { ShowStringPopupComponent } from "src/app/common-field/dialog_modules/show-string-popup/show-string-popup.component";
import { SharedPopupComponent } from "./shared-popup/shared-popup.component";
import { ClientDocumentService } from "src/app/main-module/client/client-document/client-document.service";   ///YETTOBEPROD

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-document-clientportal',
  templateUrl: './document-clientportal.component.html',
  styleUrls: ['./document-clientportal.component.scss']
})
export class DocumentClientportalComponent implements OnInit {

  code: any = this.cs.decrypt(sessionStorage.getItem('matter')).code;

  myModel = true;
  screenid = 1168;
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;

  showFiller = false;
  animal: string | undefined;
  id: string | undefined;

  matterNumber = '';
  sub = new Subscription();
  RA: any = {};

  displayedColumns: string[] = ['select', 'action', 'documenttype', 'approvaldate','eligibility', 'attachment', 'remainder', 'uploadedBy','remainderdate', 'rec',];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);

  statusList: any[] = [];
  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination
  matterdesc: any;

  constructor(public dialog: MatDialog,
    private service: MatterDocumetService, private router: Router,
    public toastr: ToastrService, private route: ActivatedRoute,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    public HttpClient: HttpClient,
    
    private excel: ExcelService,
    
    private clientUser: ClientDocumentService,
    private fb: FormBuilder,
    private auth: AuthService) { }

  new(): void {
    const dialogRef = this.dialog.open(DocumentPopupComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { matterNumber: this.matterNumber, matterdesc: this.matterdesc }
    });
    dialogRef.afterClosed().subscribe(result => {
      this.getAllListData();
      // window.location.reload();
    });
  }

  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
    this.matterNumber = this.cs.decrypt(sessionStorage.getItem('matter')).code;
    this.matterdesc = this.cs.decrypt(sessionStorage.getItem('matter')).code1;
    this.getAllListData();
    //this.findClientId();
  }

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
    this.searhform.reset();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  action() {
    this.myModel = true;
    this.router.navigate(['/main/matters/case-management/documenttab']);
  }
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

  
  selectedItems3: SelectItem[] = [];
  multidocumentnoList: any[] = [];
  multiselectdocumentnoList: any[] = [];
  dropdownSettings = {
    singleSelection: false,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  selectedItems2: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];

  selectedItems5: SelectItem[] = [];
  multiselectsentbyList: any[] = [];
  multisentbyList: any[] = [];


  findClientId(){
    this.sub.add(this.clientUser.searchClientList(this.code).subscribe(clientUserRes => {
      
    }))
  }

  getAllListData() {

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.noteTypeId.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.statusList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: any[]) => {



        res.forEach((x) => {
          x['statusIdName'] = this.statusList.find(y => y.key == x.statusId)?.value;
        })
        
        let result = res.filter((x: any) => x.referenceField5 == "Client Portal" || x.referenceField2 == 'CLIENTPORTAL');

        
        result.forEach((x: { documentNo: string; referenceField8: string;}) => this.multidocumentnoList.push({value: x.documentNo, label:  x.documentNo + '-' + x.referenceField8}))
        this.multiselectdocumentnoList = this.multidocumentnoList;
          this.multiselectdocumentnoList = this.cas.removeDuplicatesFromArrayNew(this.multiselectdocumentnoList);

                  
        result.forEach((x: { createdBy: string;}) => this.multisentbyList.push({value: x.createdBy, label: x.createdBy}))
        this.multiselectsentbyList = this.multisentbyList;
          this.multiselectsentbyList = this.cas.removeDuplicatesFromArrayNew(this.multiselectsentbyList);


          this.statusList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [19, 20].includes(s.key));
          this.statusList.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.value}))
        this.multiselectstatusList = this.multistatusList;

        this.dataSource.data = result.filter(x => x.matterNumber == this.matterNumber);
        this.selection = new SelectionModel<any>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
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


  open_link(data: any): void {
    this.router.navigate(['main/matters/case-management/document_template/' + this.cs.encrypt({ code: data.matterDocumentId, pageflow: 'view' })]);

  }


  open_Upload(data: any): void {
    this.router.navigate(['/main/matters/case-management/document_template/' + this.cs.encrypt({ code: data.matterDocumentId, pageflow: 'upload' })]);

  }

  documentNo!: string;
  files: File[] = [];
  postUrl = '/doc-storage/upload';
  myFormData!: FormData;
  locationfile = '';
  isupload = true;
  classId = '';
  documentUrl= '';
  uploadFiles() {

    this.sub.add(this.service.get_matterdetails(this.code).subscribe(ress => {

      this.spin.hide();
      console.log(ress)
      console.log(ress.clientId)
      // res.agreementUrl = '/A001-Agreement-Document_processed_v5.docx';
      // res.agreementUrl = "001-agreement-document_signed.pdf";
      // this.fileName = res.agreementUrl
      this.documentNo = ress.documentNo;
      this.classId = ress.classId
      //      this.locationfile = 'document/' + ress.documentNo + '_' + ress.matterNumber;//+ '_' + code;
      this.locationfile = 'clientportal/' + ress.clientId + '/' + ress.matterNumber;



      if (this.files.length > 0) {
        this.spin.show();
        //  const config = new HttpRequest('POST', `/doc-storage/upload?` + 'location=' + this.locationfile,  this.myFormData, {
        const config = new HttpRequest('POST', `/doc-storage/upload?` + `location=${this.locationfile}&classId=${this.classId}`, this.myFormData, {
          reportProgress: true
        })
        this.HttpClient.request(config)
          .subscribe(event => {
            // this.httpEvent = eventnpom


            if (event instanceof HttpResponse) {
              this.spin.hide();

              let body: any = event.body;
              this.documentUrl = body.file;
              this.toastr.success("Document uploaded successfully.", "Notification", {
                timeOut: 2000,
                progressBar: false,
              });
              this.clientUser.searchClientList({clientId: [ress.clientId]}).subscribe(clientUserRes => {
              sessionStorage.setItem("clientUserId", JSON.stringify(clientUserRes[0]))
                this.submitUploadedDocuments(ress)
              })
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


  submitUploadedDocuments(ress){
    console.log(this.auth.clientUserId)
    let documentObject: any = {};
    documentObject.classId = ress.classId;
    documentObject.clientId = ress.clientId;
    documentObject.caseCategoryId = ress.caseCategoryId;
    documentObject.caseSubCategoryId = ress.caseSubCategoryId;
    documentObject.matterNumber = ress.matterNumber;
    documentObject.documentUrl = this.documentUrl;
    documentObject.deletionIndicator = 0;
    documentObject.clientUserId = this.auth.clientUserId;
    documentObject.languageId = 'EN';
    documentObject.statusId = 22;

    this.sub.add(this.service.createClinetPortalmatterDocument(documentObject).subscribe(res => {
      this.toastr.success("CheckList document successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.getAllListData();
      this.spin.hide();
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
      //  data: { link: data.referenceField1, type: "CLIENT_DOCUMENT", title: "Share Folder Location" }
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
        "Document No": x.documentNo,
        'Document Name': x.documentUrl,
        "Status": x.statusIdName,
        "Sent By ": x.createdBy,
        'Sent On': this.cs.dateapi(x.createdOn),
        'Received Date': this.cs.dateapi(x.receivedOn),
      });

    })
    this.excel.exportAsExcel(res, "Client Portal Documents");
  }

  searhform = this.fb.group({
    documentNo: [],
    documentNoFE: [],
    ereceivedOn: [],
    esentOn: [],
    sentBy: [],
    sentByFE: [],
    sreceivedOn: [],
    statusId: [],
    statusIdFE: [],
    ssentOn: [],
  });

  search() {
    // if (this.selectedItems2 && this.selectedItems2.length > 0){
    //   let multistatusList: any[]=[]
    //   this.selectedItems2.forEach((a: any)=> multistatusList.push(a.id))
    //   this.searhform.patchValue({statusId: multistatusList });
    // }

    // if (this.selectedItems5 && this.selectedItems5.length > 0){
    //   let multisentbyList: any[]=[]
    //   this.selectedItems5.forEach((a: any)=> multisentbyList.push(a.id))
    //   this.searhform.patchValue({sentBy: multisentbyList });
    // }

    // if (this.selectedItems3 && this.selectedItems3.length > 0){
    //   let multidocumentnoList: any[]=[]
    //   this.selectedItems3.forEach((a: any)=> multidocumentnoList.push(a.id))
    //   this.searhform.patchValue({documentNo: multidocumentnoList });
    // }

    this.searhform.controls.esentOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.esentOn.value));
    this.searhform.controls.ssentOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.ssentOn.value));

    this.searhform.controls.sreceivedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.sreceivedOn.value));
    this.searhform.controls.ereceivedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.ereceivedOn.value));


    this.spin.show();


      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {

        res.forEach((x) => {
          x['statusIdName'] = this.statusList.find(y => y.key == x.statusId)?.value;
        })
        let result = res.filter((x: any) => x.referenceField5 == "Client Portal" || x.referenceField2 == 'CLIENTPORTAL');
        this.dataSource.data = result.filter(x => x.matterNumber == this.matterNumber);
        this.selection = new SelectionModel<any>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));

    this.spin.hide();

  }

}
