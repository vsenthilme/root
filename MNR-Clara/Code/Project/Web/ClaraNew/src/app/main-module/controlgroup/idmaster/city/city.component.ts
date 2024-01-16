import {
  SelectionModel
} from '@angular/cdk/collections';
import {
  Component,
  OnInit,
  ViewChild
} from '@angular/core';
import {
  FormBuilder
} from '@angular/forms';
import {
  MatDialog
} from '@angular/material/dialog';
import {
  MatPaginator
} from '@angular/material/paginator';
import {
  MatSort
} from '@angular/material/sort';
import {
  MatTableDataSource
} from '@angular/material/table';
import {
  NgxSpinnerService
} from 'ngx-spinner';
import {
  ToastrService
} from 'ngx-toastr';
import {
  SelectItem
} from 'primeng/api';
import {
  Subscription
} from 'rxjs';
import {
  CommonApiService
} from 'src/app/common-service/common-api.service';
import {
  CommonService
} from 'src/app/common-service/common-service.service';
import {
  ExcelService
} from 'src/app/common-service/excel.service';
import {
  AuthService
} from 'src/app/core/core';
import {
  CityService
} from './city.service';
import {
  CityNewComponent
} from './city-new/city-new.component';
import {
  DeleteComponent
} from 'src/app/common-field/dialog_modules/delete/delete.component';

@Component({
  selector: 'app-city',
  templateUrl: './city.component.html',
  styleUrls: ['./city.component.scss']
})
export class CityComponent implements OnInit {
  screenid = 1035;
  displayedColumns: string[] = ['select',  'cityId', 'cityName', 'countryIdAndDescription', 'stateIdAndDescription', 'createdBy', 'createdOn'];
  public icon = 'expand_more';
  sub = new Subscription();
  ELEMENT_DATA: any[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  constructor(public dialog: MatDialog,
    private service: CityService,
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
    const dialogRef = this.dialog.open(CityNewComponent, {
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
        this.deleterecord(this.selection.selected[0].cityId, this.selection.selected[0].languageId, this.selection.selected[0].companyId, this.selection.selected[0].countryId, this.selection.selected[0].stateId);
      }
    });
  }
  deleterecord(id: any, languageId: any, companyId, countryId: any, stateId: any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id, languageId, companyId, countryId, stateId).subscribe((res) => {
      this.toastr.success(id + " City Id deleted successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
      this.getAllListData();
      this.selection.clear();
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
    const dialogRef = this.dialog.open(CityNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: {
        top: '6.5%'
      },
      data: {
        pageflow: data,
        code: data != 'New' ? this.selection.selected[0].cityId : null,
        languageId: data != 'New' ? this.selection.selected[0].languageId : null,
        companyId: data != 'New' ? this.selection.selected[0].companyId : null,
        countryId: data != 'New' ? this.selection.selected[0].countryId : null,
        stateId: data != 'New' ? this.selection.selected[0].stateId : null
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      this.getAllListData();
      this.selection.clear();
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
  dropdownSelectcompanyID: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.cgsetup.language.url,
      this.cas.dropdownlist.cgsetup.company.url,
    ]).subscribe((results) => {
      this.multilanguageList = this.cas.foreachlist(results[0], this.cas.dropdownlist.cgsetup.language.key);
      this.multilanguageList.forEach((x: {
        key: string;value: string;
      }) => this.dropdownSelectLanguageID.push({
        value: x.key,
        label: x.value
      }))
      this.multicompanyList = this.cas.foreachlist(results[0], this.cas.dropdownlist.cgsetup.language.key);
      this.multicompanyList.forEach((x: {
        key: string;value: string;
      }) => this.dropdownSelectcompanyID.push({
        value: x.key,
        label: x.value
      }))
      this.spin.hide();
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }
  multicompanyList: any[] = [];
  multicountryList: any[] = [];
  multistateList: any[] = [];
  multicityList: any[] = [];
  getall() {
    this.multicityList=[];
    let obj: any = {};
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
          companyId: string,
          companyIdAndDescription: string
        }) => this.multicompanyList.push({
          value: x.companyId,
          label: x.companyId + '-' + x.companyIdAndDescription
        }))
        this.multicompanyList = this.cas.removeDuplicatesFromArrayNew(this.multicompanyList);
        res.forEach((x: {
          countryId: string,
          countryIdAndDescription: string
        }) => this.multicountryList.push({
          value: x.countryId,
          label: x.countryId + '-' + x.countryIdAndDescription
        }))
        this.multicountryList = this.cas.removeDuplicatesFromArrayNew(this.multicountryList);
        res.forEach((x: {
          stateId: string,
          stateIdAndDescription: string
        }) => this.multistateList.push({
          value: x.stateId,
          label: x.stateId + '-' + x.stateIdAndDescription
        }))
        this.multistateList = this.cas.removeDuplicatesFromArrayNew(this.multistateList);
        res.forEach((x: {
          cityId: string,
          cityName: string
        }) => this.multicityList.push({
          value: x.cityId,
          label: x.cityId + '-' + x.cityName
        }))
        this.multicityList = this.cas.removeDuplicatesFromArrayNew(this.multicityList);
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
        // "Company Name": x.companyIdAndDescription,
        "City ID": x.cityId,
        "City Name": x.cityName,
        "Country Name": x.countryIdAndDescription,
        "State Name": x.stateIdAndDescription,
        'Created By': x.createdBy,
        'Created On': this.cs.excel_date(x.createdOn)
      });
    })
    this.excel.exportAsExcel(res, "City");
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.cityId + 1}`;
  }


  searhform = this.fb.group({
    languageId: [],
    companyId: [],
    countryId: [],
    cityId: [],
    stateId: [],
    startCreatedOn: [],
    startCreatedOnFE: [],
    endCreatedOn: [],
    endCreatedOnFE: [],
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
