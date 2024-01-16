import { SelectionModel } from "@angular/cdk/collections";
import { Component, Inject, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { TimeTicketsElement, TimeTicketsService } from "src/app/main-module/matters/case-management/time-tickets/time-tickets.service";

export interface variant {


  no: string;
  dimensions: string;
  timekeeper: string;
  description: string;
  hours: string;
  amount: string;
  billtype: string;
}
const ELEMENT_DATA: variant[] = [
  { no: "Value", dimensions: 'Value', timekeeper: 'dropdown', description: 'dropdown', hours: 'dropdown', amount: 'dropdown', billtype: 'dropdown', },
  { no: "Value", dimensions: 'Value', timekeeper: 'dropdown', description: 'dropdown', hours: 'dropdown', amount: 'dropdown', billtype: 'dropdown', },
  { no: "Value", dimensions: 'Value', timekeeper: 'dropdown', description: 'dropdown', hours: 'dropdown', amount: 'dropdown', billtype: 'dropdown', },
  { no: "Value", dimensions: 'Value', timekeeper: 'dropdown', description: 'dropdown', hours: 'dropdown', amount: 'dropdown', billtype: 'dropdown', },
  { no: "Value", dimensions: 'Value', timekeeper: 'dropdown', description: 'dropdown', hours: 'dropdown', amount: 'dropdown', billtype: 'dropdown', },
  { no: "Value", dimensions: 'Value', timekeeper: 'dropdown', description: 'dropdown', hours: 'dropdown', amount: 'dropdown', billtype: 'dropdown', },
  { no: "Value", dimensions: 'Value', timekeeper: 'dropdown', description: 'dropdown', hours: 'dropdown', amount: 'dropdown', billtype: 'dropdown', },



];
@Component({
  selector: 'app-timetocket-details',
  templateUrl: './timetocket-details.component.html',
  styleUrls: ['./timetocket-details.component.scss']
})
export class TimetocketDetailsComponent implements OnInit {

  ELEMENT_DATA: any[] = [];
  constructor(public dialogRef: MatDialogRef<TimetocketDetailsComponent>,
    @Inject(MAT_DIALOG_DATA,) public data: any[],
    private fb: FormBuilder, private auth: AuthService,
    private service: TimeTicketsService,

    private spin: NgxSpinnerService, public toastr: ToastrService,
    private cas: CommonApiService,
    private cs: CommonService,

  ) { }
  ngOnInit(): void {
    this.getall();


  }
  sub = new Subscription();
  @ViewChild(MatSort)
  sort: MatSort;
  @ViewChild(MatPaginator)
  paginator: MatPaginator; // Pagination
  timekeeperCodelist: any[] = [];
  statuslist: any[] = [];

  getall() {

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.timeKeeperCode.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.timekeeperCodelist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.timeKeeperCode.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key);
      this.spin.hide();
console.log(this.timekeeperCodelist)

      this.data.forEach((x) => {
        x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
      //  x.timeKeeperCode = this.timekeeperCodelist.find(y => y.key == x.timeKeeperCode)?.value;
      })
      this.ELEMENT_DATA = this.data;
console.log(this.ELEMENT_DATA)
      this.dataSource = new MatTableDataSource<TimeTicketsElement>(this.ELEMENT_DATA);
      this.selection = new SelectionModel<TimeTicketsElement>(true, []);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.spin.hide();

    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();

  }

  displayedColumns: string[] = ['no', 'dimensions', 'timekeeper', 'description', 'hours', 'amount', 'billtype','taskCode','activityCode'];
  dataSource = new MatTableDataSource<TimeTicketsElement>([]);
  selection = new SelectionModel<TimeTicketsElement>(true, []);

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
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  /** The label for the checkbox on the passed row */

  getTotalAmount() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.timeTicketAmount != null ? element.timeTicketAmount : 0);
    })
    return total;
  }



}
