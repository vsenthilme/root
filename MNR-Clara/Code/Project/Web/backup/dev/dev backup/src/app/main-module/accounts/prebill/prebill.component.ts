import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonApiService, dropdownelement } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { PrebillService } from "./prebill.service";
// import the pdfmake library
import pdfMake from "pdfmake/build/pdfmake";
// importing the fonts and icons needed
import pdfFonts from "./../../../../assets/font/vfs_fonts.js";
import { defaultStyle } from "./../../../config/customStyles";
import { fonts } from "./../../../config/pdfFonts";
import { logo } from "./../../../../assets/font/logo.js";
import { DatePipe, DecimalPipe } from "@angular/common";
import { GeneralMatterService } from "../../matters/case-management/General/general-matter.service";
import { diamondlogo } from "../../../../assets/font/dimond_logo.js";
import { resizedLogo } from "../../../../assets/font/resized_logo.js";

// PDFMAKE fonts
pdfMake.vfs = pdfFonts.pdfMake.vfs;
pdfMake.fonts = fonts;

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-prebill',
  templateUrl: './prebill.component.html',
  styleUrls: ['./prebill.component.scss']
})
export class PrebillComponent implements OnInit {

  screenid = 1137;
  public icon = 'expand_more';

  displayedColumns = ['select', 'action', 'preBillBatchNumber', 'preBillNumber', 'clientId', 'matterNumber', 'preBillDate', 'doc', 'partnerAssigned', 'totalAmount', 'referenceField5', 'statusId',];

  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  billmodeList: dropdownelement[];
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.preBillNumber + 1}`;
  }
  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  ELEMENT_DATA: any[] = [];
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);


  constructor(public dialog: MatDialog,
    private service: PrebillService, private router: Router, private matterservice: GeneralMatterService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private auth: AuthService,
    private decimalPipe: DecimalPipe,
    public datePipe: DatePipe
  ) { }
  pageflow = "Generate"

  RA: any = {};
  ngOnInit(): void {


    this.pageflow = this.route.snapshot.params.code ? this.route.snapshot.params.code : "Generate";
    if (this.pageflow != 'Approve') {
      this.displayedColumns = ['select', 'preBillBatchNumber', 'preBillNumber', 'clientId', 'matterNumber', 'preBillDate', 'doc', 'partnerAssigned', 'totalAmount', 'statusId',];
      this.screenid = 1139;
      this.RA = this.auth.getRoleAccess(1139);

    }
    else
      this.RA = this.auth.getRoleAccess(this.screenid);

    this.auth.isuserdata();

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
  deleterecord(id: any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id.preBillNumber, id.preBillBatchNumber).subscribe((res) => {
      this.toastr.success(id.preBillNumber + " deleted successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      this.spin.hide(); //this.getAllListData();
      window.location.reload();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  openDialog(data: any = 'new'): void {

    if (data != 'New')
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }

    let paramdata = "";
    // debugger
    if (data != 'New') {
      if (this.selection.selected.length > 0) {

        // if (this.selection.selected[0].statusId == 10 && data == 'Edit') {
        //   this.toastr.error("Quotation cannot be edited.", "");
        //   return;
        // }
        paramdata = this.cs.encrypt({ code: this.selection.selected[0], pageflow: data });

        this.router.navigate(['/main/accounts/prebill-display/' + paramdata]);
      }
      else {
        paramdata = this.cs.encrypt({ pageflow: data });

        this.router.navigate(['/main/accounts/prebill-display/' + paramdata]);
      }
    }
    else {

      if (this.selection.selected.length > 0) {
        if (this.selection.selected[0].statusId == 10 && data == 'Edit') {
          this.toastr.error("Prebill  can't be edited.", "");
          return;
        }
        paramdata = this.cs.encrypt({ code: this.selection.selected[0], pageflow: data });

        this.router.navigate(['/main/accounts/prebill-new/' + paramdata]);
      }
      else {
        paramdata = this.cs.encrypt({ pageflow: data });

        this.router.navigate(['/main/accounts/prebill-new/' + paramdata]);
      }

    }

  }
  sub = new Subscription();
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

  @ViewChild(MatSort)
  sort: MatSort;
  @ViewChild(MatPaginator)
  paginator: MatPaginator; // Pagination
  classIdList: any[] = [];
  statuslist: any[] = [];
  clientIdlist: any[] = [];
  userTypeList: any[] = [];
  matteridList: any[] = [];

  statusId = 45;
  statusIdApproval = 29;

  selectedItems4: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];


  selectedItems7: SelectItem[] = [];
  multiselectcreatedList: SelectItem[] = [];
  multicreatedList: SelectItem[] = [];

  selectedItems8: SelectItem[] = [];
  multiselectmatterListList: any[] = [];
  multimatterListList: any[] = [];

  selectedItems3: SelectItem[] = [];
  multiselectyseridList: any[] = [];
  multiyseridList: any[] = [];


  dropdownSettings = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  multiselectclassList: any[] = [];
  multiselectclientList: any[] = [];
  multiclientList: any[] = [];
  multiselectpreBillNumberList: any[] = [];
  multiymatterdescList: any[] = [];
  multiselectpreBillBatchNumberList: any[] = [];

  matterList: any[] = [];
  clientList: any[] = [];

  getall(excel: boolean = false) {
    this.spin.show();

    this.sub.add(this.matterservice.getAllSearchDropDown().subscribe((data: any) => {
      if (data) {
        data.matterList.forEach((x: any) => this.matterList.push({ value: x.key, label: x.key + '-' + x.value }));
        this.matterList.forEach((x: any) => this.multimatterListList.push({ value: x.value, label: x.label }));
        this.multiselectmatterListList = this.multimatterListList

        data.clientNameList.forEach((x: any) => this.clientList.push({ value: x.key, label: x.key + '-' + x.value }));
        this.clientList.forEach((x: any) => this.multiclientList.push({ value: x.value, label: x.label }));
        this.multiselectclientList = this.multiclientList

        this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
        this.cas.dropdownlist.setup.statusId.url,
        this.cas.dropdownlist.setup.userId.url,
        ]).subscribe((results) => {
          this.classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
          this.classIdList.forEach((x: { key: string; value: string; }) => this.multiselectclassList.push({ value: x.key, label: x.value }))

          this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [29, 45, 51, 56].includes(s.key));
          this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({ value: x.key, label: x.key + '-' + x.value }))
          this.multiselectstatusList = this.multistatusList;


          this.userTypeList = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.setup.userId.key);
          this.userTypeList.forEach((x: { key: string; value: string; }) => this.multicreatedList.push({ id: x.key, itemName: x.value }))
          this.multiselectcreatedList = this.multicreatedList;
          this.spin.hide();

          this.spin.show();
        if(this.pageflow != 'Approve'){
          this.sub.add(this.service.Search({statusId : [45, 46, 29, 56]}).subscribe((res: any[]) => {
            res.forEach((x: { partnerAssigned: string; }) => this.multiyseridList.push({ value: x.partnerAssigned, label: x.partnerAssigned }))
            this.multiselectyseridList = this.multiyseridList;
            this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);
            // let result = res.filter((x: any) => x.statusId != 51 && x.deletionIndicator == 0)
            // let result1 = result.filter((x: any) => x.partnerAssigned == this.auth.userID)

            res.forEach((x: { preBillNumber: string; }) => this.multiselectpreBillNumberList.push({ value: x.preBillNumber, label: x.preBillNumber }))
            this.multiselectpreBillNumberList = this.cas.removeDuplicatesFromArrayNew(this.multiselectpreBillNumberList);

            res.forEach((x: { preBillBatchNumber: string; }) => this.multiselectpreBillBatchNumberList.push({ value: x.preBillBatchNumber, label: x.preBillBatchNumber }))
            this.multiselectpreBillBatchNumberList = this.cas.removeDuplicatesFromArrayNew(this.multiselectpreBillBatchNumberList);

            this.ELEMENT_DATA = res;

            // if (this.pageflow == 'Approve') {
            //   this.ELEMENT_DATA = result1;
            // }
            // else {
            //   this.ELEMENT_DATA = result;
            // }

            ///this.ELEMENT_DATA = res;
            this.ELEMENT_DATA.forEach((x) => {
              x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
              x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
              // if (this.pageflow == 'Approve') {
              //   x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
              // }
              // else {
              //   x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
              // }
              x.clientId = this.clientList.find(y => y.value == x.clientId)?.label;
              x['matterdesc'] = this.matterList.find(y => y.value == x.matterNumber)?.label;
            })



            if (excel)
              this.excel.exportAsExcel(this.ELEMENT_DATA.sort((a, b) => (a.preBillNumber > b.preBillNumber) ? -1 : 1));
            this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.preBillNumber > b.preBillNumber) ? -1 : 1));
            // this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
            this.selection = new SelectionModel<any>(true, []);
            this.dataSource.sort = this.sort;
            this.dataSource.paginator = this.paginator;
            this.spin.hide();
          }, err => {
            this.cs.commonerror(err);
            this.spin.hide();
          }));
        }
        if(this.pageflow == 'Approve'){
          this.sub.add(this.service.Search({partnerAssigned : [this.auth.userID], statusId : [45, 46, 29, 56]}).subscribe((res: any[]) => {
            res.forEach((x: { partnerAssigned: string; }) => this.multiyseridList.push({ value: x.partnerAssigned, label: x.partnerAssigned }))
            this.multiselectyseridList = this.multiyseridList;
            this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);
            // let result = res.filter((x: any) => x.statusId != 51 && x.deletionIndicator == 0)
            // let result1 = result.filter((x: any) => x.partnerAssigned == this.auth.userID)

            res.forEach((x: { preBillNumber: string; }) => this.multiselectpreBillNumberList.push({ value: x.preBillNumber, label: x.preBillNumber }))
            this.multiselectpreBillNumberList = this.cas.removeDuplicatesFromArrayNew(this.multiselectpreBillNumberList);

            res.forEach((x: { preBillBatchNumber: string; }) => this.multiselectpreBillBatchNumberList.push({ value: x.preBillBatchNumber, label: x.preBillBatchNumber }))
            this.multiselectpreBillBatchNumberList = this.cas.removeDuplicatesFromArrayNew(this.multiselectpreBillBatchNumberList);

            this.ELEMENT_DATA = res;

            // if (this.pageflow == 'Approve') {
            //   this.ELEMENT_DATA = result1;
            // }
            // else {
            //   this.ELEMENT_DATA = result;
            // }

            ///this.ELEMENT_DATA = res;
            this.ELEMENT_DATA.forEach((x) => {
              x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
              x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
              // if (this.pageflow == 'Approve') {
              //   x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
              // }
              // else {
              //   x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
              // }
              x.clientId = this.clientList.find(y => y.value == x.clientId)?.label;
              x['matterdesc'] = this.matterList.find(y => y.value == x.matterNumber)?.label;
            })



            if (excel)
              this.excel.exportAsExcel(this.ELEMENT_DATA.sort((a, b) => (a.preBillNumber > b.preBillNumber) ? -1 : 1));
            this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.preBillNumber > b.preBillNumber) ? -1 : 1));
            // this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
            this.selection = new SelectionModel<any>(true, []);
            this.dataSource.sort = this.sort;
            this.dataSource.paginator = this.paginator;
            this.spin.hide();
          }, err => {
            this.cs.commonerror(err);
            this.spin.hide();
          }));
        }
        }, (err) => {
          this.toastr.error(err, "");
        });
        this.spin.hide();
      }

    }, (err) => {
      this.toastr.error(err, "");
    }));




  }
  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Client Name": x.clientId,
        'Batch No': x.preBillBatchNumber,
        "Prebill No  ": x.preBillNumber,
        "Partner ": x.partnerAssigned,
        'Matter Description': x.matterNumber,
        "Prebill Amount": x.totalAmount,
        "Status  ": x.statusIddes,
        'Prebill Date': this.cs.dateapiutc0(x.preBillDate),
      });

    })
    this.excel.exportAsExcel(res, "Prebill");
  }
  getAllListData() {
    this.getall();
  }
  userTypeIdL = { userTypeId: [1] };
  searchStatusList = { statusId: [29, 45, 51] };
  intakeFormNumberList: dropdownelement[] = [];
  searhform = this.fb.group({
    classId: [],
    clientId: [],
    createdByFE: [],
    createdBy: [],
    endPreBillDate: [],
    endPreBillDateFE: [],
    partnerAssigned: [],
    partnerAssignedFE: [],
    matterNumber: [],
    matterNumberFE: [],
    preBillNumber: [],
    preBillBatchNumber: [],
    startPreBillDate: [],
    startPreBillDateFE: [],
    statusIdFE: [],
    statusId: [],
  });
  Clear() {
    this.reset();
  };

  search() {
    this.searhform.controls.endPreBillDate.patchValue(this.cs.dateNewFormat(this.searhform.controls.endPreBillDateFE.value));
    this.searhform.controls.startPreBillDate.patchValue(this.cs.dateNewFormat(this.searhform.controls.startPreBillDateFE.value));

    if (this.selectedItems3 && this.selectedItems3.length > 0) {
      let multiyseridList: any[] = []
      this.selectedItems3.forEach((a: any) => multiyseridList.push(a.id))
      this.searhform.patchValue({ partnerAssigned: this.selectedItems3 });
    }

    if (this.selectedItems4 && this.selectedItems4.length > 0) {
      let multistatusList: any[] = []
      this.selectedItems4.forEach((a: any) => multistatusList.push(a.id))
      this.searhform.patchValue({ statusId: this.selectedItems4 });
    }

    if (this.selectedItems7 && this.selectedItems7.length > 0) {
      let multicreatedList: any[] = []
      this.selectedItems7.forEach((a: any) => multicreatedList.push(a.id))
      this.searhform.patchValue({ createdBy: multicreatedList });
    }
    if (this.selectedItems8 && this.selectedItems8.length > 0) {
      let multimatterListList: any[] = []
      this.selectedItems8.forEach((a: any) => multimatterListList.push(a.id))
      this.searhform.patchValue({ matterNumber: multimatterListList });
    }




    this.spin.show();
    this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {
      console.log(res)
      let result = res.filter((x: any) => x.statusId != 51 && x.deletionIndicator == 0)
      let result1 = result.filter((x: any) => x.partnerAssigned == this.auth.userID)
      if (this.pageflow == 'Approve') {
        this.ELEMENT_DATA = result1;
      }
      else {
        this.ELEMENT_DATA = result;
      }

      ///this.ELEMENT_DATA = res;
      this.ELEMENT_DATA.forEach((x) => {
        x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
        if (this.pageflow == 'Approve') {
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
        }
        else {
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
        }
        x.clientId = this.clientList.find(y => y.value == x.clientId)?.label;
        x['matterdesc'] = this.matterList.find(y => y.value == x.matterNumber)?.label;
      })




      this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
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
  reset() {
    this.searhform.reset();
  }

  openApprove(data: any): void {

    let paramdata = this.cs.encrypt({ code: this.selection.selected[0], pageflow: data });
    console.log(data)

    this.router.navigate(['/main/accounts/prebill-approve/' + paramdata]);

  }

  generatePdf(element: any) {
    let currentDate = this.datePipe.transform(new Date, 'MM-dd-yyyy', "GMT00:00");
    this.sub.add(this.service.getPreBillPdfData(element.matterNumber, element.preBillNumber).subscribe((res: any) => {
      console.log(res)
      let unPaidPriorBalance = 0;
      unPaidPriorBalance = ((res.finalSummary.priorBalance != null ? res.finalSummary.priorBalance : 0) - (res.finalSummary.paymentReceived != null ? res.finalSummary.paymentReceived : 0))

      let totalfinalamount = 0;
      totalfinalamount = (((res.finalSummary.priorBalance != null ? res.finalSummary.priorBalance : 0) -
        (res.finalSummary.paymentReceived != null ? res.finalSummary.paymentReceived : 0)) +
        (res.finalSummary.currentFees != null ? res.finalSummary.currentFees : 0) +
        (res.finalSummary.advancedCost != null ? res.finalSummary.advancedCost : 0));

        // totalfinalamount  = ((res.finalSummary.advancedCost != null ? res.finalSummary.advancedCost : 0) +
        // (res.finalSummary.currentFees != null ? res.finalSummary.currentFees : 0) +
        // (res.finalSummary.priorBalance != null ? res.finalSummary.priorBalance : 0) -
        // ((res.finalSummary.paymentReceived != null ? res.finalSummary.paymentReceived : 0)));

      if (res != null) {

     //   let groupedTimeTickets = this.cs.groupByData(res.timeTicketDetail.timeTickets, (data: any) => data.taskCode);
        let groupedTimeTickets = this.cs.groupByData(res.timeTicketDetail.timeTickets.sort((a, b) => (a.taskCode > b.taskCode) ? 1 : -1), (data: any) => data.taskCode);
        console.log(groupedTimeTickets)
        let taskCode: any[] = [];
        res.timeTicketDetail.timeTickets.forEach(data => {
          if (!taskCode.includes(data.taskCode)) {
            taskCode.push(data.taskCode);
          }
        })
        var dd: any;
        dd = {
          pageSize: "A4",
          pageOrientation: "portrait",
          pageMargins: [40, 95, 40, 60],
          header(currentPage: number, pageCount: number, pageSize: any): any {
            if (currentPage != 1) {
              return [{
                layout: 'noBorders', // optional
                table: {
                  // headers are automatically repeated if the table spans over multiple pages
                  // you can declare how many rows should be treated as headers
                  headerRows: 0,
                  widths: [60, 150, 140, 50, '*'],
                  body: [
                    [{ text: 'Client Number', bold: true, fontSize: 10 }, { text: ': ' + res.reportHeader.clientId, fontSize: 10 }, { text: '', fontSize: 10 }, { text: '', fontSize: 10 }, { text: currentDate, fontSize: 10 }],
                    [{ text: 'Matter', bold: true, fontSize: 10 }, { text: ': ' + res.reportHeader.matterNumber, fontSize: 10 }, { text: '', fontSize: 10 }, { text: '', fontSize: 10 }, { text: 'Page : ' + currentPage, fontSize: 10 }]
                  ]
                },
                margin: [40, 30]
              }]
            } else {
              return [{

                table: {
                  headerRows: 0,
                  widths: [130, 400,],

                  body: [

                    [
                   { image: diamondlogo.smallLogo, fit: [60, 60],  bold: true, fontSize: 12, border: [false, false, false, false] },
                    { image: resizedLogo.resized, fit: [230, 230],  bold: true, fontSize: 12, border: [false, false, false, false] },
                    ],

                  ]
                },
                margin: [40, 20, 40, 10]
              }]
            }
          },
          footer(currentPage: number, pageCount: number, pageSize: any): any {
            if (currentPage < pageCount) {
              return [{
                text: 'Continued on Next Page',
                style: 'header',
                alignment: 'center',
                bold: true,
                fontSize: 6
              }]
            }
          },
          content: [
            '\n',
            {
              layout: 'noBorders', // optional
              table: {
                // headers are automatically repeated if the table spans over multiple pages
                // you can declare how many rows should be treated as headers
                headerRows: 0,
                widths: [150, 55, 100, 80, 140],
                body: [
                  [{ text: (res.reportHeader.addressLine15 != null ? res.reportHeader.addressLine15 : '') + ', ' + (res.reportHeader.addressLine16 != null ? res.reportHeader.addressLine16 : '') + ' ' + (res.reportHeader.addressLine18 != null ? res.reportHeader.addressLine18 : ''), fontSize: 10 },
                  { text: '', fontSize: 10 },
                  { text: "PRE-BILL", bold: true, italics: true, fontSize: 20,},
                  { text: '', fontSize: 10 },
                  { text: this.datePipe.transform(new Date, 'MM-dd-yyyy', "GMT00:00"), fontSize: 10 }],
                ]
              },
            },
            '\n',
            { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] },
            '\n',
            {
              layout: 'noBorders', // optional
              table: {
                headerRows: 0,
                widths: [70, 110, 100, 140, 60],
                body: [
                  [{ text: 'Client:', fontSize: 9, alignment: 'right', bold: true }, { text: res.reportHeader.clientId + ' - ' + res.reportHeader.clientName, fontSize: 9 }, { text: '', fontSize: 9 }, { text: '', fontSize: 9 }, { text: '', fontSize: 9 }],
                  [{ text: 'Matter:', fontSize: 9, alignment: 'right', bold: true }, { text: res.reportHeader.matterNumber + ' - ' + res.reportHeader.matterDescription, fontSize: 9 }, { text: '', fontSize: 9 }, { text: '', fontSize: 9 }, { text: '', fontSize: 9, bold: true }],
                  [{ text: 'Matter Type:', fontSize: 9, alignment: 'right', bold: true }, { text: res.reportHeader.caseCategory + '-' + res.reportHeader.caseSubCategory, fontSize: 9 }, '', { text: 'Originating Timekeeper:', fontSize: 9, bold: true, alignment: 'right' }, { text: res.reportHeader.originatingTimeKeeper, fontSize: 9, alignment: 'left' }],
                  [{ text: 'Comments:', fontSize: 9, alignment: 'right', bold: true }, { text: res.reportHeader.comments, fontSize: 9 }, '', { text: 'Responsible Timekeeper:', fontSize: 9, bold: true, alignment: 'right' }, { text: res.reportHeader.responsibleTimeKeeper, fontSize: 9, alignment: 'left' }],
                  [{ text: 'File Open Date:', fontSize: 9, alignment: 'right', bold: true }, { text: this.datePipe.transform(res.reportHeader.caseOpenedDate, 'MM-dd-yyyy', "GMT00:00"), fontSize: 9 }, '', { text: 'Billing Format Code:', fontSize: 9, bold: true, alignment: 'right' }, { text: res.reportHeader.billingFormatId, fontSize: 9, alignment: 'left' }],
                  [{ text: 'Billing Mode:', fontSize: 9, alignment: 'right', bold: true }, { text: res.reportHeader.billingModeId, fontSize: 9 }, { text: 'Bill Date :' + this.cs.dateapiutc0(res.reportHeader.preBillDate), fontSize: 9, bold: true }, { text: 'Fees/Costs Cut Date:', fontSize: 9, bold: true, alignment: 'right' }, { text: this.datePipe.transform(res.reportHeader.feesCostCutoffDate, 'MM-dd-yyyy', "GMT00:00"), fontSize: 9 }],
                  [{ text: 'Billing Frequency:', fontSize: 9, alignment: 'right', bold: true }, { text: res.reportHeader.billingFrequencyId, fontSize: 9 }, { text: 'Start Date:' + this.cs.dateapiutc0(res.reportHeader.startDateForPreBill), fontSize: 9, bold: true }, { text: 'Payments Cut Date:', fontSize: 9, bold: true, alignment: 'right' }, { text: this.datePipe.transform(res.reportHeader.paymentCutoffDate, 'MM-dd-yyyy', "GMT00:00"), fontSize: 9 }],
                  [{ text: 'Remarks:', fontSize: 9, alignment: 'right', bold: true }, { text: res.reportHeader.billingRemarks, fontSize: 9 }, { text: 'Last Bill :', fontSize: 9, bold: true }, { text: 'Type of Bill:', fontSize: 9, bold: true, alignment: 'right' }, { text: 'Regular', fontSize: 9 }],

                ]
              },
            },
            '\n',
            { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] },
            '\n'
          ],
          defaultStyle
        };

        //Account Aging
        if (res.accountAgingDetail != null) {
          let accountData = res.accountAgingDetail;
          dd.content.push({
            table: {
              headerRows: 0,
              widths: [100, 70, 100, 100, 100],
              body: [
                [{ text: 'Account', margin: [0, 10, 0, 0], fontSize: 10, bold: true, border: [false, true, false, false] }, { text: 'Current', decoration: 'underline', fontSize: 10, bold: true, border: [false, true, false, false] }, { text: '30-59 Days', decoration: 'underline', fontSize: 10, bold: true, border: [false, true, false, false] }, { text: '60-89 Days', decoration: 'underline', fontSize: 10, bold: true, border: [false, true, false, false] }, { text: '90 Days and Over', style: 'tableHeader', decoration: 'underline', fontSize: 10, bold: true, border: [false, true, false, false] }],
                [{ text: 'Aging', lineHeight: 2, border: [false, false, false, true], bold: true, fontSize: 10 }, { text: accountData.currentAmount != null ? accountData.currentAmount : '0.00', border: [false, false, false, true], fontSize: 10 }, { text: accountData.amount30To59Days != null ? accountData.amount30To59Days : '0.00', border: [false, false, false, true], fontSize: 10 }, { text: accountData.amount60To89Days != null ? accountData.amount60To89Days : '0.00', border: [false, false, false, true], fontSize: 10 }, { text: accountData.amount90DaysAndOver != null ? accountData.amount90DaysAndOver : '0.00', border: [false, false, false, true], fontSize: 10 }],
                [{ colspan: 2, lineHeight: 2, text: 'Fees Billed to Date:', margin: [0, 10, 0, 0], border: [false, false, false, true], bold: true, fontSize: 10 }, { text: '', border: [false, false, false, true], fontSize: 10 }, { text: accountData.feesBilledToDate != null ? '$ ' + this.decimalPipe.transform(accountData.feesBilledToDate, "1.2-2") : '$ 0.00', border: [false, false, false, true], margin: [0, 10, 0, 0], fontSize: 10 }, { text: 'Costs Billed to Date:', border: [false, false, false, true], bold: true, margin: [0, 10, 0, 0], fontSize: 10 }, { text: accountData.costsBilledToDate != null ? '$ ' + this.decimalPipe.transform(accountData.costsBilledToDate, "1.2-2") : '$ 0.00', border: [false, false, false, true], margin: [0, 10, 0, 0], fontSize: 10 }]
              ]
            },
            layout: {
              hLineStyle: function (i, node) {
                return { dash: { length: 10, space: 4 } };
              },
            }
          })
        }

        //Time tickets
        if (groupedTimeTickets.size > 0) {
          // border: [left, top, right, bottom]
          // border: [false, false, false, false]
          let bodyArray: any[] = [];
          bodyArray.push([{ text: 'Ticket No', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Date', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Timekeeper', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Description', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Hours', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Amount', bold: true, decoration: 'underline', alignment: 'center', fontSize: 10, border: [false, false, false, false] }, { text: '', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] },]);

          taskCode.forEach(task => {
            let taskIndex = 0;
            let totalHours = 0;
            let totalBillableAmount = 0;
           // groupedTimeTickets.get(task).forEach((timeTicket, i) => {
           groupedTimeTickets.get(task).sort((a, b) => (a.timeTicketDate > b.timeTicketDate) ? 1 : -1).forEach((timeTicket, i) => {
              timeTicket.billType = timeTicket.billType.toLowerCase()
            //  let description = decodeURIComponent(timeTicket.ticketDescription);
              totalHours = totalHours + (timeTicket.billableTimeInHours != null ? timeTicket.billableTimeInHours : 0.00);
              totalBillableAmount = totalBillableAmount + (timeTicket.billableAmount != null ? timeTicket.billType == "billable" ? timeTicket.billableAmount : 0.00 : 0.00);

              if (taskIndex == 0) {
                bodyArray.push([
                  { text: timeTicket.taskCode != null ? timeTicket.taskCode : '', fontSize: 10, bold: true, border: [false, false, false, false], lineHeight: 2 },
                  { text: timeTicket.taskText != null ? timeTicket.taskText : '', colSpan: 6, fontSize: 10, bold: true, border: [false, false, false, false], lineHeight: 2 },
                ])
                taskIndex++;
              }

              bodyArray.push([
                { text: timeTicket.timeTicketNumber != null ? timeTicket.timeTicketNumber : '', fontSize: 10, border: [false, false, false, false] },
                { text: this.datePipe.transform(timeTicket.timeTicketDate, 'MM-dd-yyyy', "GMT00:00"), fontSize: 10, border: [false, false, false, false] },
                { text: timeTicket.timeTicketName != null ? timeTicket.timeTicketName : '', fontSize: 10, border: [false, false, false, false] },
                { text: timeTicket.ticketDescription != null ? (timeTicket.taskCode != null ? timeTicket.taskCode : '') + (timeTicket.taskCode != null ? ': ' : '') + (timeTicket.activityCode != null ? timeTicket.activityCode : '') + (timeTicket.activityCode != null ? '- ' : '') + timeTicket.ticketDescription : ' ', fontSize: 10, border: [false, false, false, false] },
                { text: timeTicket.billableTimeInHours != null ? this.decimalPipe.transform(timeTicket.billableTimeInHours, "1.1-1") : '0.0', fontSize: 10, border: [false, false, false, false] },
                { text: timeTicket.billableAmount != null ? timeTicket.billableAmount != 0 ? timeTicket.billType == "non-billable" ? '0.00 NB' : timeTicket.billType == "no%20charge" ? '0.00 NC' : '$ ' + this.decimalPipe.transform(timeTicket.billableAmount, "1.2-2") : '$0.00' : '$0.00', fontSize: 10, border: [false, false, false, false], alignment: 'right', },
                { text: timeTicket.billType != null ? timeTicket.billType == "billable" ? 'BL' : timeTicket.billType == "non-billable" ? 'NB' : timeTicket.billType == "no%20charge" ? 'NC' : '' : '', fontSize: 10, border: [false, false, false, false] },
              ])
              if ((i + 1) == groupedTimeTickets.get(task).length) {
                bodyArray.push([
                  { text: '', fontSize: 10, border: [false, false, false, false] },
                  { text: '', fontSize: 10, border: [false, false, false, false] },
                  { text: '', fontSize: 10, border: [false, false, false, false] },

                  { text: timeTicket.taskText != null ? timeTicket.taskText : '', fontSize: 10, bold: true, border: [false, false, false, false] },
                  { text: this.decimalPipe.transform(totalHours, "1.1-1"), fontSize: 10, border: [false, true, false, true], bold: true },
                  { text: '$ ' + this.decimalPipe.transform(totalBillableAmount, "1.2-2") + '    ', fontSize: 10, border: [false, true, false, true], bold: true, alignment: 'right', },
                  { text: '', fontSize: 10, border: [false, false, false, false] },
                ])
              }

            });


          });

          bodyArray.push([
            { text: '', fontSize: 10, border: [false, false, false, false] },
            { text: '', fontSize: 10, border: [false, false, false, false] },
            { text: '', fontSize: 10, border: [false, false, false, false] },

            { text: 'Grand Total:', fontSize: 10, bold: true, border: [false, false, false, false], alignment: 'right' },
            { text: this.decimalPipe.transform(res.timeTicketDetail.sumOfTotalHours, "1.1-1"), fontSize: 10, border: [false, false, false, false], bold: true },
            { text: '$ ' + this.decimalPipe.transform(res.timeTicketDetail.sumOfTotalBillableAmount, "1.2-2") + '    ', fontSize: 10, border: [false, false, false, false], bold: true, alignment: 'right', },
            { text: '', fontSize: 10, border: [false, false, false, false] },
          ])


          bodyArray.push([
            { text: '', fontSize: 10, border: [false, false, false, false] },
            { text: '', fontSize: 10, border: [false, false, false, false] },
            { text: '', fontSize: 10, border: [false, false, false, false] },

            { text: 'Total Hours:', fontSize: 10, bold: true, border: [false, false, false, false], alignment: 'right' },
            { text: this.decimalPipe.transform(res.timeTicketDetail.sumOfTotalHours, "1.1-1"), fontSize: 10, border: [false, false, false, false]},
            { text: '    ', fontSize: 10, border: [false, false, false, false], bold: true, alignment: 'right', },
            { text: '', fontSize: 10, border: [false, false, false, false] },
          ])

        //Billable Hrs
          bodyArray.push([
            { text: '', fontSize: 10, border: [false, false, false, false] },
            { text: '', fontSize: 10, border: [false, false, false, false] },
            { text: '', fontSize: 10, border: [false, false, false, false] },

            { text: 'Billable Hours:', fontSize: 10, bold: true, border: [false, false, false, false], alignment: 'right' },
            { text: this.decimalPipe.transform(res.timeTicketDetail.sumOfTotalBillableHours, "1.1-1"), fontSize: 10, border: [false, false, false, false] },
            { text: '    ', fontSize: 10, border: [false, false, false, false], bold: true, alignment: 'right', },
            { text: '', fontSize: 10, border: [false, false, false, false] },
          ])
          dd.content.push(
            '\n',
            {
              text: 'Fees',
              style: 'header',
              decoration: 'underline',
              alignment: 'center',
              bold: true
            },
            '\n',
            {
              table: {
                // layout: 'noBorders', // optional
                // heights: [,60,], // height for each row
                headerRows: 0,
                widths: [40, 50, 60, 200, 30, 60, 50],
                body: bodyArray
              }
            },
            // { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] }
          )
        }
        //Timekeeper Summary
        if (res.timeKeeperSummary) {
          if (res.timeKeeperSummary.length > 0) {
            let stackArray: any[] = [];
            stackArray.push(
              {
                text: 'Timekeeper Summary',
                alignment: 'center',
                fontSize: 12,
                lineHeight: 1.3,
                bold: true
              });
            let map = new Map();
            res.timeKeeperSummary.forEach((timeKeeper, i) => {
              if (timeKeeper.billType.toUpperCase() == 'BILLABLE') {
                let timeKeeperRatePerHour = timeKeeper.timeTicketAssignedRate != null ? this.decimalPipe.transform(timeKeeper.timeTicketAssignedRate, "1.2-2") : '$ 0.00'
                let timeTickerHours = timeKeeper.timeTicketHours != null ? this.decimalPipe.transform(timeKeeper.timeTicketHours, "1.1-1") : '0.0'
                let timeTotal = timeKeeper.timeTicketAssignedRate * timeKeeper.timeTicketHours
                let timeFortmatTotal = timeTotal != null ? this.decimalPipe.transform(timeTotal, "1.2-2") : '$0.00'
                stackArray.push({
                  //   text: 'Timekeeper Approved Bill Amount' + '$ ' + this.decimalPipe.transform(timeKeeper.approvedBillableAmount, "1.2-2") + ' Time Ticket Amount '+ '$ ' + this.decimalPipe.transform(timeKeeper.timeTicketAmount, "1.2-2"),
                  text: 'Timekeeper ' + timeKeeper.timeTicketName + ' worked ' + timeTickerHours + ' hours at ' + timeKeeperRatePerHour + ' per hour, totaling ' + '$ ' + timeFortmatTotal,
                  alignment: 'center',
                  fontSize: 10,
                  lineHeight: 1.3,
                })
              } else {
                let data = map.get(timeKeeper.timeTicketName);
                if (!data) {
                  map.set(timeKeeper.timeTicketName, timeKeeper);
                } else {
                  timeKeeper.timeTicketHours = timeKeeper.timeTicketHours + data.timeTicketHours;
                  map.set(timeKeeper.timeTicketName, timeKeeper);
                }
              }
            });
            map.forEach(m => {
              stackArray.push({
                text: 'Timekeeper ' + m.timeTicketName + ' worked ' + m.timeTicketHours + ' hours at no charge',
                alignment: 'center',
                fontSize: 10,
                lineHeight: 1.3,
              })
            })
            dd.content.push('\n', {
              stack: stackArray
            }, '\n', { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] }, '\n')
          }
        }

        //Cost detail
        if (res.expenseEntry.length > 0) {
          let bodyArray: any[] = [];
          bodyArray.push([{ text: 'ID', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Date', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Description', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Amount', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }]);
          let total = 0;

          res.expenseEntry.forEach((expense, i) => {
          let positiveExpenseAmount = (expense.expenseAmount < 0 ? expense.expenseAmount * -1 : expense.expenseAmount)
            let stringForm = expense.matterExpenseId.toString();
            var last4Digits = stringForm.substring(stringForm.length - 5, stringForm.length);
            total = total + (expense.expenseAmount != null ? expense.expenseAmount : 0.00);
            let positiveTotal = (total < 0 ? total * -1 : total)
            console.log(positiveTotal)
            bodyArray.push([
              { text: expense.matterExpenseId != null ? last4Digits : '', fontSize: 10, border: [false, false, false, false] },
              { text: this.datePipe.transform(expense.createdOn, 'MM-dd-yyyy', "GMT00:00"), fontSize: 10, border: [false, false, false, false] },
              { text: expense.expenseDescription != null ? expense.expenseDescription : '', fontSize: 10, border: [false, false, false, false] },
              { text: expense.expenseAmount != null ? '$ ' + this.decimalPipe.transform(expense.expenseAmount, "1.2-2") : '$ 0.00', fontSize: 10, border: [false, false, false, false] }
            ])
            if ((i + 1) == res.expenseEntry.length) {
              bodyArray.push([
                { text: '', fontSize: 10, border: [false, false, false, false] },
                { text: '', fontSize: 10, border: [false, false, false, false] },
                { text: '', fontSize: 10, border: [false, false, false, false] },
                { text: '$ ' + this.decimalPipe.transform(total , "1.2-2"), fontSize: 10, border: [false, true, false, false], bold: true }
              ])
            }
          });
          dd.content.push(
            '\n',
            {
              text: 'Costs',
              style: 'header',
              decoration: 'underline',
              alignment: 'center',
              bold: true
            },
            '\n',
            {
              table: {
                widths: [40, 50, 330, 50],
                headerRows: 0,
                body: bodyArray,
                alignment: "center"
              }

            },
            { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] },
            '\n'
          )
        }

        //Payment detail
        if (res.paymentDetail.length > 0) {
          let bodyArray: any[] = [];
          bodyArray.push([{ text: 'Date', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Description', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Amount', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }]);
          let total = 0;
          res.paymentDetail.forEach((payment, i) => {
            total = total + (payment.amount != null ? payment.amount : 0.00);
            bodyArray.push([
              { text: this.datePipe.transform(payment.postingDate, 'MM-dd-yyyy', "GMT00:00"), fontSize: 10, border: [false, false, false, false] },
              { text: payment.description != null ? payment.description : 'Payment Received for '+ res.reportHeader.matterNumber + ' on '+ this.datePipe.transform(payment.postingDate, 'MM-dd-yyyy', "GMT00:00"), fontSize: 10, border: [false, false, false, false] },
              { text: payment.amount != null ? '$ ' + this.decimalPipe.transform(payment.amount, "1.2-2") : '$ 0.00', fontSize: 10, border: [false, false, false, false] }
            ])
            if ((i + 1) == res.paymentDetail.length) {
              bodyArray.push([
                { text: '', fontSize: 10, border: [false, false, false, false] },
                { text: 'Total Payments Received:', fontSize: 10, border: [false, false, false, false], bold: true },
                { text: '$ ' + this.decimalPipe.transform(total, "1.2-2"), fontSize: 10, border: [false, true, false, false], bold: true }
              ])
            }
          });
          dd.content.push(
            '\n',
            {
              text: 'Payment Detail',
              style: 'header',
              alignment: 'center',
              bold: true
            },
            '\n',
            {
              columns: [
                { width: 50, text: '' },
                {
                  width: 300,
                  table: {
                    widths: [100, 200, 50],
                    headerRows: 0,
                    body: bodyArray,
                    alignment: "center"
                  }
                },
                { width: 10, text: '' },
              ]
            },
            { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] },
            '\n'
          )
        }

        //Final Summary
        if (res.finalSummary) {

          dd.content.push(
            {
              stack: [{
                text: 'Current Pre bill Summary',
                alignment: 'center',
                fontSize: 12,
                lineHeight: 1.3,
                bold: true
              }]
            })
            let positiveAdvanceCost = (res.finalSummary.advancedCost != null ? res.finalSummary.advancedCost: '$0.00')
          let obj: any = {
            columns: [
              { width: '*', text: '' },
              {
                width: 'auto',
                table: {
                  body: [
                    [
                      {
                        text: 'Prior Balance :',
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        bold: true,
                        border: [false, false, false, false],
                      },
                      {
                        text: (res.finalSummary.priorBalance != null ? '$ ' + this.decimalPipe.transform(res.finalSummary.priorBalance, "1.2-2") : '$ 0.00'),
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        border: [false, false, false, false]
                      }
                    ],
                    [
                      {
                        text: 'Payments Received :',
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        bold: true,
                        border: [false, false, false, false]
                      },
                      {
                        text: (res.finalSummary.paymentReceived != null ? '$ ' + this.decimalPipe.transform(res.finalSummary.paymentReceived, "1.2-2") : '$ 0.00'),
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        border: [false, false, false, false]
                      }
                    ],
                    [
                      {
                        text: 'Unpaid Prior Balance :',
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        bold: true,
                        border: [false, false, false, false]
                      },
                      {
                        text: (unPaidPriorBalance > 0 ?  '$ ' + this.decimalPipe.transform(unPaidPriorBalance, "1.2-2") : '$ 0.00'),
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        border: [false, true, false, false]
                      }
                    ],
                    [
                      {
                        text: 'Current Fees :',
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        bold: true,
                        border: [false, false, false, false]
                      },
                      {
                        text: (res.finalSummary.currentFees != null ? '$ ' + this.decimalPipe.transform(res.finalSummary.currentFees, "1.2-2") : '$ 0.00'),
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        border: [false, false, false, false]
                      }
                    ],
                    [
                      {
                        text: 'Advanced Costs :',
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        bold: true,
                        border: [false, false, false, false]
                      },
                      {
                        //text: (positiveAdvanceCost != null ? '$ ' + this.decimalPipe.transform(positiveAdvanceCost, "1.2-2") : '$ 0.00'),
                        text:  (res.finalSummary.advancedCost != null ? '$ ' + this.decimalPipe.transform(res.finalSummary.advancedCost, "1.2-2") : '$0.00'),
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        border: [false, false, false, false]
                      }
                    ],
                    [
                      {
                        text: 'TOTAL AMOUNT DUE :',
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        bold: true,
                        border: [false, false, false, false]
                      },
                      {
                        text: '$ ' + this.decimalPipe.transform(totalfinalamount, "1.2-2"),
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        bold: true,
                        border: [false, true, false, true]
                      }
                    ],
                  ],
                  alignment: "center"
                }
              }
            ]
          }
          if (res.finalSummary.paymentReceived != null && res.finalSummary.paymentReceived != 0) {
            obj.columns.push({ width: '*', text: '\n\n     (Last Payment Date : ' + (res.finalSummary.dateOfLastPayment != null ? this.datePipe.transform(res.finalSummary.dateOfLastPayment, 'MM-dd-yyyy', "GMT00:00") : '') + ")", fontSize: 10 })
          } else {
            obj.columns.push({ width: '*', text: '', fontSize: 10 })
          }
          dd.content.push(obj);
        }

        dd.content.push("\n\n",
          {
            stack: [
              {
                text: res.reportHeader.message,
                alignment: 'left',
                fontSize: 11,
                lineHeight: 1.1,
                bold: true
              },
              {
                text: 'Should you have any questions pertaining to this pre bill,',
                alignment: 'center',
                fontSize: 11,
                lineHeight: 1.1,
                bold: true
              }, {
                text: 'do not hesitate to contact us at',
                alignment: 'center',
                fontSize: 11,
                lineHeight: 1.1,
                bold: true
              }, {
                text: 'accounting@montyramirezlaw.com',
                alignment: 'center',
                fontSize: 11,
                lineHeight: 1.1,
                bold: true
              },
            ]
          })

        pdfMake.createPdf(dd).download('Prebill ' + this.pageflow + ' - ' + element.preBillNumber);
        // pdfMake.createPdf(dd).open();
      } else {
        this.toastr.info("No data available", "Pdf Generate");
      }
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }


}
