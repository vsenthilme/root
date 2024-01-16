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
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { TransactionElement } from "../../setting/configuration/transaction-id/transaction.service";
import { AgreementElement, AgreementService } from "./agreement.service";

interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-agreement-snap',
  templateUrl: './agreement-snap.component.html',
  styleUrls: ['./agreement-snap.component.scss']
})
export class AgreementSnapComponent implements OnInit {
  screenid = 1079;

  public icon = 'expand_more';
  sub = new Subscription();
  ELEMENT_DATA: AgreementElement[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  inquiryNoList1:any;
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
  animal: string | undefined;
  name: string | undefined;
  constructor(public dialog: MatDialog,
    private service: AgreementService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private fb: FormBuilder,
    private cs: CommonService,
    private router: Router,
    private cas: CommonApiService,
    private excel: ExcelService,
    private auth: AuthService,) { }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);

    this.getallationslist();
  }

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
  Resend(data: any) {

    this.router.navigate(['main/crm/agreementdocument/' + this.cs.encrypt({ agreementCode: data.agreementCode, potentialClientId: data.potentialClientId, pageflow: 'agreement' })]);


  };

  open_link(data: any): void {

    this.router.navigate(['main/crm/agreementdocument/' + this.cs.encrypt({ agreementCode: data.agreementCode, potentialClientId: data.potentialClientId, pageflow: 'view' })]);

  }

  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination

  selectedItems2: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];
  
  dropdownSettings = {
    singleSelection: false,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  selectedItems4: SelectItem[] = [];
  multiselectInquirynoList: any[] = [];
  multiInquirynoList: any[] = [];


  selectedItems5: SelectItem[] = [];
  multiselectpotentialClientList: any[] = [];
  multipotentialClientList: any[] = [];

  selectedItems6: SelectItem[] = [];
  multiselectagreementList: any[] = [];
  multiagreementList: any[] = [];
  

  selectedItems7: SelectItem[] = [];
  multiseleccasecattList: any[] = [];
  multicasecatList: any[] = [];
  getallationslist() {




    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.setup.statusId.url,
    this.cas.dropdownlist.setup.caseCategoryId.url,
        this.cas.dropdownlist.crm.inquiryNumber.url,
      this.cas.dropdownlist.crm.potentialClientId.url,
      this.cas.dropdownlist.setup.agreementCode.url,]).subscribe((results) => {
      this.classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      this.statusIdList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [11, 12, 15, 19, 20].includes(s.key));
      this.statusIdList.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.value}))
      this.multiselectstatusList = this.multistatusList;

      this.caseCategoryIdList = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.setup.caseCategoryId.key);
      this.caseCategoryIdList.forEach((x: { key: string; value: string; }) => this.multicasecatList.push({value: x.key, label: x.key+ '-' + x.value}))
      this.multiseleccasecattList = this.multicasecatList;
      
      this.inquiryNoList = this.cas.foreachlist_searchpage(results[3], this.cas.dropdownlist.crm.inquiryNumber.key);
      this.inquiryNoList1 = (results[3]);
      this.inquiryNoList.forEach((x: { key: string; value: string; }) => this.multiInquirynoList.push({value: x.key, label:  x.value}))
      this.multiselectInquirynoList = this.multiInquirynoList;

      this.prospectiveNoList = this.cas.foreachlist_searchpage(results[4], this.cas.dropdownlist.crm.potentialClientId.key);
      // this.prospectiveNoList.forEach((x: { key: string; value: string; }) => this.multipotentialClientList.push({value: x.key, label:  x.value}))
      // this.multiselectpotentialClientList = this.multipotentialClientList;
      this.agreementCodeList = this.cas.foreachlist_searchpage(results[5], this.cas.dropdownlist.setup.agreementCode.key);
      this.agreementCodeList.forEach((x: { key: string; value: string; }) => this.multiagreementList.push({value: x.key, label: x.key+ '-' + x.value}))
      this.multiselectagreementList = this.multiagreementList;


      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: AgreementElement[]) => {
        res.forEach((x) => {
          x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
          x.statusId = this.statusIdList.find(y => y.key == x.statusId)?.value;
          x.caseCategoryId = this.caseCategoryIdList.find(y => y.key == x.caseCategoryId)?.value;
          x['clientname'] = this.inquiryNoList1.find(y => y.inquiryNumber == x.inquiryNumber)?.firstName;
          x['clientname1'] = this.inquiryNoList1.find(y => y.inquiryNumber == x.inquiryNumber)?.lastName;
        })

        res.forEach((x: { potentialClientId: string;}) => this.multipotentialClientList.push({value: x.potentialClientId, label: x.potentialClientId}))
        this.multiselectpotentialClientList = this.multipotentialClientList;
          this.multiselectpotentialClientList = this.cas.removeDuplicatesFromArrayNew(this.multiselectpotentialClientList);

        this.ELEMENT_DATA = res;

        this.dataSource = new MatTableDataSource<AgreementElement>(this.ELEMENT_DATA.sort((a, b) => (a.potentialClientId > b.potentialClientId) ? -1 : 1));
        this.selection = new SelectionModel<AgreementElement>(true, []);
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
  classIdList: any[] = [];
  caseCategoryIdList: any[] = [];
  statusIdList: any[] = [];
  inquiryNoList: any[] = [];
  prospectiveNoList: any[] = [];
  casecatList: any[] = [];
  agreementCodeList: any[] = [];

  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Agreement": x.agreementCode,
        'Prospective Client': x.potentialClientId,
        "Inquiry No": x.inquiryNumber,
        "Version  ": x.agreementURLVersion,
        "Case Category  ": x.caseCategoryId,
        'Status': x.statusId,
        "Email  ": x.emailId,
        'Send Date': this.cs.dateapi(x.sentOn),
        'Received Date': this.cs.dateapi(x.receivedOn),
  //      'Resend Date': this.cs.dateapi(x.resentOn),
        // 'Validated Date': this.cs.dateapi(x.validated)
      });

    })
    this.excel.exportAsExcel(res, "Agrement");
  }

  displayedColumns: string[] = ['select', 'status', 'agreementCode', 'potentialClientId', 'inquiryNumber', 'clientname','link', 'agreementURLVersion', 'caseCategoryId', 'emailId', 'sentOn', 'receivedOn',];
  dataSource = new MatTableDataSource<AgreementElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<AgreementElement>(true, []);

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
  checkboxLabel(row?: AgreementElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.agreementCode + 1}`;
  }

    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }

  searchStatusList = {
    statusId: [11, 12, 15, 19, 20]
  };

  searhform = this.fb.group({
    agreementCode: [],
    agreementCodeFE: [],
    caseCategoryId: [],
    caseCategoryIdFE: [],
    eapprovedOn: [],
    ereceivedOn: [],
    eresentOn: [],
    esentOn: [],
    inquiryNumber: [],
    potentialClientId: [],
    inquiryNumberFE: [],
    potentialClientIdFE: [],
    sapprovedOn: [],
    sreceivedOn: [],
    sresentOn: [],
    ssentOn: [],
    statusId: [],
    statusIdFE: [],
  });
  Clear() {
    this.reset();
  };
  search() {

    this.searhform.controls.sapprovedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.sapprovedOn.value));
    this.searhform.controls.sreceivedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.sreceivedOn.value));
    this.searhform.controls.ssentOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.ssentOn.value));
    this.searhform.controls.eapprovedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.eapprovedOn.value));
    this.searhform.controls.ereceivedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.ereceivedOn.value));
    this.searhform.controls.esentOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.esentOn.value));
    this.searhform.controls.sresentOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.sresentOn.value));
    this.searhform.controls.eresentOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.eresentOn.value));

    // if (this.selectedItems2 && this.selectedItems2.length > 0){
    //   let multistatusList: any[]=[]
    //   this.selectedItems2.forEach((a: any)=> multistatusList.push(a.id))
    //   this.searhform.patchValue({statusId: this.selectedItems2 });
    // }


    // if (this.selectedItems5 && this.selectedItems5.length > 0){
    //   let multipotentialClientList: any[]=[]
    //   this.selectedItems5.forEach((a: any)=> multipotentialClientList.push(a.id))
    //   this.searhform.patchValue({potentialClientId: this.selectedItems5 });
    // }

    // if (this.selectedItems4 && this.selectedItems4.length > 0){
    //   let multiInquirynoList: any[]=[]
    //   this.selectedItems4.forEach((a: any)=> multiInquirynoList.push(a.id))
    //   this.searhform.patchValue({agreementCode: this.selectedItems4 });
    // }

    // if (this.selectedItems6 && this.selectedItems6.length > 0){
    //   let multiagreementList: any[]=[]
    //   this.selectedItems6.forEach((a: any)=> multiagreementList.push(a.id))
    //   this.searhform.patchValue({inquiryNumber: this.selectedItems6 });
    // }

    // if (this.selectedItems7 && this.selectedItems7.length > 0){
    //   let multicasecatList: any[]=[]
    //   this.selectedItems7.forEach((a: any)=> multicasecatList.push(a.id))
    //   this.searhform.patchValue({caseCategoryId: this.selectedItems7 });
    // }

    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.setup.statusId.url,
    this.cas.dropdownlist.setup.caseCategoryId.url]).subscribe((results) => {
      this.classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      this.statusIdList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key);
      this.caseCategoryIdList = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.setup.caseCategoryId.key);
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();



    this.spin.show();
    this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: AgreementElement[]) => {
      res.forEach((x) => {
        x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
        x.statusId = this.statusIdList.find(y => y.key == x.statusId)?.value;
        x.caseCategoryId = this.caseCategoryIdList.find(y => y.key == x.caseCategoryId)?.value;
        x['clientname'] = this.inquiryNoList.find(y => y.key == x.inquiryNumber)?.firstName;
        x['clientlastname'] = this.inquiryNoList.find(y => y.key == x.inquiryNumber)?.lastName;
      })
      this.ELEMENT_DATA = res;

      this.dataSource = new MatTableDataSource<AgreementElement>(this.ELEMENT_DATA.sort((a, b) => (a.potentialClientId > b.potentialClientId) ? -1 : 1));
      this.selection = new SelectionModel<AgreementElement>(true, []);
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  reset() {
    this.searhform.reset();
  }
}

