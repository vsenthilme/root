import { SelectionModel } from "@angular/cdk/collections";
import { HttpErrorResponse } from "@angular/common/http";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { DomSanitizer } from "@angular/platform-browser";
import { Router, ActivatedRoute } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { DownloadComponent } from "src/app/common-field/dialog_modules/download/download.component";
import { dropdownelement, CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { documentTemplateService } from "src/app/main-module/setting/business/document-template/document-template.service";
import { ReceiptNewComponent } from "./receipt-new/receipt-new.component";
import { ReceiptNoService } from "./receipt-no.service";

@Component({
  selector: 'app-receipt-no',
  templateUrl: './receipt-no.component.html',
  styleUrls: ['./receipt-no.component.scss']
})
export class ReceiptNoComponent implements OnInit {

  screenid = 1132;

  displayedColumns: string[] = ['select', 'documentType', 'receiptNo', 'receiptType', 'receiptDate', 'receiptNoticeDate', 'eligibiltyDate', 'expirationDate', 'createdBy', 'createdOn', 'documentdownload', 'statusIddes',];

  docurl: any;
  fileUrldownload: any;

  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  ClientFilter: any;
  matterdesc: any;
  code: any;
  toggleFloat() {

    this.isShowDiv = !this.isShowDiv;
    this.toggle = !this.toggle;

    if (this.icon === 'expand_more') {
      this.icon = 'chevron_left';
    } else {
      this.icon = 'expand_more'
    }
    this.showFloatingButtons = !this.showFloatingButtons;

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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.receiptNo + 1}`;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  ELEMENT_DATA: any[] = [];
  // displayedColumns: string[] = ['select', 'taskno', 'type', 'creation', 'deadline', 'remainder', 'originatting', 'responsible', 'legal', 'status',];

  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);

  constructor(public dialog: MatDialog,
    private service: ReceiptNoService, private router: Router,
    public toastr: ToastrService, private route: ActivatedRoute,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService,
    private documentTemplateService: documentTemplateService,
    private sanitizer: DomSanitizer,) { }
  matterno: any = "";
  RA: any = {};
  ngOnInit(): void {
    this.code = (this.cs.decrypt(this.route.snapshot.params.code));
    this.RA = this.auth.getRoleAccess(this.screenid);
    sessionStorage.setItem('matter', this.route.snapshot.params.code);
    this.matterdesc = this.cs.decrypt(sessionStorage.getItem('matter')).code1;
    this.matterno = this.cs.decrypt(sessionStorage.getItem('matter')).code;
    this.ClientFilter = { matterNumber: this.matterno };

    this.getAllListData();
  }
  deleteDialog() {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    if (this.selection.selected[0].statusId === 10) {
      this.toastr.error("Receipt Number is already approved.", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }

    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: this.selection.selected[0],
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0]);

      }
    });
  }
  deleterecord(data: any) {
    this.spin.show();
    this.sub.add(this.service.Delete(data.receiptNo, data.classId, data.matterNumber, data.languageId).subscribe((res) => {
      this.toastr.success(data.receiptNo + " deleted successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      this.spin.hide();// this.getAllListData();
      window.location.reload();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  openDialog(data: any = 'new'): void {
    if (data != 'New') {
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
      if (this.selection.selected[0].statusId === 10 && data == 'Edit') {
        this.toastr.error("Approved Receipt Number can't be Edited.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    }
    const dialogRef = this.dialog.open(ReceiptNewComponent, {
      disableClose: true,
      width: '80%',
      maxWidth: '80%',
      //position: { top: '2.0%' },
      data: { pageflow: data, matter: this.cs.decrypt(this.route.snapshot.params.code).code, matterdesc: this.matterdesc, code: data != 'New' ? this.selection.selected[0] : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      this.getAllListData();
    });
  }
  sub = new Subscription();
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination
  receiptNolist: any[] = [];
  statuslist: any[] = [];

  getall(excel: boolean = false) {

    this.spin.show();
    this.cas.getalldropdownlist([
      // this.cas.dropdownlist.setup.receiptNo.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      // this.receiptNolist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.receiptNo.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: any[]) => {
        res.forEach((x) => {
          // x.receiptNo = this.receiptNolist.find(y => y.key == x.noteTypeId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;

        })
        this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno);

        if (excel)
          this.excel.exportAsExcel(res, "Receipt No");
        this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
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
  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Document Type": x.documentType,
        //   'Name': x.name,
        "Receipt Number": x.receiptNo,
        'Receipt Type': x.receiptType,
        "Receipt Date": this.cs.excel_date(x.receiptDate),
        'Eligibility Date':this.cs.excel_date(x.eligibiltyDate),
        'Expiration Date':this.cs.excel_date(x.expirationDate),
        'Created By': x.createdBy,
        'Created On': this.cs.excel_date(x.createdOn),
        'Status': x.statusIddes,
      });

    })
    this.excel.exportAsExcel(res, "Receipt No");
  }
  getAllListData() {
    this.getall();
  }

  searchStatusList = {
    statusId: [38, 39]
  };
  searhform = this.fb.group({
    endCreatedOn: [],
    receiptNo: [],
    expenseType: [],
    matterNumber: [this.matterno],
    createdBy: [],
    startCreatedOn: [],
    statusId: [],
  });
  Clear() {
    this.reset();
  };

  search() {
    // this.searhform.controls.endCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.endCreatedOn.value));
    // this.searhform.controls.startCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.startCreatedOn.value));


    // this.spin.show();
    // this.cas.getalldropdownlist([
    //   // this.cas.dropdownlist.setup.receiptNo.url,
    //   this.cas.dropdownlist.setup.statusId.url,
    // ]).subscribe((results) => {
    //   // this.receiptNolist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.receiptNo.key);
    //   this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
    //   this.spin.hide();

    //   this.spin.show();
    //   this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {

    //     res.forEach((x) => {
    //       // x.noteTypeId = this.noteTypeIdlist.find(y => y.key == x.noteTypeId)?.value;
    //       x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
    //     })
    //     this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno);
    //     this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
    //     this.selection = new SelectionModel<any>(true, []);
    //     this.dataSource.sort = this.sort;
    //     this.dataSource.paginator = this.paginator;
    //     this.spin.hide();
    //   }, err => {
    //     this.cs.commonerror(err);
    //     this.spin.hide();
    //   }));
    // }, (err) => {
    //   this.toastr.error(err, "");
    // });
    // this.spin.hide();

  }
  reset() {
    this.searhform.reset();
  }

  async download(element) {
    this.spin.show()
    console.log(element);
    const blob = await this.documentTemplateService.getfile1(element.referenceField8,element.clientId,element.matterNumber)
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
      a.download = element.referenceField8;
      a.click();
      URL.revokeObjectURL(this.docurl);

    }
    this.spin.hide();
  }


  downloadConfirm(element): void {
    const dialogRef = this.dialog.open(DownloadComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
      position: { top: '9%', },
      data: { url: this.fileUrldownload, fileName: element.referenceField8}
    });

    dialogRef.afterClosed().subscribe(result => {
     // this.search();
    });
  }

}