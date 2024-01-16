import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router, ActivatedRoute } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonApiService, dropdownelement } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { ExpensesNewComponent } from "./expenses-new/expenses-new.component";
import { MatterExpensesElement, MatterExpensesService } from "./matter-expenses.service";

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-expenses',
  templateUrl: './expenses.component.html',
  styleUrls: ['./expenses.component.scss']
})
export class ExpensesComponent implements OnInit {

  screenid = 1112;


  expenseTypeList: any[] = [{ value: 'Debit', label: 'Debit' }, { value: 'Credit', label: 'Credit' }]

  displayedColumns: string[] = ['select', 'action', 'expenseCode', 'referenceField5', 'expenseType', 'expenseDescription', 'expenseAmount', 'statusIddes','referenceField2','createdOn', 'createdBy','referenceField1',];
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  ClientFilter: any;
  matterdesc: any;
  statuslist1: dropdownelement[];
  multiselectexpenseList: any[] = [];
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.expenseCode + 1}`;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  ELEMENT_DATA: MatterExpensesElement[] = [];
  // displayedColumns: string[] = ['select', 'taskno', 'type', 'creation', 'deadline', 'remainder', 'originatting', 'responsible', 'legal', 'status',];

  dataSource = new MatTableDataSource<MatterExpensesElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<MatterExpensesElement>(true, []);

  constructor(public dialog: MatDialog,
    private service: MatterExpensesService, private router: Router,
    public toastr: ToastrService, private route: ActivatedRoute,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    public auth: AuthService) { }
  matterno: any = "";
  RA: any = {};
  ngOnInit(): void {

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


    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: this.selection.selected[0].matterExpenseId,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].matterExpenseId);

      }
    });
  }
  deleterecord(id: any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id).subscribe((res) => {
      this.toastr.success(id + " deleted successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      this.spin.hide();
      this.getAllListData();
      // window.location.reload();
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
    const dialogRef = this.dialog.open(ExpensesNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, matter: this.cs.decrypt(this.route.snapshot.params.code).code, matterdesc: this.matterdesc, code: data != 'New' ? this.selection.selected[0].matterExpenseId : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      this.getAllListData();
      // window.location.reload();
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
  expenseCodelist: any[] = [];
  statuslist: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];


  dropdownSettings = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  getall(excel: boolean = false) {

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.expenseCode.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.expenseCodelist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.expenseCode.key);
      this.expenseCodelist.forEach((x: { key: string; value: string; }) => this.multiselectexpenseList.push({ value: x.key, label: x.key + '-' + x.value }))

      this.statuslist1 = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [37, 38, 39, 46, 34, 51].includes(s.key));
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({ value: x.key, label: x.value }))
      this.multiselectstatusList = this.multistatusList;
      this.spin.hide();

      this.spin.show();
      console.log()
     // this.sub.add(this.service.Getall().subscribe((res: any[]) => {
       this.sub.add(this.service.Search({matterNumber : this.matterno}).subscribe((res: any[]) => {
        res.forEach((x) => {
          // x.expenseCode = this.expenseCodelist.find(y => y.key == x.noteTypeId)?.value;
          x.statusIddes = this.statuslist1.find(y => y.key == x.statusId)?.value;

       
        //  x.referenceField2 = this.cs.matterApiSearch(x.referenceField2)

          if(x.referenceField1 != null && x.referenceField1.includes("null")){
            x.referenceField1 = '';
          }

          if(x.expenseType == "Credit" && x.expenseAmount < 0){
           x.expenseAmount = -(x.expenseAmount) 
          }
        })
        this.ELEMENT_DATA = res.filter(x => x.deletionIndicator == 0);

        if (excel)
          this.excel.exportAsExcel(res, "Expenses");
        this.dataSource = new MatTableDataSource<MatterExpensesElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<MatterExpensesElement>(true, []);
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
        "Expense Code": x.expenseCode,
        'Expense Type': x.expenseType,
        "Description": x.expenseDescription,
        "Amount": x.expenseAmount ? '$' + x.expenseAmount : '$0.00',
        "Status  ": x.statusIddes,
        'Cost Type': x.referenceField1,
        'Created By': x.createdBy,
        'Approved Date': this.cs.dateapi(x.createdOn),
      });

    })
    res.push({
      "Expense Code": '',
      'Expense Type': '',
      "Description": '',
      "Amount": this.getBillableAmount() ? '$' + this.getBillableAmount() : '$0.00',
      "Status  ":  '',
      'Cost Type':  '',
      'Created By':  '',
      'Approved Date':  '',
    });
    this.excel.exportAsExcel(res, "Expenses");
  }
  getAllListData() {
    this.getall();
  }

  searchStatusList = {
    statusId: [38, 39]
  };
  searhform = this.fb.group({
    endCreatedOn: [],
    expenseCode: [],
    expenseType: [],
    matterNumber: [this.matterno],
    createdBy: [],
    startCreatedOn: [],
    statusId: [],
    statusIdFE: [],
  });
  Clear() {
    this.reset();
  };

  search() {


    // // if (this.selectedItems2 && this.selectedItems2.length > 0) {
    // //   let multistatusList: any[] = []
    // //   this.selectedItems2.forEach((a: any) => multistatusList.push(a.id))
    // //   this.searhform.patchValue({ statusId: multistatusList });
    // // }

    this.searhform.controls.matterNumber.patchValue(this.matterno);

    this.searhform.controls.endCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.endCreatedOn.value));
    this.searhform.controls.startCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.startCreatedOn.value));


    this.spin.show();
    this.cas.getalldropdownlist([
      // this.cas.dropdownlist.setup.expenseCode.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      // this.expenseCodelist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.expenseCode.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {

        res.forEach((x) => {
          // x.noteTypeId = this.noteTypeIdlist.find(y => y.key == x.noteTypeId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
          if(x.referenceField1 != null && x.referenceField1.includes("null")){
            console.log(x.referenceField1)
            x.referenceField1 = '';
          }

          if(x.expenseType == "Credit" && x.expenseAmount < 0){
            x.expenseAmount = -(x.expenseAmount) 
           }
           
        })
        this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno && x.deletionIndicator == 0);
        this.dataSource = new MatTableDataSource<MatterExpensesElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<MatterExpensesElement>(true, []);
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
  reset() {
    this.searhform.reset();
  }

  post(data: any) {
    data.statusId = 37;
    if(data.expenseType == "Credit"){
      data.expenseAmount = -(data.expenseAmount)
    }
    this.sub.add(this.service.Update(data, data.matterExpenseId).subscribe(res => {
      this.toastr.success(res.expenseCode + " posted successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
      this.getAllListData();
    }, err => {

      this.cs.commonerror(err);
      this.spin.hide();

    }));
  }

  unpost(data: any) {
    data.statusId = 38;
    if(data.expenseType == "Credit"){
      data.expenseAmount = -(data.expenseAmount)
    }
    this.sub.add(this.service.Update(data, data.matterExpenseId).subscribe(res => {
      this.toastr.success(res.expenseCode + " posted successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
      this.getAllListData();
    }, err => {

      this.cs.commonerror(err);
      this.spin.hide();

    }));
  }

  getBillableAmount() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.expenseAmount != null ? element.expenseAmount : 0);
    })
    return (Math.round(total * 100) / 100);
  }
}