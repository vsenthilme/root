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
  CliententityassignementService
} from '../../master/cliententityassignment/cliententityassignement.service';
import {
  OwnershipNewComponent
} from './ownership-new/ownership-new.component';
import {
  OwnershipService
} from './ownership.service';
import {
  ActivatedRoute,
  Router
} from '@angular/router';
import {
  DeleteComponent
} from 'src/app/common-field/dialog_modules/delete/delete.component';

@Component({
  selector: 'app-ownership',
  templateUrl: './ownership.component.html',
  styleUrls: ['./ownership.component.scss']
})
export class OwnershipComponent implements OnInit {

  screenid = 1021;
  public icon = 'expand_more';
  sub = new Subscription();
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  ELEMENT_DATA: any[] = [];
  constructor(public dialog: MatDialog,
    private service: OwnershipService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private router: Router,
    private cas: CommonApiService,
    private excel: ExcelService,
    private route: ActivatedRoute,
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
  search() {
    this.spin.show();
    this.sub.add(this.service.search(this.searhform.getRawValue()).subscribe((res: any[]) => {
      console.log(res)
      this.dataSource = new MatTableDataSource < any > (res);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.spin.hide();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }
  displayedColumns: string[] = ['select', 'action', 'requestId', 'storeName', 'createdBy', 'createdOn'];
  dataSource = new MatTableDataSource < any > (this.ELEMENT_DATA);
  selection = new SelectionModel < any > (true, []);

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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.requestId + 1}`;
  }
  RA: any = {};
  js: any = {}
  ngOnInit(): void {
    let code = this.route.snapshot.params.code;
    this.js = this.cs.decrypt(code);


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
    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
      position: {
        top: '6.5%'
      },
      data: this.selection.selected[0].requestId,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].requestId, this.selection.selected[0].languageId, this.selection.selected[0].companyId);

      }
    });
  }
  deleterecord(id: any, languageId: any, companyId: any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id, languageId, companyId).subscribe((res) => {
      this.toastr.success(id + " request deleted successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide(); 
      this.router.navigate(['/main/controlgroup/transaction/summary'])
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  openDialog2(data: any = 'New'): void {
   
    if (data != 'New') {
      if (this.selection.selected.length == 0) {
        this.toastr.warning("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    }
    let paramdata = "";
    paramdata = this.cs.encrypt({
      pageflow: data,
      code: data != 'New' ? this.selection.selected[0].requestId : null,
      languageId: data != 'New' ? this.selection.selected[0].languageId : null,
      companyId: data != 'New' ? this.selection.selected[0].companyId : null
    });
    this.router.navigate(['/main/controlgroup/transaction/ownershipNew/' + paramdata]);
   
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

  multiselectyseridList: any[] = [];
  multiyseridList: any[] = [];
  multicasecatList: any[] = [];

  multiselectclassList: any[] = [];
  multilanguageList: any[] = [];
  multicompanyList: any[] = [];
  multigrouptypeList: any[] = [];
  multigroupidList: any[] = [];
  multirequestList: any[]=[];
  multistoreList: any[]=[];
  getall(excel: boolean = false) {
    let obj: any = {};
   obj.requestId = [this.js.line.requestId]
    obj.statusId = [1];
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
        companyId: string
      }) => this.multicompanyList.push({
        value: x.companyId,
        label: x.companyId
      }))
      this.multicompanyList = this.cas.removeDuplicatesFromArrayNew(this.multicompanyList);
      res.forEach((x: {
        requestId: string
      }) => this.multirequestList.push({
        value: x.requestId,
        label: x.requestId
      }))
      this.multirequestList = this.cas.removeDuplicatesFromArrayNew(this.multirequestList);
      res.forEach((x: {
        storeId: string,
        storeName: string
      }) => this.multistoreList.push({
        value: x.storeId,
        label: x.storeId + '-' + x.storeName
      }))
      this.multistoreList = this.cas.removeDuplicatesFromArrayNew(this.multistoreList);
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
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }
  downloadexcel() {
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        'Language ID': x.languageId,
        'Company ID': x.companyId,
        "Request ID": x.requestId,
        "Store ID": x.storeId + ' - ' + x.storeName,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "Ownership Change Request");
  }
  getAllListData() {
    this.getall();
  }
  searhform = this.fb.group({
    languageId: [],
    companyId: [],
    storeId: [],
    requestId: [],
    createdBy: [],
    startCreatedOn:[],
    endCreatedOn:[],
  });


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

  validate(element, pageflow){
    console.log(element)
    if(element.groupTypeId == "1002"){
      console.log(element.groupTypeId)
    let paramdata = "";
    paramdata = this.cs.encrypt({ code: this.selection.selected[0], pageflow: 'Brother Sister Template' });
    this.router.navigate(['/main/controlgroup/transaction/brotherSisterRemplate/' + paramdata]);
    }
    if(element.groupTypeId == "1001"){
      console.log(element.groupTypeId)
      let paramdata = "";
      paramdata = this.cs.encrypt({ code: element, pageflow: 'Family Template'});
      console.log(this.selection.selected[0]);
      this.router.navigate(['/main/controlgroup/transaction/familytemplate/' + paramdata]);
      }
      if(element.groupTypeId == "1003"){
        console.log(element.groupTypeId)
        element.statusId=2;
        console.log(element.statusId);
        let paramdata = "";
        paramdata = this.cs.encrypt({line: element, pageflow: pageflow});
    this.router.navigate(['/main/controlgroup/transaction/proposal/' + paramdata]);
        }
  }
}
