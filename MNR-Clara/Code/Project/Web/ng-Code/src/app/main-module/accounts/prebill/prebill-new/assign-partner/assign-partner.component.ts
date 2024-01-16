import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { TimekeeperService } from "src/app/main-module/setting/business/timekeeper/timekeeper.service";
import { PrebillService } from "../../prebill.service";
@Component({
  selector: 'app-assign-partner',
  templateUrl: './assign-partner.component.html',
  styleUrls: ['./assign-partner.component.scss']
})
export class AssignPartnerComponent implements OnInit {
  constructor(public dialogRef: MatDialogRef<AssignPartnerComponent>,
    private service: TimekeeperService, private router: Router,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }


  sub = new Subscription();
  ngOnInit(): void {

    this.sub.add(this.service.Getall().subscribe((res: any) => {


      this.dataSource = new MatTableDataSource<any>(res.filter((x: any) => x.userTypeId == 1));
      this.selection = new SelectionModel<any>(true, []);

      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  displayedColumns: string[] = ['select', 'timekeeperCode', 'timekeeperName',];
  dataSource = new MatTableDataSource<any>([]);
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
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
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

  confirm() {

    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row","Notification",{
          timeOut: 2000,
          progressBar: false,
        });
      return;
    }
    this.dialogRef.close(this.selection.selected[0].timekeeperCode);
  }

}

