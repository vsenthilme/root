import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router, ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DeleteComponent } from 'src/app/common-field/dialog_modules/delete/delete.component';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { ExpensesNewComponent } from '../../matters/case-management/expenses/expenses-new/expenses-new.component';
import { MatterExpensesElement, MatterExpensesService } from '../../matters/case-management/expenses/matter-expenses.service';
import { dropdownelement } from '../../setting/business/document-checklist/document-checklist.component';

@Component({
  selector: 'app-matter-expense',
  templateUrl: './matter-expense.component.html',
  styleUrls: ['./matter-expense.component.scss']
})
export class MatterExpenseComponent implements OnInit {

  screenid = 1169;


  expenseTypeList: dropdownelement[] = [{ key: 'Debit', value: 'Debit' }, { key: 'Credit', value: 'Credit' }]

  displayedColumns: string[] = ['select', 'action', 'expenseCode','matterExpenseId','clientid','matterno', 'referenceField5','referenceField1',  'expenseDescription', 'expenseAmount', 'statusIddes', 'referenceField2','createdOn', 'createdBy',];
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  clientList: any;
 // ClientFilter: any;
 // matterdesc: any;
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
 // matterno: any = "";
  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    // sessionStorage.setItem('matter', this.route.snapshot.params.code);
    // this.matterdesc = this.cs.decrypt(sessionStorage.getItem('matter')).code1;
    // this.matterno = this.cs.decrypt(sessionStorage.getItem('matter')).code;
    // this.ClientFilter = { matterNumber: this.matterno };

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
    const dialogRef = this.dialog.open(ExpensesNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    data: { pageflow: data, matter: this.selection.selected[0].matterNumber, code: data != 'New' ? this.selection.selected[0].matterExpenseId : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      // this.getAllListData();
      window.location.reload();
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

  getall(excel: boolean = false) {

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.expenseCode.url,
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.client.clientId.url,
    ]).subscribe((results) => {
      this.expenseCodelist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.expenseCode.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key);
      this.clientList = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.client.clientId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Search({statusId: ["38"]}).subscribe((res: any[]) => {
        console.log(res)
        res.forEach((x) => {
          // x.expenseCode = this.expenseCodelist.find(y => y.key == x.noteTypeId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.clientname = this.clientList.find(y => y.key == x.clientId)?.value;

          if(x.referenceField1 != null && x.referenceField1.startsWith(null)){
           x.referenceField1 = ''
          }
        })
        this.ELEMENT_DATA = res.filter(x => x.deletionIndicator == 0);
        console.log(this.ELEMENT_DATA)
       //this.ELEMENT_DATA = res;

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
        "Matter No": x.matterNumber,
        "Client Name": x.clientId,
        "MatterExpense ID":x.matterExpenseId,
        "Prebill NO":x.referenceField1,
        'Expense Type': x.expenseType,
        "Description": x.expenseDescription,
        "Amount": x.expenseAmount,
        "Status  ": x.statusIddes,
        'Cost Type': x.referenceField5,
        'Created By': x.createdBy,
        'Approved Date': this.cs.dateapi(x.createdOn),
      });

    })
    res.push({
      "Expense Code": '',
      "Matter No": '',
      "Client Name":'',
      'Expense Type': '',
      "Description":'',
      "Amount": this.getBillableAmount(),
      "Status  ": '',
      'Cost Type': '',
      'Created By': '',
      'Approved Date': '',
    });
    this.excel.exportAsExcel(res, "Expenses");
  }
  getAllListData() {
    this.getall();
  }

  searchStatusList = {
    statusId: [37, 38, 34, 46, 29, 56, 51],
  };
  searhform = this.fb.group({
    endCreatedOn: [],
    expenseCode: [],
    expenseType: [],
    matterNumber: [],
    createdBy: [],
    startCreatedOn: [],
    statusId: [],
  });
  Clear() {
    this.reset();
  };

  search() {
 //   this.searhform.controls.matterNumber.patchValue(this.matterno);

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
          // x.expenseCode = this.expenseCodelist.find(y => y.key == x.noteTypeId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.clientname = this.clientList.find(y => y.key == x.clientId)?.value;

          if(x.referenceField1 != null && x.referenceField1.startsWith(null)){
           x.referenceField1 = ''
          }
        })
        this.ELEMENT_DATA = res.filter(x => x.deletionIndicator == 0);
        
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
    return total;
  }


  multipleUpdate(){
    //this.selection.selected.statusId = 37;
    this.selection.selected.forEach((x )=> {
      x.statusId =37  
    })
    this.sub.add(this.service.multipleExpenseupdate(this.selection.selected).subscribe(res => {
      this.toastr.success( " updated successfully!","Notification",{
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
}