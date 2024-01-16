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
import { ClientNotesElement, ClientNotesService } from "../client-notes.service";
import { NotesNewComponent } from "./notes-new/notes-new.component";


interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-notes-list',
  templateUrl: './notes-list.component.html',
  styleUrls: ['./notes-list.component.scss']
})
export class NotesListComponent implements OnInit {
  screenid = 1086;
  public icon = 'expand_more';

  displayedColumns: string[] = ['select', 'notesNumber', 'noteTypeId', 'description','createdBy', 'createdOn', 'statusId'];

  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
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
  checkboxLabel(row?: ClientNotesElement): string {
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
  ELEMENT_DATA: ClientNotesElement[] = [];
  // displayedColumns: string[] = ['select', 'taskno', 'type', 'creation', 'deadline', 'remainder', 'originatting', 'responsible', 'legal', 'status',];

  dataSource = new MatTableDataSource<ClientNotesElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<ClientNotesElement>(true, []);

  constructor(public dialog: MatDialog,
    private service: ClientNotesService, private router: Router,
    public toastr: ToastrService, private route: ActivatedRoute,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
  clientId: any;
  clientName: any;
  ClientFilter: any;

  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    sessionStorage.setItem('client', this.route.snapshot.params.code);
    this.clientName = this.cs.decrypt(sessionStorage.getItem('client')).code1;
    this.clientId = this.cs.decrypt(sessionStorage.getItem('client')).code;
    this.ClientFilter = { clientId: this.clientId };
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
    const dialogRef = this.dialog.open(NotesNewComponent, {
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


  selectedItems: SelectItem[] = [];
  multiselectnotenoList: any[] = [];
  multinotenoList: any[] = [];

  

  selectedItems2: SelectItem[] = [];
  multiselectnotetypeList: any[] = [];
  multinotetypeList: any[] = [];

  selectedItems3: SelectItem[] = [];
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



  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination
  noteTypeIdlist: any[] = [];
  statuslist: any[] = [];

  getall(excel: boolean = false) {

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.noteTypeId.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.noteTypeIdlist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.noteTypeId.key,);
      this.noteTypeIdlist.forEach((x: { key: string; value: string; }) => this.multinotetypeList.push({value: x.key, label:  x.value}))
      this.multiselectnotetypeList = this.multinotetypeList;

      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [18, 21].includes(s.key));
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label: x.key + '-' + x.value}))
      this.multiselectstatusList = this.multistatusList;
      
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: ClientNotesElement[]) => {
       


        res.forEach((x) => {
          x.noteTypeId = this.noteTypeIdlist.find(y => y.key == x.noteTypeId)?.value;
          x.statusId = this.statuslist.find(y => y.key == x.statusId)?.value;
        })
        this.ELEMENT_DATA = res.filter(x => x.clientId == this.clientId);

        this.ELEMENT_DATA.forEach((x: { notesNumber: string;}) => this.multinotenoList.push({value: x.notesNumber, label:  x.notesNumber}))
        this.multiselectnotenoList = this.multinotenoList;

        if (excel)
          this.excel.exportAsExcel(res, "Notes");
        this.dataSource = new MatTableDataSource<ClientNotesElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<ClientNotesElement>(true, []);
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
        "Note No": x.notesNumber,
        'Type': x.noteTypeId,
        "Description ": x.noteText,
        "matterNumber": x.matterNumber,
        'Created By': x.createdBy,
        "Status  ": x.statusId,
        'Created Date': this.cs.dateapi(x.createdOn),
      });

    })
    this.excel.exportAsExcel(res, "Notes");
  }
  getAllListData() {
    this.getall();
  }


  searchStatusList = {
    statusId: [18, 21
    ]
  };
  searhform = this.fb.group({
    endCreatedOn: [],
    matterNumber: [],
    noteTypeId: [],
    noteTypeIdFE: [],
    notesNumber: [],
    notesNumberFE: [],
    startCreatedOn: [],
    statusId: [],
    statusIdFE: [],
  });
  Clear() {
    this.reset();
  };

  search() {
    this.searhform.controls.endCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.endCreatedOn.value));
    this.searhform.controls.startCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.startCreatedOn.value));


    
    if (this.selectedItems && this.selectedItems.length > 0){
      let multinotenoList: any[]=[]
      this.selectedItems.forEach((a: any)=> multinotenoList.push(a.id))
      this.searhform.patchValue({notesNumber: this.selectedItems });
    }
    if (this.selectedItems2 && this.selectedItems2.length > 0){
      let multinotetypeList: any[]=[]
      this.selectedItems2.forEach((a: any)=> multinotetypeList.push(a.id))
      this.searhform.patchValue({noteTypeId: this.selectedItems2 });
    }
    if (this.selectedItems3 && this.selectedItems3.length > 0){
      let multistatusList: any[]=[]
      this.selectedItems3.forEach((a: any)=> multistatusList.push(a.id))
      this.searhform.patchValue({statusId: this.selectedItems3 });
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
          x.statusId = this.statuslist.find(y => y.key == x.statusId)?.value;
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
}