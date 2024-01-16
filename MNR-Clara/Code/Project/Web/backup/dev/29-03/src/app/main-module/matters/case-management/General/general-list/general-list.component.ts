import {
  SelectionModel
} from "@angular/cdk/collections";
import {
  Component,
  OnInit,
  ViewChild
} from "@angular/core";
import {
  FormBuilder
} from "@angular/forms";
import {
  MatDialog
} from "@angular/material/dialog";
import {
  MatPaginator,
  PageEvent
} from "@angular/material/paginator";
import {
  MatSort
} from "@angular/material/sort";
import {
  MatTableDataSource
} from "@angular/material/table";
import {
  ActivatedRoute,
  Router
} from "@angular/router";
import {
  NgxSpinnerService
} from "ngx-spinner";
import {
  ToastrService
} from "ngx-toastr";
import {
  Paginator
} from "primeng/paginator";
import {
  Subscription
} from "rxjs";
import {
  DeleteComponent
} from "src/app/common-field/dialog_modules/delete/delete.component";
import {
  CommonApiService,
  dropdownelement
} from "src/app/common-service/common-api.service";
import {
  CommonService
} from "src/app/common-service/common-service.service";
import {
  ExcelService
} from "src/app/common-service/excel.service";
import {
  AuthService
} from "src/app/core/core";
import {
  CaseAssignmentService
} from "../../../case-assignment/case-assignment.service";
import {
  MatterRateService
} from "../../rate-list/matter-rate.service";
import {
  GeneralMatterElement,
  GeneralMatterService
} from "../general-matter.service";
interface SelectItem {
  id: string;
  itemName: string;
}
@Component({
  selector: 'app-general-list',
  templateUrl: './general-list.component.html',
  styleUrls: ['./general-list.component.scss']
})
export class GeneralListComponent implements OnInit {


  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination

  screenid = 1098;
  public icon = 'expand_more';
  sub = new Subscription();

  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;

  isPagematter = true;
  showListByFilter = false;


  searchStatusList = {
    statusId: [26, 27, 28, 29, 30, 36]
  };
  searhform = this.fb.group({
    clientId: [],
    caseCategoryId: [],
    caseInformationNo: [],
    caseOpenedDate1: [],
    caseOpenedDate2: [],
    matterDescription: [],
    matterNumber: [],
    statusId: [,],
    classId: []
  });

  caseCategoryList: any[] = [];
  statusList: any[] = [];
  clientList: any[] = [];
  caseSubCategoryList: any[] = [];
  classList: any[] = [];
  matterList: any[] = [];


  dropdownSettings = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  pageNumber = 0;
  pageSize = 10;
  totalRecords = 0;

  ELEMENT_DATA: GeneralMatterElement[] = [];
  displayedColumns: string[] = ['select', 'classId', 'matterNumber', 'matterDescription', 'clientId', 'caseCategoryId', 'caseSubCategoryId', 'caseInformationNo', 'caseOpenedDate', 'statusId', ];
  dataSource = new MatTableDataSource < GeneralMatterElement > (this.ELEMENT_DATA);
  selection = new SelectionModel < GeneralMatterElement > (true, []);

  RA: any = {};
  classObj: any[] = [];

