import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { PhysicalInventoryService } from "../physical-inventory.service";
import { AnnualCreateComponent } from "./annual-create/annual-create.component";
@Component({
  selector: 'app-physical-main',
  templateUrl: './physical-main.component.html',
  styleUrls: ['./physical-main.component.scss']
})
export class PhysicalMainComponent implements OnInit {

  screenid= 3076 ;

  isShowDiv = false;
  public icon = 'expand_more';
  showFloatingButtons: any;
  toggle = true;

  displayedColumns: string[] = ['select', 'actions', 'warehouseId', 'cycleCountNo', 'by', 'countedOn', 'status',];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    public dialog: MatDialog,
    private spin: NgxSpinnerService,
    public cs: CommonService,
    public auth: AuthService,
    public periodicService: PhysicalInventoryService,
    public toastr: ToastrService,
    public router: Router,
  ) { }

  RA: any = {};
  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllPeriodicCountList();
  }

  getAllPeriodicCountList() {
    let obj: any = {};
    obj.warehouseId = [this.auth.warehouseId];
    obj.headerStatusId = [];
    this.spin.show();
    this.periodicService.findPeriodicDataListNew(obj).subscribe( //20-06-23 streaming
      result => {
        this.spin.hide();
        this.dataSource = new MatTableDataSource(result);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error => {
        this.spin.hide();
        this.toastr.error(
          "Error",
          "Notification"
        );
      }
    );
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
    console.log('show:' + this.showFloatingButtons);
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  create(): void {
    const dialogRef = this.dialog.open(AnnualCreateComponent, {
      disableClose: true,
      width: '80%',
      maxWidth: '50%',
      position: { top: '9%', },
    });

    dialogRef.afterClosed().subscribe(result => {
    });
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.no + 1}`;
  }
  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  goTo(type: any, element: any = null) {
     if(this.selection.selected[0].statusId === 78 && type != "Display"){
      this.toastr.error("Stock count confirmed can't be edited", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
  }
    if(type == "count"){
      this.periodicService.findPeriodicDLine({cycleCountNo: element.cycleCountNo, lineStatusId: [72]}).subscribe(res => {
 
        if (this.selection.selected.length == 0) {
          this.toastr.error(
            "Please choose line to edit",
            "Notification"
          );
          return;
        }
        else if(res.length == 0){
            this.toastr.error(
              "Please assign counter first",
              "Notification"
            );
            return;
         
        } 
        else {
          let obj: any = {};
          obj.periodicHeaderData = this.selection.selected[0];
          obj.periodicLine = res;
          obj.type = type;
          let data = this.cs.encrypt(obj);
          this.router.navigate(['/main/cycle-count/periodic-count-confirm', data])
        }
      })
    }
    if(type == "Assign"){
      this.periodicService.findPeriodicDLine({cycleCountNo: element.cycleCountNo, lineStatusId: [70, 72, 73]}).subscribe(res => {
 
        if (this.selection.selected.length == 0) {
          this.toastr.error(
            "Please choose line to edit",
            "Notification"
          );
          return;
        }
        else if(res.length == 0){
            this.toastr.error(
              "No lines found",
              "Notification"
            );
            return;
         
        } 
        else {
          let obj: any = {};
          obj.periodicHeaderData = this.selection.selected[0];
          obj.type = type;
          obj.periodicLine = res;
          let data = this.cs.encrypt(obj);
          this.router.navigate(['/main/cycle-count/periodic-count-confirm', data])
        }
      })
    }
    if(type == "Display"){
      this.periodicService.findPeriodicDLine({cycleCountNo: this.selection.selected[0].cycleCountNo}).subscribe(res => {
        if (this.selection.selected.length == 0) {
          this.toastr.error(
            "Please choose line to edit",
            "Notification"
          );
          return;
        }
        else {
          let obj: any = {};
          obj.periodicHeaderData = this.selection.selected[0];
          obj.type = type;
          obj.periodicLine = res;
          let data = this.cs.encrypt(obj);
          this.router.navigate(['/main/cycle-count/periodic-count-confirm', data])
        }
      });
    }
  }
}
