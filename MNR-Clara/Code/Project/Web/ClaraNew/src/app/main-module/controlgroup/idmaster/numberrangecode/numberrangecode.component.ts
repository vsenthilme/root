import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DeleteComponent } from 'src/app/common-field/dialog_modules/delete/delete.component';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { CompanyNewComponent } from '../company/company-new/company-new.component';
import { StatusNewComponent } from '../status/status-new/status-new.component';
import { NumberrangecodeService } from './numberrangecode.service';
import { NumberrangecodeNewComponent } from './numberrangecode-new/numberrangecode-new.component';

@Component({
  selector: 'app-numberrangecode',
  templateUrl: './numberrangecode.component.html',
  styleUrls: ['./numberrangecode.component.scss']
})
export class NumberrangecodeComponent implements OnInit {

  screenid = 1035;
  displayedColumns: string[] = ['select',  'numberRangeCode', 'numberRangeFrom','numberRangeTo','numberRangeObject','numberRangeCurrent','numberRangeStatus', 'createdBy', 'createdOn'];
  public icon = 'expand_more';
  sub = new Subscription();
  ELEMENT_DATA: any[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  constructor(public dialog: MatDialog,
    private service: NumberrangecodeService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) {}
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
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  display(): void {
    const dialogRef = this.dialog.open(CompanyNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: {
        top: '6.5%'
      },
    });
    dialogRef.afterClosed().subscribe(result => {
      this.getAllListData();
    });
  }
  RA: any = {};
  ngOnInit(): void {
    //this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllListData();
  }
  dataSource = new MatTableDataSource < any > (this.ELEMENT_DATA);
  selection = new SelectionModel < any > (true, []);
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
      position: {
        top: '6.5%'
      },
      data: this.selection.selected[0].languageId,
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleterecord(this.selection.selected[0].numberRangeCode, this.selection.selected[0].languageId,this.selection.selected[0].companyId,this.selection.selected[0].numberRangeObject);
      }
    });
  }
  deleterecord(id: any, languageId: any,companyId:any,numberRangeObject:any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id, languageId,companyId,numberRangeObject).subscribe((res) => {
      this.toastr.success(id + " Number Range Code deleted successfully!", "Notification", {
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
  openDialog(data: any = 'new'): void {
    if (data != 'New')
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    const dialogRef = this.dialog.open(NumberrangecodeNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: {
        top: '6.5%'
      },
      data: {
        pageflow: data,
        code: data != 'New' ? this.selection.selected[0].numberRangeCode : null,
        languageId: data != 'New' ? this.selection.selected[0].languageId : null,
        companyId: data != 'New' ? this.selection.selected[0].companyId : null,
        numberRangeObject:data != 'New' ? this.selection.selected[0].numberRangeObject : null,

      }
    });
    dialogRef.afterClosed().subscribe(result => {
      this.getAllListData();
      this.selection.clear()
    });
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }
  }
  @ViewChild(MatSort, {
    static: true
  })
  sort: MatSort;
  @ViewChild(MatPaginator, {
    static: true
  })
  paginator: MatPaginator; // Pagination
  multilanguageList: any[] = [];
  multiyseridList: any[] = [];
  multiselectyseridList: any[] = [];
  dropdownSelectLanguageID: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.cgsetup.language.url,
    ]).subscribe((results) => {
      this.multilanguageList = this.cas.foreachlist(results[0], this.cas.dropdownlist.cgsetup.language.key);
      this.multilanguageList.forEach((x: {
        key: string;value: string;
      }) => this.dropdownSelectLanguageID.push({
        value: x.key,
        label: x.value
      }))
      this.spin.hide();
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }
  multinumberobject:any[]=[];
  multistatusList: any[] = [];
  multicompanyList: any[]=[];
  getall() {
    let obj: any = {};
    obj.numberRangeStatus=[0];
    this.spin.show();
    this.sub.add(this.service.search(obj).subscribe((res: any[]) => {
        this.dataSource = new MatTableDataSource < any > (res);
        this.spin.hide();
        res.forEach((x: {
          languageId: string
        }) => this.multilanguageList.push({
          value: x.languageId,
          label: x.languageId
        }))
        this.multilanguageList = this.cas.removeDuplicatesFromArrayNew(this.multilanguageList);
        res.forEach((x: {
          numberRangeCode: string;numberRangeFrom: string,numberRangeTo:string,
          languageId: string
        }) => this.multistatusList.push({
          value: x.numberRangeCode,
          label: x.numberRangeCode + '-' + x.numberRangeFrom +'-'+x.numberRangeTo
        }))
        res.forEach((x: {
          companyId: string,
          companyIdAndDescription: string
        }) => this.multicompanyList.push({
          value: x.companyId,
          label: x.companyId + '-' + x.companyIdAndDescription
        }))
        this.multicompanyList = this.cas.removeDuplicatesFromArrayNew(this.multicompanyList);

        this.multistatusList = this.cas.removeDuplicatesFromArrayNew(this.multistatusList);
          res.forEach((x: {
            numberRangeObject: string;
        }) => this.multinumberobject.push({
          value: x.numberRangeObject,
          label: x.numberRangeObject 
        }))
        res.forEach((x: {
          createdBy: string;
        }) => this.multiyseridList.push({
          value: x.createdBy,
          label: x.createdBy
        }))
        this.multiselectyseridList = this.multiyseridList;
        this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      }
      , err => {
        this.cs.commonerrorNew(err);
        this.spin.hide();
      }));
  }
  downloadexcel() {
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        // "Language ID": x.languageId,
        // "Company ID":x.companyId,
        "Number Range Code": x.numberRangeCode,
        "Number Range From": x.numberRangeFrom,
        "Number Range To":x.numberRangeTo,
        "Number Range Object":x.numberRangeObject,
        "Number Range Current":x.numberRangeCurrent,
        "Number Range Status":x.numberRangeStatus == 0 ?'Active':'InActive',
        'Created By': x.createdBy,
        'Created On': this.cs.excel_date(x.createdOn)
      });
    })
    this.excel.exportAsExcel(res, "Number Range ");
  }
  getAllListData() {
    this.getall();
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
  checkboxLabel(row ? : any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.adminCost + 1}`;
  }
  searhform = this.fb.group({
    languageId: [],
    numberRangeObject:[],
    createdBy: [],
    numberRangeCode:[],
    createdOn: [],
    statusId: [],
    createdOn_from: [],
    startCreatedOn: [],
    endCreatedOn: [],
    startCreatedOnFE: [],
    endCreatedOnFE: [],
    createdOn_toFE: [],
    fromDateString: [, ],
    endDateString: [, ],
  });
  search() {
    this.spin.show();
    this.searhform.controls.startCreatedOn.patchValue(
      this.cs.dateNewFormat1(this.searhform.controls.startCreatedOnFE.value)
    );
    this.searhform.controls.endCreatedOn.patchValue(
      this.cs.dateNewFormat1(this.searhform.controls.endCreatedOnFE.value)
    );
    this.sub.add(this.service.search(this.searhform.getRawValue()).subscribe((res: any[]) => {
      this.dataSource = new MatTableDataSource < any > (res);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.spin.hide();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }
  Cancel() {
    this.reset();
    this.dataSource = new MatTableDataSource < any > (this.ELEMENT_DATA);
    this.selection = new SelectionModel < any > (true, []);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
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
}


