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
import { AnnualCreateComponent } from "../physical-main/annual-create/annual-create.component";
@Component({
  selector: 'app-variant-annual',
  templateUrl: './variant-annual.component.html',
  styleUrls: ['./variant-annual.component.scss']
})
export class VariantAnnualComponent implements OnInit {
  screenid: 1076 | undefined;

  isShowDiv = false;
  public icon = 'expand_more';
  showFloatingButtons: any;
  toggle = true;

  displayedColumns: string[] = ['select', 'warehouseId', 'cycleCountNo', 'by', 'countedOn', 'status',];
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

  ngOnInit(): void {
    this.getAllPeriodicCountList();
  }

  getAllPeriodicCountList() {
    let obj: any = {};
    obj.warehouseId = [this.auth.warehouseId];
    obj.headerStatusId = [73, 74, 78];
    this.spin.show();
    this.periodicService.findPeriodicDataList(obj).subscribe(
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
      console.log('The dialog was closed');
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

  goTo(pageFlow: any = 'Edit',) {

    if (this.selection.selected.length == 0) {
      this.toastr.error(
        "Please choose line to edit",
        "Notification"
      );
      return;
    }
    else if(this.selection.selected[0].statusId === 78 && pageFlow == 'Edit'){
        this.toastr.error("Stock count confirmed can't be edited", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
    }
   else {
    console.log(pageFlow)
    let obj: any = {};
     obj.periodicHeaderData = this.selection.selected[0];
      obj.type = pageFlow;
      let data = this.cs.encrypt(obj);
      this.router.navigate(['/main/cycle-count/variant-annual-edit', data])
    }
  }
}
