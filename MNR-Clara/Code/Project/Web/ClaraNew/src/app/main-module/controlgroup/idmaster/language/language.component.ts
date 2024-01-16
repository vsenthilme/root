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
  Table
} from 'primeng/table';
import {
  Subscription
} from 'rxjs';
import {
  DeleteComponent
} from 'src/app/common-field/dialog_modules/delete/delete.component';
import {
  CommonService,
  dropdownelement
} from 'src/app/common-service/common-service.service';
import {
  AuthService
} from 'src/app/core/core';
import {
  LanguageService
} from './language.service';
import {
  LanguageNewComponent
} from './language-new/language-new.component';
import {
  SelectItem
} from 'primeng/api';
import {
  CommonApiService
} from 'src/app/common-service/common-api.service';
import {
  ExcelService
} from 'src/app/common-service/excel.service';
import {
  CaseCategoryService
} from 'src/app/main-module/setting/business/case-category/case-category.service';
import {
  CasecategoryDisplayComponent
} from 'src/app/main-module/setting/business/case-category/casecategory-display/casecategory-display.component';
import { ConfirmComponent } from 'src/app/common-field/dialog_modules/confirm/confirm.component';

@Component({
  selector: 'app-language',
  templateUrl: './language.component.html',
  styleUrls: ['./language.component.scss']
})
export class LanguageComponent implements OnInit {
  screenid = 1035;
  displayedColumns: string[] = ['select', 'languageId', 'languageDescription', 'createdBy', 'createdOn'];
  public icon = 'expand_more';
  sub = new Subscription();
  ELEMENT_DATA: any[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  constructor(public dialog: MatDialog,
    private service: LanguageService,
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
    const dialogRef = this.dialog.open(LanguageNewComponent, {
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
      let obj: any = {}   
      obj.languageId = [this.auth.languageId];
      this.spin.show();
      this.sub.add(this.service.searchStorePartner(obj).subscribe(res => {
        console.log(res.length);
        if (res.length > 0) {
          const dialogRef1 = this.dialog.open(ConfirmComponent, {
            disableClose: true,
            width: '50%',
            maxWidth: '80%',
            position: {
              top: '6.5%'
            },
            data: {
              title: "Delete",
              message: 'The Selected Language Cannot be deleted as it contains associated data in Store Partner Listing ',
              pageflow:"delete"
            }
          });
          this.spin.hide();
          dialogRef1.afterClosed().subscribe(result => {
          if(res.length == 0){
     
            this.deleterecord(this.selection.selected[0].languageId);

          }
      
      
    });
  }
  else{
    this.deleterecord(this.selection.selected[0].languageId);

  }
})
      )
  }
   
  )
   
  }
  deleterecord(id: any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id).subscribe((res) => {
      this.toastr.success(id + " Language Id deleted successfully!", "Notification", {
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
    console.log(data);
    if (data != 'New')
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    const dialogRef = this.dialog.open(LanguageNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: {
        top: '6.5%'
      },
      data: {
        pageflow: data,
        code: data != 'New' ? this.selection.selected[0].languageId : null
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
  paginator: MatPaginator; 
  multilanguageList: any[] = [];
  multiyseridList: any[] = [];
  multiselectyseridList: any[] = [];

  getall() {
    this.multilanguageList = [];
    let obj: any = {};
    this.spin.show();
    this.sub.add(this.service.search(obj).subscribe((res: any[]) => {
         this.dataSource = new MatTableDataSource < any >(res);
        this.spin.hide();
        res.forEach((x: { languageId: string; languageDescription: string, }) => {
            this.multilanguageList.push({
              value: x.languageId,
              label: x.languageId + '-' + x.languageDescription,
            });
        });
        this.multilanguageList = this.cas.removeDuplicatesFromArrayNew(this.multilanguageList);
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
        "Language ID": x.languageId,
        "Language Name": x.languageDescription,
        'Created By': x.createdBy,
        'Created On': this.cs.excel_date(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "Language");
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
    createdBy: [],
    createdOn: [],
    createdOn_from: [],
    createdOn_to: [],
    createdOn_fromFE: [],
    createdOn_toFE: [],
    fromDateString: [, ],
    endDateString: [, ],
    endCreatedOn: [],
    startCreatedOn: [],
    endCreatedOnFE: [],
    startCreatedOnFE: [],
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
      console.log(res);
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