  constructor(public dialog: MatDialog,
    private service: GeneralMatterService, private router: Router, private route: ActivatedRoute,
    public toastr: ToastrService, private caseservice: CaseAssignmentService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService,
    private matterRate: MatterRateService) {}

  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
    if (this.auth.classId != '3') {
      this.classObj.push(this.auth.classId);
    } else {
      this.classObj.push(1, 2);
    }
   // this.getAllListData(false);
    this.search();
    this.getSearchDropDown();

  }

  pageHandler($event: PageEvent) {
    this.pageNumber = $event.pageIndex;
    this.pageSize = $event.pageSize;
    this.getAllListData(false);
  }

  caseInformationList: any[] = [];
  multicaseInformationList: any[] = [];
  getSearchDropDown() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.caseinfo.caseinfoimmId.url

    ]).subscribe((results) => {
      this.caseInformationList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.caseinfo.caseinfoimmId.key);
      console.log(this.caseInformationList)
      this.caseInformationList.forEach((x: {
        key: string;value: string;
      }) => this.multicaseInformationList.push({
        value: x.key,
        label: x.value
      }))
      console.log(this.multicaseInformationList)
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.show();
    this.sub.add(this.service.getAllSearchDropDown().subscribe((data: any) => {
      if (data) {
        data.clientNameList.forEach(client => {
          this.clientList.push({
            value: client.key,
            label: client.key + '-' + client.value
          });
        });
        data.classList.forEach(calss => {
          this.classList.push({
            value: calss.key,
            label: calss.key + '-' + calss.value
          });
        });
        data.matterList.forEach(matter => {
          this.matterList.push({
            value: matter.key,
            label: matter.key + '-' + matter.value
          });
        });
        data.caseCategoryList.forEach(caseCategory => {
          this.caseCategoryList.push({
            value: caseCategory.key,
            label: caseCategory.value
          });
        });
        data.subCaseCategoryList.forEach(caseSubCategory => {
          this.caseSubCategoryList.push({
            value: caseSubCategory.key,
            label: caseSubCategory.value
          });
        });
        data.statusIdList.forEach(status => {
          this.statusList.push({
            value: status.key,
            label: status.value
          });
        });
      }
    }, (err) => {
      this.toastr.error(err, "");
    }));
  }

  getAllListData(excel: boolean = false) {
    this.sub.add(this.service.getAllMatterByPagination(this.pageNumber, this.pageSize, this.classObj).subscribe((res: any) => {
      this.showListByFilter = false;
      this.spin.hide();
      this.ELEMENT_DATA = res.content;

      // const categories = this.ELEMENT_DATA.map(person => ({
      //   caseInformationNo: person.caseInformationNo,
      // }));
      // const distinctThings = categories.filter(
      //   (thing, i, arr) => arr.findIndex(t => t.caseInformationNo === thing.caseInformationNo) === i
      // );
      // distinctThings.forEach(x => {
      //  this.caseInformationList.push({ value: x.caseInformationNo, label: x.caseInformationNo });
      // });
      if (excel)
        this.excel.exportAsExcel(this.ELEMENT_DATA.sort((a, b) => (a.matterNumber > b.matterNumber) ? -1 : 1));
      this.dataSource = new MatTableDataSource < GeneralMatterElement > (this.ELEMENT_DATA.sort((a, b) => (a.matterNumber > b.matterNumber) ? -1 : 1));
      this.selection = new SelectionModel < GeneralMatterElement > (true, []);
      this.dataSource.sort = this.sort;
      this.totalRecords = res.totalElements;
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  search() {
    this.spin.show()
    // this.searhform.controls.classId.patchValue(this.classObj);
    this.searhform.controls.caseOpenedDate1.patchValue(this.cs.day_callapiSearch(this.searhform.controls.caseOpenedDate1.value));
    this.searhform.controls.caseOpenedDate2.patchValue(this.cs.day_callapiSearch(this.searhform.controls.caseOpenedDate2.value));
    if (this.auth.classId != '3') {
      this.searhform.controls.classId.patchValue([this.auth.classId])
    } 
    this.sub.add(this.service.SearchNew(this.searhform.getRawValue()).subscribe((res: GeneralMatterElement[]) => {
      this.showListByFilter = true;
      // if (this.auth.classId != '3') {
      //   this.ELEMENT_DATA = res.filter(x => x.classId == this.auth.classId);
      // } else {
      //   this.ELEMENT_DATA = res;
      // }
      this.dataSource = new MatTableDataSource < GeneralMatterElement > (res);
      this.selection = new SelectionModel < GeneralMatterElement > (true, []);
      // this.dataSource.sort = this.sort;
      // this.totalRecords = this.ELEMENT_DATA.length;
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  deleteDialog() {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    this.spin.show();
    this.sub.add(this.caseservice.Get(this.selection.selected[0].matterNumber).subscribe((res) => {
      this.spin.hide();
      this.toastr.error("Case Assignment has been already done.", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
    }, err => {
      const dialogRef = this.dialog.open(DeleteComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        position: {
          top: '6.7%',
        },
        data: this.selection.selected[0].matterNumber,
      });
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.deleterecord(this.selection.selected[0].matterNumber);
        }
      });
      this.spin.hide();
    }));
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

    this.sub.add(this.matterRate.getMatterRateByMatterNumber(this.selection.selected[0].matterNumber).subscribe((res) => {
      let timeKeeperCodeList: any[] = []
      res.forEach(element => {
        timeKeeperCodeList.push(element.timeKeeperCode)
      });
      let unMmatchedList: any[] = []
      console.log(timeKeeperCodeList)
      console.log(this.auth.userId)
      timeKeeperCodeList.forEach(x => {
        if (x != this.auth.userId && this.auth.userTypeId != 7) {
          unMmatchedList.push(x)
        }
      })
      
    
   //  if(timeKeeperCodeList.length == unMmatchedList.length){

     if(2 > 4){

      this.toastr.error("You are not authorized to edit others matter", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.cs.notifyOther(true);
      return;
      
     }
     else{
      let paramdata = "";
      if (this.selection.selected.length > 0) {
        sessionStorage.removeItem('matter');
        paramdata = this.cs.encrypt({ code: this.selection.selected[0].matterNumber, code1: this.selection.selected[0].matterDescription, pageflow: data });
        sessionStorage.setItem('matter', paramdata);
        const url = this.router.serializeUrl(
          this.router.createUrlTree(['/main/matters/case-management/matter/' + paramdata])
        );
        
        window.open('#' + url, '_blank');
      }
      else {
        
        sessionStorage.setItem('matter', paramdata);
  
        paramdata = this.cs.encrypt({ pageflow: data });
       
        const url = this.router.serializeUrl(
          this.router.createUrlTree(['/main/matters/case-management/matter/' + paramdata])
        );
        
        window.open('#' + url, '_blank');
      }
      // const dialogRef = this.dialog.open(CasecategoryDisplayComponent, {
      //   disableClose: true,
      //   width: '50%',
      //   maxWidth: '80%',
      //   position: { top: '6.7%', },
      //   data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].clientId : null }
      // });
  
      // dialogRef.afterClosed().subscribe(result => {
  
      //   this.getAllListData();
      // });
     }
    }));


  }

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
  checkboxLabel(row ? : GeneralMatterElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.classId + 1}`;
  }

  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }

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



  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Class": x.classId,
        "Matter No": x.matterNumber,
        'Matter Description': x.matterDescription,
        "Client Name ": x.clientId,
        'Case Category': x.caseCategoryId,
        'Case Sub Category': x.caseSubCategoryId,
        "Case Info Sheet No": x.caseInformationNo,
        'Case Opened Date': this.cs.dateapi(x.caseOpenedDate),
        "Status  ": x.statusId,
      });

    })
    this.excel.exportAsExcel(res, "Matters");
  }

  Clear() {
    this.reset();
  };

  reset() {
    this.searhform.reset();
    this.getAllListData();
  }

  openAssign(): void {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    this.spin.show();
    this.sub.add(this.caseservice.Get(this.selection.selected[0].matterNumber).subscribe((res) => {
      this.spin.hide();
      this.toastr.error("Case Assignment has been already done.", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
    }, err => {
      this.spin.hide();
      let paramdata = this.cs.encrypt({
        code: this.selection.selected[0].matterNumber,
        pageflow: 'New'
      });
      this.router.navigate(['/main/matters/case-assignment/resource-assigment/' + paramdata]);
    }));
    // this.selection.selected[0].id
  }

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }
  }
}
