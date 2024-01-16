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
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { ClientDocumentService } from "src/app/main-module/client/client-document/client-document.service";
import { DocumentNEWComponent } from "./document-new/document-new.component";
import { MatterDocumetService } from "./matter-documet.service";

interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-matter-document',
  templateUrl: './matter-document.component.html',
  styleUrls: ['./matter-document.component.scss']
})
export class MatterDocumentComponent implements OnInit {

  screenid = 1104;

  displayedColumns: string[] = ['select', 'action', 'status', 'documentNo', 'documentUrl', 'attachment', 'documentUrlVersion', 'matterNumber', 'sentby', 'senton', 'receivedon',];

  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
  matterdesc: any;
  multidocumentNoList: any;
  multiselectdocumentNoList: any;
  code: any;
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.notesNumber + 1}`;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  ELEMENT_DATA: any[] = [];
  // displayedColumns: string[] = ['select', 'taskno', 'type', 'creation', 'deadline', 'remainder', 'originatting', 'responsible', 'legal', 'status',];

  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);

  constructor(public dialog: MatDialog,
    private service: MatterDocumetService, private router: Router,
    public toastr: ToastrService, private route: ActivatedRoute,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
  matterNumber: any;
  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    this.code = (this.cs.decrypt(this.route.snapshot.params.code));
    sessionStorage.setItem('matter', this.route.snapshot.params.code);
    this.matterdesc = this.cs.decrypt(sessionStorage.getItem('matter')).code1;
    this.matterNumber = this.cs.decrypt(sessionStorage.getItem('matter')).code;
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
      data: this.selection.selected[0].notesNumber,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].notesNumber);

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

      this.spin.hide();// this.getAllListData();
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
    const dialogRef = this.dialog.open(DocumentNEWComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, matterNumber: this.matterNumber, matterdesc: this.matterdesc, code: data != 'New' ? this.selection.selected[0].notesNumber : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      //this.getAllListData();
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
  noteTypeIdlist: any[] = [];
  statuslist: any[] = [];

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



  selectedItems3: SelectItem[] = [];
  multiselectyseridList: any[] = [];
  multiyseridList: any[] = [];

  getall(excel: boolean = false) {

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.noteTypeId.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.noteTypeIdlist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.noteTypeId.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [12, 15, 19, 20, 22, 23].includes(s.key));
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.value}))
    this.multiselectstatusList = this.multistatusList;
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Search({matterNumber: [this.matterNumber]}).subscribe((res: any[]) => {
        res.forEach((x) => {
          x.noteTypeId = this.noteTypeIdlist.find(y => y.key == x.noteTypeId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
        })
        res.forEach((x: { sentBy: string;}) => this.multiyseridList.push({value: x.sentBy, label: x.sentBy}))
        this.multiselectyseridList = this.multiyseridList;
          this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);

          // res.forEach((x: { documentNo: string;}) => this.multidocumentNoList.push({value: x.documentNo, label: x.documentNo}))
          // this.multiselectdocumentNoList = this.multidocumentNoList;
          //   this.multiselectdocumentNoList = this.cas.removeDuplicatesFromArrayNew(this.multidocumentNoList);


          this.ELEMENT_DATA = res.filter((x: any) => x.referenceField5 != "Client Portal" && x.referenceField2 != 'CLIENTPORTAL');
        //this.ELEMENT_DATA = result.filter(x => x.matterNumber == this.matterNumber);


        if (excel)
          this.excel.exportAsExcel(res, "Document");
        this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
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
    this.spin.hide();

  }
  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Document No": x.documentNo,
        'File Name': x.documentUrl,
        "Version": x.documentUrlVersion,
        'Case No': x.matterNumber,
        "Status  ": x.statusId,
        'Sent By': x.sentBy,
        'Sent On': this.cs.dateapi(x.sentOn),
        'Received On': this.cs.dateapi(x.receivedOn),
      });

    })
    this.excel.exportAsExcel(res, "Document");
  }
  getAllListData() {
    this.getall();
  }


  DocSign_check_update(obj: any) {

    this.spin.show();
    this.sub.add(this.service.docuSignDownload(obj.matterNumber, obj.classId, obj.documentNo).subscribe((res: any) => {
      this.spin.hide();
      this.getall();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));

  }

  searchStatusList = {
    statusId: [12, 15, 19, 20, 22, 23
    ]
  };
  searhform = this.fb.group({
    documentNo: [],
    ereceivedOn: [],
    esentOn: [],
    sentByFE: [],
    sentBy: [],
    sreceivedOn: [],
    ssentOn: [],
    statusId: [],
    statusIdFE: []
  });
  Clear() {
    this.reset();
  };

  search() {
    

    
 if (this.selectedItems3 && this.selectedItems3.length > 0){
  let multiyseridList: any[]=[]
  this.selectedItems3.forEach((a: any)=> multiyseridList.push(a.id))
  this.searhform.patchValue({sentBy: multiyseridList });
}

  // if (this.selectedItems2 && this.selectedItems2.length > 0){
  //   let multistatusList: any[]=[]
  //   this.selectedItems2.forEach((a: any)=> multistatusList.push(a.id))
  //   this.searhform.patchValue({statusId: multistatusList });
  // }
    this.searhform.controls.ereceivedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.ereceivedOn.value));
    this.searhform.controls.esentOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.esentOn.value));
    this.searhform.controls.sreceivedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.sreceivedOn.value));
    this.searhform.controls.ssentOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.ssentOn.value));

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.noteTypeId.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.noteTypeIdlist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.noteTypeId.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {
        res.forEach((x) => {
          x.noteTypeId = this.noteTypeIdlist.find(y => y.key == x.noteTypeId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
        })
        this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterNumber);


        this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
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
    this.spin.hide();

  }
  reset() {
    this.searhform.reset();
  }

  open_link(data: any): void {
    this.router.navigate(['main/matters/case-management/document_template/' + 'DOC_SIGN/' + this.cs.encrypt({ code: data.matterDocumentId, pageflow: 'view' })]);

  }
  open_Upload(data: any): void {
    this.router.navigate(['/main/matters/case-management/document_template/' + 'DOC_SIGN/' +  this.cs.encrypt({ code: data.matterDocumentId, pageflow: 'upload' })]);

  }
}