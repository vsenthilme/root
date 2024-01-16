import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { ClientDocumentService } from "./client-document.service";
import { DocumentNewComponent } from "./document-new/document-new.component";


interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-client-document',
  templateUrl: './client-document.component.html',
  styleUrls: ['./client-document.component.scss']
})
export class ClientDocumentComponent implements OnInit {
  screenid = 1091;


  displayedColumns: string[] = ['select', 'action', 'status', 'documentNo', 'documentUrl', 'attachment', 'documentUrlVersion', 'matterNumber', 'sentby', 'senton', 'receivedon',];

  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
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
    private service: ClientDocumentService, private router: Router,
    public toastr: ToastrService, private route: ActivatedRoute,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
  clientId: any;
  clientName: any;
  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    sessionStorage.setItem('client', this.route.snapshot.params.code);
    this.clientId = this.cs.decrypt(sessionStorage.getItem('client')).code;
    this.clientName = this.cs.decrypt(sessionStorage.getItem('client')).code1;
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
    const dialogRef = this.dialog.open(DocumentNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, clientId: this.clientId, clientName: this.clientName, code: data != 'New' ? this.selection.selected[0].notesNumber : null }
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
  noteTypeIdlist: any[] = [];
  statuslist: any[] = [];
  matterList: any[]=[];
  docnoList: any[]=[];

  selectedItems3: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];

  selectedItems: SelectItem[] = [];
  multiselectmatterListList: any[] = [];
  multimatterListList: any[] = [];

  selectedItems4: SelectItem[] = [];
  multiselecidocnoListList: any[] = [];
  multidocnoListList: any[] = [];
  
  dropdownSettings = {
    singleSelection: false,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };


  getall(excel: boolean = false) {

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.noteTypeId.url,
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.matter.matterNumber.url,
      this.cas.dropdownlist.setup.documentcode.url,
    ]).subscribe((results) => {
      this.noteTypeIdlist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.noteTypeId.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [12, 15, 19, 20, 22, 23].includes(s.key));
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label: x.key + '-' + x.value}))
      this.multiselectstatusList = this.multistatusList;

      this.matterList = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.matter.matterNumber.key);
      this.matterList.forEach((x: { key: string; value: string; }) => this.multimatterListList.push({value: x.key, label: x.key + '-' + x.value}))
      this.multiselectmatterListList = this.multimatterListList;

      this.docnoList = this.cas.foreachlist_searchpage(results[3], this.cas.dropdownlist.setup.documentcode.key);
      this.docnoList.forEach((x: { key: string; value: string; }) => this.multidocnoListList.push({value: x.key, label: x.key + ' / ' + x.value}))
      this.multiselecidocnoListList = this.multidocnoListList;

      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: any[]) => {
        res.forEach((x) => {
          x.noteTypeId = this.noteTypeIdlist.find(y => y.key == x.noteTypeId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
        })
        this.ELEMENT_DATA = res.filter(x => x.clientId == this.clientId);

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
        "Version ": x.documentUrlVersion,
        "Matter No": x.matterNumber,
        'Sent By': x.sentBy,
        "Status  ": x.statusIddes,
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
    this.sub.add(this.service.docuSignDownload(obj.clientId, obj.classId, obj.documentNo).subscribe((res: any) => {
      this.spin.hide();
      this.getall();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  searchStatusList = { statusId: [12, 15, 19, 20, 22, 23] };
  searhform = this.fb.group({
    documentNo: [],
    documentNoFE: [],
    ereceivedOn: [],
    esentOn: [],
    matterNumber: [],
    matterNumberFE: [],
    sentBy: [],
    sreceivedOn: [],
    ssentOn: [],
    statusIdFE: [],
    statusId: []
  });
  Clear() {
    this.reset();
  };

  search() {
    this.searhform.controls.ereceivedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.ereceivedOn.value));
    this.searhform.controls.esentOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.esentOn.value));
    this.searhform.controls.sreceivedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.sreceivedOn.value));
    this.searhform.controls.ssentOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.ssentOn.value));


    if (this.selectedItems3 && this.selectedItems3.length > 0){
      let multistatusList: any[]=[]
      this.selectedItems3.forEach((a: any)=> multistatusList.push(a.id))
      this.searhform.patchValue({statusId: this.selectedItems3 });
    }


    if (this.selectedItems && this.selectedItems.length > 0){
      let multimatterListList: any[]=[]
      this.selectedItems.forEach((a: any)=> multimatterListList.push(a.id))
      this.searhform.patchValue({matterNumber: this.selectedItems });
    }

    if (this.selectedItems4 && this.selectedItems4.length > 0){
      let multidocnoListList: any[]=[]
      this.selectedItems4.forEach((a: any)=> multidocnoListList.push(a.id))
      this.searhform.patchValue({documentNo: this.selectedItems4 });
    }

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
        this.ELEMENT_DATA = res.filter(x => x.clientId == this.clientId);
        ;
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
    this.router.navigate(['main/client/document/' + this.cs.encrypt({ code: data.clientDocumentId, pageflow: 'view' })]);

  }
  open_Upload(data: any): void {
    this.router.navigate(['main/client/document/' + this.cs.encrypt({ code: data.clientDocumentId, pageflow: 'upload' })]);

  } 
}