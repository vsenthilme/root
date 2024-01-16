import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonApiService, dropdownelement } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { GeneralMatterService } from "../../matters/case-management/General/general-matter.service";
import { PaymentPlanService } from "./payment-plan.service";



interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-payment-plan',
  templateUrl: './payment-plan.component.html',
  styleUrls: ['./payment-plan.component.scss']
})
export class PaymentPlanComponent implements OnInit {

  screenid = 1136;
  displayedColumns: string[] = ['select', 'paymentPlanNumber', 'clientId', 'matterNumber', 'quotationNo', 'paymentPlanTotalAmount', 'paymentPlanStartDate', 'noOfInstallment', 'statusIddes'];

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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.quotationNo + 1}`;
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
    private service: PaymentPlanService, private router: Router,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private matterservice: GeneralMatterService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }

  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
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
    if (this.selection.selected[0].statusId == 43) {
      this.toastr.error("Closed Payment plan cannot be deleted.", "Notification", {
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

    this.sub.add(this.service.Delete(id.paymentPlanNumber, id.paymentPlanRevisionNo).subscribe((res) => {
      this.toastr.success(id.paymentPlanNumber + " deleted successfully!", "Notification", {
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
    if (this.selection.selected.length > 0) {
      if (this.selection.selected[0].statusId == 43 && data == 'Edit') {
        this.toastr.error("Closed payment plan cannot be edited.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
      paramdata = this.cs.encrypt({ code: this.selection.selected[0], pageflow: data });
      this.router.navigate(['/main/accounts/paymentplannew/' + paramdata]);
    }
    else {
      paramdata = this.cs.encrypt({ pageflow: data });
      this.router.navigate(['/main/accounts/paymentplannew/' + paramdata]);
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
  paymentPlanlist: any[] = [];
  userTypeList: any[]=[];
  quotationlist: any[]=[];
  matterList: any[]=[];
  
  selectedItems1: SelectItem[] = [];
  multiselectpaymentplanList: any[] = [];
  multipaymentplanList: any[] = [];

  selectedItems4: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];

  
  selectedItems7: SelectItem[] = [];
  multiselectcreatedList: any[] = [];
  multicreatedList: any[] = [];

  selectedItems6: SelectItem[] = [];
  multiselectquotationList: any[] = [];
  multiquotationList: any[] = [];

  selectedItems3: any[] = [];
  multiselectclientList: any[] = [];
  multiclientList: any[] = [];

  selectedItems8: SelectItem[] = [];
  multiselectmatterListList: any[] = [];
  multimatterListList: any[] = [];

  dropdownSettings = {
    singleSelection: false,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };



  getall(excel: boolean = false) {
    this.spin.show();

    this.sub.add(this.matterservice.getAllSearchDropDown().subscribe((data: any) => {
      if (data) {
        data.matterList.forEach((x: any) => this.matterList.push({ value: x.key, label: x.key + '-' + x.value }));
        this.matterList.forEach((x: any) => this.multimatterListList.push({ value: x.value, label: x.label }));
        this.multiselectmatterListList = this.multimatterListList
    
        data.clientNameList.forEach((x: any) => this.clientIdlist.push({ value: x.key, label: x.key + '-' + x.value }));
        this.clientIdlist.forEach((x: any) => this.multiclientList.push({ value: x.value, label: x.label }));
        this.multiselectclientList = this.multiclientList
        

    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.setup.statusId.url,
   // this.cas.dropdownlist.client.clientId.url,
    this.cas.dropdownlist.accounting.paymentPlanNumber.url,
    this.cas.dropdownlist.setup.userId.url,
    this.cas.dropdownlist.accounting.quotationNo.url,
   // this.cas.dropdownlist.matter.matterNumber.url,
    ]).subscribe((results) => {
      this.classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [1, 10, 12, 13, 43].includes(s.key));
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label: x.key + '-' + x.value}))
      this.multiselectstatusList = this.multistatusList;

      // this.clientIdlist = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.client.clientId.key);
      // this.clientIdlist.forEach((x: { key: string; value: string; }) => this.multiclientList.push({value: x.key, label: x.key + ' - ' + x.value}))
      // this.multiselectclientList = this.multiclientList;


      this.paymentPlanlist = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.accounting.paymentPlanNumber.key);
     
      this.paymentPlanlist.forEach((x: { key: string; value: string; }) => this.multipaymentplanList.push({value: x.key, label:  x.value}))
      console.log(this.paymentPlanlist)
      this.multiselectpaymentplanList = this.multipaymentplanList;

      this.userTypeList = this.cas.foreachlist_searchpage(results[3], this.cas.dropdownlist.setup.userId.key);
      this.userTypeList.forEach((x: { key: string; value: string; }) => this.multicreatedList.push({value: x.key, label:  x.value}))
      this.multiselectcreatedList = this.multicreatedList;

      this.quotationlist = this.cas.foreachlist_searchpage(results[4], this.cas.dropdownlist.accounting.quotationNo.key);
      this.quotationlist.forEach((x: { key: string; value: string; }) => this.multiquotationList.push({value: x.key, label: x.value}))
      this.multiselectquotationList = this.multiquotationList;

      // this.matterList = this.cas.foreachlist_searchpage(results[6], this.cas.dropdownlist.matter.matterNumber.key);
      // this.matterList.forEach((x: { key: string; value: string; }) => this.multimatterListList.push({value: x.key, label: x.key + ' / ' + x.value}))
      // this.multiselectmatterListList = this.multimatterListList;


      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: any[]) => {
        if (this.auth.classId != '3')
          this.ELEMENT_DATA = res.filter(x => x.classId === Number(this.auth.classId));
        else
          this.ELEMENT_DATA = res;
        this.ELEMENT_DATA.forEach((x) => {
          x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.clientId = this.clientIdlist.find(y => y.value == x.clientId)?.label;
          x.matterNumber = this.matterList.find(y => y.value == x.matterNumber)?.label;
        })

        if (excel)
          this.excel.exportAsExcel(this.ELEMENT_DATA.sort((a, b) => (a.paymentPlanNumber > b.paymentPlanNumber) ? -1 : 1));
        this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.paymentPlanNumber > b.paymentPlanNumber) ? -1 : 1));
        // this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
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

  }
  console.log(this.matterList)
}, (err) => {
  this.toastr.error(err, "");
}));

    this.spin.hide();

  }
  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Payment Plan No": x.paymentPlanNumber,
        'Client Name': x.clientId,
        "Matter No  ": x.matterNumber,
        "Quote No ": x.quotationNo,
        'Start Date': x.paymentPlanStartDate,
        'Payment Plan Amount': this.cs.dateapi(x.paymentPlanTotalAmount),
        "No of Installments  ": x.noOfInstallment,
        "Installment  Amount  ": x.installmentAmount,
        "Status  ": x.statusIddes,
      });

    })
    this.excel.exportAsExcel(res, "Payment Plan");
  }
  getAllListData() {
    this.getall();
  }

  searchStatusList = { statusId: [1, 12, 43] };
  intakeFormNumberList: dropdownelement[] = [];
  searhform = this.fb.group({

    clientId: [],
    clientIdFE: [],
    createdBy: [],
    createdByFE: [],
    endPaymentPlanDate: [],
    matterNumber: [],
    matterNumberFE: [],
    quotationNo: [],
    quotationNoFE: [],
    paymentPlanNumber: [],
    paymentPlanNumberFE: [],
    startPaymentPlanDate: [],
    statusId: [],
    statusIdFE: [],
  });
  Clear() {
    this.reset();
  };





  search() {
    this.searhform.controls.startPaymentPlanDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.startPaymentPlanDate.value));
    this.searhform.controls.endPaymentPlanDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.endPaymentPlanDate.value));

//     if (this.selectedItems1 && this.selectedItems1.length > 0){
//       let multipaymentplanList: any[]=[]
//       this.selectedItems1.forEach((a: any)=> multipaymentplanList.push(a.id))
//       this.searhform.patchValue({paymentPlanNumber: multipaymentplanList });
//     }

//  if (this.selectedItems4 && this.selectedItems4.length > 0){
//       let multistatusList: any[]=[]
//       this.selectedItems4.forEach((a: any)=> multistatusList.push(a.id))
//       this.searhform.patchValue({statusId: this.selectedItems4 });
//     }

//       if (this.selectedItems7 && this.selectedItems7.length > 0){
//       let multicreatedList: any[]=[]
//       this.selectedItems7.forEach((a: any)=> multicreatedList.push(a.id))
//       this.searhform.patchValue({createdBy: multicreatedList });
//     }

    
//     if (this.selectedItems6 && this.selectedItems6.length > 0){
//       let multiquotationList: any[]=[]
//       this.selectedItems6.forEach((a: any)=> multiquotationList.push(a.id))
//       this.searhform.patchValue({quotationNo: multiquotationList });
//     }

//     if (this.selectedItems3 && this.selectedItems3.length > 0){
//       let multiclientList: any[]=[]
//       this.selectedItems3.forEach((a: any)=> multiclientList.push(a.id))
//       this.searhform.patchValue({clientId: multiclientList });
//     }

//     if (this.selectedItems8 && this.selectedItems8.length > 0){
//       let multimatterListList: any[]=[]
//       this.selectedItems8.forEach((a: any)=> multimatterListList.push(a.id))
//       this.searhform.patchValue({matterNumber: multimatterListList });
//     }


    // this.spin.show();
    // this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    // this.cas.dropdownlist.setup.statusId.url,
    // this.cas.dropdownlist.client.clientId.url,
    // ]).subscribe((results) => {
    //   this.classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
    //   this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key);
    //   this.clientIdlist = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.client.clientId.key);
      this.spin.hide();

      this.spin.show();
      // this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {

      //   this.ELEMENT_DATA.forEach((x) => {
      //     x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
      //     x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
      //     x.clientId = this.clientIdlist.find(y => y.key == x.clientId)?.value;
      //     x.matterNumber = this.matterList.find(y => y.key == x.matterNumber)?.value;
      //   })




      //   this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.paymentPlanNumber > b.paymentPlanNumber) ? -1 : 1));
      //   // this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
      //   this.selection = new SelectionModel<any>(true, []);
      //   this.dataSource.sort = this.sort;
      //   this.dataSource.paginator = this.paginator;
      //   this.spin.hide();
      // }, err => {
      //   this.cs.commonerror(err);
      //   this.spin.hide();
      // }));
    // }, (err) => {
    //   this.toastr.error(err, "");
    // });


    // this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    // this.cas.dropdownlist.setup.statusId.url,
    // this.cas.dropdownlist.client.clientId.url,
    // ]).subscribe((results) => {
    //   this.classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
    //   this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key);
    //   this.clientIdlist = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.client.clientId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {

        this.ELEMENT_DATA = res;
        this.ELEMENT_DATA.forEach((x) => {
          x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.clientId = this.clientIdlist.find(y => y.value == x.clientId)?.label;
          x.matterNumber = this.matterList.find(y => y.value == x.matterNumber)?.label ;
        })

        this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.clientId > b.clientId) ? -1 : 1));
        this.selection = new SelectionModel<any>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    // }, (err) => {
    //   this.toastr.error(err, "");
    // });
    this.spin.hide();


  }
  reset() {
    this.searhform.reset();
  }
  sendtoClient(data: any) {

    this.sub.add(this.service.Update({ statusId: 13 }, data.paymentPlanNumber, data.paymentPlanRevisionNo).subscribe(res => {
      this.toastr.success("Send to Client successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
      this.getall();


    }, err => {

      this.cs.commonerror(err);
      this.spin.hide();

    }));

  }

}
