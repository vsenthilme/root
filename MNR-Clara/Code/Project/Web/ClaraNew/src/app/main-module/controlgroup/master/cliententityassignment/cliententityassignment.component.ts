import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { SelectItem } from 'primeng/api';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { ControlgroupNewComponent } from '../controlgroup/controlgroup-new/controlgroup-new.component';
import { DeleteComponent } from 'src/app/common-field/dialog_modules/delete/delete.component';
import { CliententityassignementService } from './cliententityassignement.service';
import { CliententityassignmentNewComponent } from './cliententityassignment-new/cliententityassignment-new.component';

@Component({
  selector: 'app-cliententityassignment',
  templateUrl: './cliententityassignment.component.html',
  styleUrls: ['./cliententityassignment.component.scss']
})
export class CliententityassignmentComponent implements OnInit {
  screenid = 1021;
  public icon = 'expand_more';
  sub = new Subscription();
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  ELEMENT_DATA: any[] = [];
  constructor(public dialog: MatDialog,
    private service:CliententityassignementService ,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
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

  displayedColumns: string[] = ['select', 'languageId','companyId','storeName','clientName','statusId', 'createdBy', 'createdOn'];
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);

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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.clientId + 1}`;
  }
  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);

    this.getAllListData();
   // this.StatusList.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.value}))
    this.multiselectstatusList = this.multistatusList;
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
       data: this.selection.selected[0].caseCategoryId,
     });

     dialogRef.afterClosed().subscribe(result => {

       if (result) {
         this.deleterecord(this.selection.selected[0].clientId,this.selection.selected[0].languageId,this.selection.selected[0].companyId,this.selection.selected[0].storeId,this.selection.selected[0].versionNumber);

       }
     });
   }
   deleterecord(id: any,languageId:any,companyId:any,storeId:any,versionNumber:any) {
     this.spin.show();
     this.sub.add(this.service.Delete(id,languageId,companyId,storeId,versionNumber).subscribe((res) => {
       this.toastr.success(id + " deleted successfully!", "Notification", {
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
    const dialogRef = this.dialog.open(CliententityassignmentNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].clientId : null,languageId: data != 'New' ? this.selection.selected[0].languageId : null,companyId: data != 'New' ? this.selection.selected[0].companyId : null,storeId: data != 'New' ? this.selection.selected[0].storeId : null,versionNumber: data != 'New' ? this.selection.selected[0].versionNumber : null }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.getAllListData();
    });
  }

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination

  selectedItems2: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];

  selectedItems3: SelectItem[] = [];
  multiselectyseridList: any[] = [];
  multiyseridList: any[] = [];
  multicasecatList: any[] = [];
    dropdownSettings = {
    singleSelection: false,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };
  multiselectclassList: any[] = [];

  
  downloadexcel() {
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        'Language ID': x.languageId,
        'Company ID':x.companyId,
        "Store Name":x.storeName,
        "Client Name":x.clientName,
        "Status": (x.statusId) == 0 ? 'Active' : 'Inactive',
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "Client Store Assignment");
  }
  getAllListData() {
    this.getall();
  }
  multicompanyList:any[]=[];
  multigrouptypeList:any[]=[];
  multigroupidList:any[]=[]
  multilanguageList:any[]=[];
  getall() {
    let obj: any = {};
    obj.statusId=[0];
    this.spin.show();
    this.sub.add(this.service.search(obj).subscribe((res: any[]) => {
      this.dataSource = new MatTableDataSource<any>(res);
      this.spin.hide();
      res.forEach((x: { languageId: string}) => this.multilanguageList.push({value: x.languageId, label: x.languageId }))
      this.multilanguageList = this.cas.removeDuplicatesFromArrayNew(this.multilanguageList);
      res.forEach((x: { companyId: string,company}) => this.multicompanyList.push({value: x.companyId, label: x.companyId+'-'+'M&R' }))
      this.multicompanyList = this.cas.removeDuplicatesFromArrayNew(this.multicompanyList);
      res.forEach((x: { clientId: string,clientName}) => this.multigrouptypeList.push({value: x.clientId, label: x.clientId+'-'+x.clientName }))
      this.multigrouptypeList = this.cas.removeDuplicatesFromArrayNew(this.multigrouptypeList);
      res.forEach((x: { storeId: string,storeName: string}) => this.multigroupidList.push({value: x.storeId, label: x.storeId +'-'+x.storeName}))
      this.multigroupidList = this.cas.removeDuplicatesFromArrayNew(this.multigroupidList);
      res.forEach((x: { createdBy: string;}) => this.multiyseridList.push({value: x.createdBy, label: x.createdBy}))
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
  searhform = this.fb.group({
   clientId:[],
languageId:[],
storeId:[],
companyId:[],
statusId:[[0],],
createdOn:[],
createdBy:[],
startCreatedOn:[],
endCreatedOn:[],
startCreatedOnFE:[],
endCreatedOnFE:[],
createdOn_from:[],
createdOn_to:[],
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
  Cancel() {
    this.reset();

    this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);

    this.selection = new SelectionModel<any>(true, []);
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





