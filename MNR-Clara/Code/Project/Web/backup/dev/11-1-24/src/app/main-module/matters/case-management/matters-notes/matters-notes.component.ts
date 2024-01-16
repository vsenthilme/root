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
import { MatternotesNewComponent } from "./matternotes-new/matternotes-new.component";
import { MattersNotesElement, MattersNotesService } from "./matters-notes.service";

interface SelectItem {
  id: string;
  itemName: string;
}



@Component({
  selector: 'app-matters-notes',
  templateUrl: './matters-notes.component.html',
  styleUrls: ['./matters-notes.component.scss']
})
export class MattersNotesComponent implements OnInit {
  screenid = 1100;
  public icon = 'expand_more';

  displayedColumns: string[] = ['select', 'notesNumber', 'noteTypeId', 'notesDescription', 'createdBy', 'createdOn', 'statusId'];

  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  ClientFilter: any;
  clientName: any;
  matterdesc: any;
  noteTypeIdlist1: any;
  noteNolist: any;
  notenolist: any;
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
  checkboxLabel(row?: MattersNotesElement): string {
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
  ELEMENT_DATA: MattersNotesElement[] = [];
  // displayedColumns: string[] = ['select', 'taskno', 'type', 'creation', 'deadline', 'remainder', 'originatting', 'responsible', 'legal', 'status',];

  dataSource = new MatTableDataSource<MattersNotesElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<MattersNotesElement>(true, []);

  constructor(public dialog: MatDialog,
    private service: MattersNotesService, private router: Router,
    public toastr: ToastrService, private route: ActivatedRoute,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
  matterno: any;
  RA: any = {};
  ngOnInit(): void {
  this.code = (this.cs.decrypt(this.route.snapshot.params.code));
    this.RA = this.auth.getRoleAccess(this.screenid);
    sessionStorage.setItem('matter', this.route.snapshot.params.code);
    this.matterdesc = this.cs.decrypt(sessionStorage.getItem('matter')).code1;
    this.matterno = this.cs.decrypt(sessionStorage.getItem('matter')).code;
    this.ClientFilter = { matterNumber: this.matterno };
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
  reload() { window.location.reload() }
  deleterecord(id: any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id).subscribe((res) => {
      this.toastr.success(id + " deleted successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      this.spin.hide(); // this.getAllListData();
      window.location.reload()
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
    const dialogRef = this.dialog.open(MatternotesNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, matter: this.cs.decrypt(this.route.snapshot.params.code).code, matterdesc: this.matterdesc, code: data != 'New' ? this.selection.selected[0].notesNumber : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      //this.getAllListData();
      window.location.reload()
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
  multiNoteList: any[] = [];

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
      this.cas.dropdownlist.matter.notesNumber.url,
    ]).subscribe((results) => {
      this.noteTypeIdlist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.noteTypeId.key, {  matterNumber: this.matterno});
      this.noteTypeIdlist.forEach((x: { key: string; value: string; }) => this.multiNoteList.push({value: x.key, label:  x.key + '-' + x.value}))
      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [18, 21].includes(s.key));
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.key + '-' + x.value}))
    this.multiselectstatusList = this.multistatusList;
    this.noteNolist = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.matter.notesNumber.key, {  matterNumber: this.matterno});
    this.noteNolist.forEach((x: { key: string; value: string; }) => this.multiNoteList.push({value: x.key, label:  x.value}))
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: MattersNotesElement[]) => {
        res.forEach((x) => {
          x.noteTypeId = this.noteTypeIdlist.find(y => y.key == x.noteTypeId)?.value;
          x.statusId = this.statuslist.find(y => y.key == x.statusId)?.value;
        })
        this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno);

        if (excel)
          this.excel.exportAsExcel(res, "Notes");
        this.dataSource = new MatTableDataSource<MattersNotesElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<MattersNotesElement>(true, []);
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
        "Description": x.notesDescription,
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
    //noteText: [],
    noteTypeId: [],
    notesNumber: [],
    startCreatedOn: [],
    statusId: [],
    statusIdFE: [],
  });
  Clear() {
    this.reset();
  };

  search() {
    
  // if (this.selectedItems2 && this.selectedItems2.length > 0){
  //   let multistatusList: any[]=[]
  //   this.selectedItems2.forEach((a: any)=> multistatusList.push(a.id))
  //   this.searhform.patchValue({statusId: multistatusList });
  // }


    this.searhform.controls.endCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.endCreatedOn.value));
    this.searhform.controls.startCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.startCreatedOn.value));


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
        this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno && x.deletionIndicator == 0);
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