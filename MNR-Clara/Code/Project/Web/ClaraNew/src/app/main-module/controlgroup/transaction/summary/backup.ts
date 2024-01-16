import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DeleteComponent } from 'src/app/common-field/dialog_modules/delete/delete.component';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { OwnershipService } from '../ownership/ownership.service';
import { EmailRequestComponent } from './email-request/email-request.component';
import { ApprovalComponent } from '../approval/approval.component';
import { HttpErrorResponse } from '@angular/common/http';
import { DomSanitizer } from '@angular/platform-browser';
import { UploadFilesComponent } from '../ownership/ownership-new/upload-files/upload-files.component';

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.scss'],
})
export class SummaryComponent implements OnInit {
  screenid = 1021;
  public icon = 'expand_more';
  sub = new Subscription();
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  ELEMENT_DATA: any[] = [];
  constructor(
    public dialog: MatDialog,
    private service: OwnershipService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private router: Router,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService,
    private pdf: ApprovalComponent,
    private sanitizer: DomSanitizer,
  ) { }
  toggleFloat() {
    this.isShowDiv = !this.isShowDiv;
    this.toggle = !this.toggle;

    if (this.icon === 'expand_more') {
      this.icon = 'chevron_left';
    } else {
      this.icon = 'expand_more';
    }
    this.showFloatingButtons = !this.showFloatingButtons;
  }
  showFiller = false;
  animal: string | undefined;
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  search() {
    this.spin.show();
    this.searhform.controls.startCreatedOn.patchValue(
      this.cs.dateNewFormat1(this.searhform.controls.startCreatedOnFE.value)
    );
    this.searhform.controls.endCreatedOn.patchValue(
      this.cs.dateNewFormat1(this.searhform.controls.endCreatedOnFE.value)
    );
    this.sub.add(
      this.service.search(this.searhform.getRawValue()).subscribe(
        (res: any[]) => {
          console.log(res);
          this.dataSource = new MatTableDataSource<any>(res);
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
          this.spin.hide();
        },
        (err) => {
          this.cs.commonerrorNew(err);
          this.spin.hide();
        }
      )
    );
  }
  displayedColumns: string[] = [
    'select',
    'action',
    'doc',
    'document',
    'coOwnwer',
    // 'languageId',
    // 'companyId',
    'requestId',
    'storeId',
    'createdBy',
    'createdOn',
    'statusId',
  ];
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.requestId + 1
      }`;
  }
  RA: any = {};
  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllListData();
   
  }
  deleteDialog() {
    if (this.selection.selected.length === 0) {
      this.toastr.error('Kindly select any Row', 'Notification', {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: {
        top: '6.5%',
      },
      data: this.selection.selected[0].caseCategoryId,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.deleterecord(
          this.selection.selected[0].requestId,
          this.selection.selected[0].languageId,
          this.selection.selected[0].companyId
        );
      }
    });
  }
  deleterecord(id: any, languageId: any, companyId: any) {
    this.spin.show();
    this.sub.add(
      this.service.Delete(id, languageId, companyId).subscribe(
        (res) => {
          this.toastr.success(id + ' deleted successfully!', 'Notification', {
            timeOut: 2000,
            progressBar: false,
          });
          this.spin.hide(); //this.getAllListData();
          window.location.reload();
        },
        (err) => {
          this.cs.commonerror(err);
          this.spin.hide();
        }
      )
    );
  }

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }
  }

  @ViewChild(MatSort, {
    static: true,
  })
  sort: MatSort;
  @ViewChild(MatPaginator, {
    static: true,
  })
  paginator: MatPaginator; // Pagination

  multiselectyseridList: any[] = [];
  multiyseridList: any[] = [];
  multicasecatList: any[] = [];

  multiselectclassList: any[] = [];
  multilanguageList: any[] = [];
  multicompanyList: any[] = [];
  multigrouptypeList: any[] = [];
  multigroupidList: any[] = [];
  multirequestList: any[] = [];
  multistoreList: any[] = [];
  getall(excel: boolean = false) {
    let obj: any = {};
    this.spin.show();
    this.sub.add(
      this.service.search(obj).subscribe(
        (res: any[]) => {
          console.log(res);
          this.dataSource = new MatTableDataSource<any>(res);
          this.spin.hide();
          res.forEach((x: { languageId: string }) =>
            this.multilanguageList.push({
              value: x.languageId,
              label: x.languageId,
            })
          );
          this.multilanguageList = this.cas.removeDuplicatesFromArrayNew(
            this.multilanguageList
          );
          res.forEach((x: { companyId: string }) =>
            this.multicompanyList.push({
              value: x.companyId,
              label: x.companyId,
            })
          );
          this.multicompanyList = this.cas.removeDuplicatesFromArrayNew(
            this.multicompanyList
          );
          res.forEach((x: { requestId: string }) =>
            this.multirequestList.push({
              value: x.requestId,
              label: x.requestId,
            })
          );
          this.multirequestList = this.cas.removeDuplicatesFromArrayNew(
            this.multirequestList
          );
          res.forEach((x: { storeId: string; storeName: string }) =>
            this.multistoreList.push({
              value: x.storeId,
              label: x.storeId + '-' + x.storeName,
            })
          );
          this.multistoreList = this.cas.removeDuplicatesFromArrayNew(
            this.multistoreList
          );
          res.forEach((x: { createdBy: string }) =>
            this.multiyseridList.push({
              value: x.createdBy,
              label: x.createdBy,
            })
          );
          this.multiselectyseridList = this.multiyseridList;
          this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(
            this.multiyseridList
          );
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
        },
        (err) => {
          this.cs.commonerrorNew(err);
          this.spin.hide();
        }
      )
    );
  }
  openDialog2(data: any = 'New'): void {
   
    if (data != 'New') {
      if (this.selection.selected.length == 0) {
        this.toastr.warning("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    }
    let paramdata = "";
    paramdata = this.cs.encrypt({
      pageflow: data,
      code: data != 'New' ? this.selection.selected[0].requestId : null,
      languageId: data != 'New' ? this.selection.selected[0].languageId : null,
      companyId: data != 'New' ? this.selection.selected[0].companyId : null
    });
    this.router.navigate(['/main/controlgroup/transaction/ownershipNew/' + paramdata]);
   
  }
  downloadexcel() {
    var res: any = [];
    this.dataSource.data.forEach((x) => {
      res.push({
        // 'Language ID': x.languageId,
        // 'Company ID': x.companyId,
        'Request ID': x.requestId,
        'Store ID': x.storeId + ' - ' + x.storeName,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn),
      });
    });
    this.excel.exportAsExcel(res, 'Summary');
  }
  getAllListData() {
    this.getall();
  }
  searhform = this.fb.group({
    languageId: [],
    companyId: [],
    storeId: [],
    requestId: [],
    createdBy: [],
    startCreatedOn: [],
    endCreatedOn: [],
    startCreatedOnFE: [],
    endCreatedOnFE: [],
  });

  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  reset() {
    this.searhform.reset();
  }

  approvalSend(element): void { }

  openScreen(data) {
    let paramdata = '';
    sessionStorage.removeItem('controlGroupsSummary');
    paramdata = this.cs.encrypt({
      requestId: this.selection.selected[0].requestId,
      pageflow: data,
    });
    sessionStorage.setItem('controlGroupsSummary', paramdata);
    const url = this.router.serializeUrl(
      this.router.createUrlTree([
        '/main/controlgroup/transaction/ownership/' + paramdata,
      ])
    );

    window.open('#' + url, '_blank');
  }

  openNew(data: any = 'New'): void {
    let paramdata = '';
    paramdata = this.cs.encrypt({
      pageflow: data,
      code: data != 'New' ? this.selection.selected[0].requestId : null,
      languageId: data != 'New' ? this.selection.selected[0].languageId : null,
      companyId: data != 'New' ? this.selection.selected[0].companyId : null,
    });
    this.router.navigate([
      '/main/controlgroup/transaction/ownershipNew/' + paramdata,
    ]);
  }

  navigateScreen(screen, element) {
    //  ownership

    if (screen == 'ownershipNew') {
      let paramdata = '';
      paramdata = this.cs.encrypt({
        pageflow: 'Display',
        code: element.requestId,
        languageId: element.languageId,
        companyId: element.companyId,
      });
      this.router.navigate([
        '/main/controlgroup/transaction/ownershipNew/' + paramdata,
      ]);
    }

    //Validate

    if (screen == 'validate') {
      if (element.statusId == 1) {
        let paramdata = '';
        sessionStorage.removeItem('controlGroupsSummary');
        paramdata = this.cs.encrypt({ line: element });
        sessionStorage.setItem('controlGroupsSummary', paramdata);
        const url = this.router.serializeUrl(
          this.router.createUrlTree([
            '/main/controlgroup/transaction/ownership/' + paramdata,
          ])
        );
        window.open('#' + url, '_blank');
      } else {
        let paramdata = '';
        paramdata = this.cs.encrypt({ code: element, pageflow: 'Display' });
        if(element.groupTypeId == 1001){
          this.router.navigate([
            '/main/controlgroup/transaction/familytemplate/' + paramdata,
          ]);
        }
        if(element.groupTypeId == 1002){
          this.router.navigate([
            '/main/controlgroup/transaction/brotherSisterRemplate/' + paramdata,
          ]);
        }
   
      }
    }

    if (screen == 'proposed') {
      if (element.statusId == 2) {
        let paramdata = '';
        sessionStorage.removeItem('controlGroupsSummary');
        paramdata = this.cs.encrypt({ line: element });
        sessionStorage.setItem('controlGroupsSummary', paramdata);
        const url = this.router.serializeUrl(
          this.router.createUrlTree([
            '/main/controlgroup/transaction/validation/' + paramdata,
          ])
        );
        window.open('#' + url, '_blank');
      } else {
        let paramdata = '';
        paramdata = this.cs.encrypt({ line: element, pageflow: 'Display' });
        this.router.navigate([
          '/main/controlgroup/transaction/proposal/' + paramdata,
        ]);
      }
    }
    if (screen == 'readyForapproval') {
      if (element.statusId == 3) {
        let paramdata = '';
        sessionStorage.removeItem('controlGroupsSummary');
        paramdata = this.cs.encrypt({ line: element });
        sessionStorage.setItem('controlGroupsSummary', paramdata);
        const url = this.router.serializeUrl(
          this.router.createUrlTree([
            '/main/controlgroup/transaction/proposed/' + paramdata,
          ])
        );
        window.open('#' + url, '_blank');
      }
      if (element.statusId == 6) {
        let paramdata = '';
        sessionStorage.removeItem('controlGroupsSummary');
        paramdata = this.cs.encrypt({ line: element, pageflow: 'transfer' });
        sessionStorage.setItem('controlGroupsSummary', paramdata);
        const url = this.router.serializeUrl(
          this.router.createUrlTree([
            '/main/controlgroup/transaction/proposed/' + paramdata,
          ])
        );
        window.open('#' + url, '_blank');
      } else {
      }
    }
    if (screen == 'approval') {
      if (element.statusId == 4) {
        let paramdata = '';
        sessionStorage.removeItem('controlGroupsSummary');
        paramdata = this.cs.encrypt({ line: element });
        sessionStorage.setItem('controlGroupsSummary', paramdata);
        const url = this.router.serializeUrl(
          this.router.createUrlTree([
            '/main/controlgroup/transaction/approval/' + paramdata,
          ])
        );
        window.open('#' + url, '_blank');
      }
      if (element.statusId == 5) {
        let paramdata = '';
        paramdata = this.cs.encrypt({ line: element, pageflow: 'transfer' });
        this.router.navigate([
          '/main/controlgroup/transaction/storePartnerListing/' + paramdata,
        ]);
      }
    }
  }

  sendMail(element): void {
    const dialogRef = this.dialog.open(EmailRequestComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      data: {
        element: element,
      },
    });

    dialogRef.afterClosed().subscribe((result) => { });
  }

  clientList: any[] = [];
  totalPercentage = 0;
  showClient(e) {
    this.totalPercentage = 0;
    this.clientList = [];
    this.clientList.push(e);
    let total = 0;
    total = total + (e.coOwnerPercentage1 != null ? e.coOwnerPercentage1 : 0) + (e.coOwnerPercentage2 != null ? e.coOwnerPercentage2 : 0) +
      (e.coOwnerPercentage3 != null ? e.coOwnerPercentage3 : 0) + (e.coOwnerPercentage4 != null ? e.coOwnerPercentage4 : 0) +
      (e.coOwnerPercentage5 != null ? e.coOwnerPercentage5 : 0) + (e.coOwnerPercentage6 != null ? e.coOwnerPercentage6 : 0)
      + (e.coOwnerPercentage7 != null ? e.coOwnerPercentage7 : 0) + (e.coOwnerPercentage8 != null ? e.coOwnerPercentage8 : 0)
      + (e.coOwnerPercentage9 != null ? e.coOwnerPercentage9 : 0) + (e.coOwnerPercentage10 != null ? e.coOwnerPercentage10 : 0);

    this.totalPercentage = total;
  }


  generatePdf() {
    this.pdf.validatepdf();
  }



  fileUrldownload: any;
  docurl: any;
  async download(element) {
    this.spin.show()
    const blob = await this.service.download(element.referenceField1)
      .catch((err: HttpErrorResponse) => {
        this.cs.commonerror(err);
      });
    this.spin.hide();
    if (blob) {
      const blobOb = new Blob([blob], {
        type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      });
      this.fileUrldownload = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blobOb));
      // http://remote.url.tld/path/to/document.doc&embedded=true
      this.docurl = window.URL.createObjectURL(blob);
      const a = document.createElement('a')
      a.href = this.docurl
      a.download = element.referenceField1;
      a.click();
      URL.revokeObjectURL(this.docurl);

    }
    this.spin.hide();
  }


  uploadFiles: any;
  viewUpload(element): void {
    const dialogRef = this.dialog.open(UploadFilesComponent, {
      disableClose: true,
      width: '60%',
      maxWidth: '80%',
      data: {line: element,code:element.requestId, pageflow: 'summaryView'}
    });
    dialogRef.afterClosed().subscribe(result => {
      this.uploadFiles = null;
      this.uploadFiles = result.lines;
    })
  }
}
