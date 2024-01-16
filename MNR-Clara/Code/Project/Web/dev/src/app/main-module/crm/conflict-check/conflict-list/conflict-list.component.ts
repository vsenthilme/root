import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { NotesComponent } from "src/app/common-field/notes/notes.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { DisplayComponent } from "../../potential/display/display.component";
import { ConflictCheckService } from "../conflict-check.service";

import { Location } from "@angular/common";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";


export interface PeriodicElement {
  name: string;
  no: number;
  document: number;
  exited: string;
  firstName: string;
  lastName: string;
  email: string;
  data: any;
}

@Component({
  selector: 'app-conflict-list',
  templateUrl: './conflict-list.component.html',
  styleUrls: ['./conflict-list.component.scss']
})
export class ConflictListComponent implements OnInit {
  screenid: 1081 | undefined;
  ELEMENT_DATA: PeriodicElement[] = [];
  showFiller = false;
  animal: string | undefined;
  name: string | undefined;
  sub = new Subscription();

  constructor(public dialog: MatDialog,
    private location: Location,
    private cs: CommonService,
    private router: Router,
    private route: ActivatedRoute,
    private auth: AuthService,
    private service: ConflictCheckService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService, public toastr: ToastrService,
  ) { }




  openDocument(): void {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    let paramdata: any;
    if (this.selection.selected[0].exited == 'Prospective Client') {
      const dialogRef = this.dialog.open(DisplayComponent, {
        disableClose: true,
        width: '95%',
        maxWidth: '95%',
        position: { top: '6.5%' },
        data: { pageflow: 'display', data: { potentialClientId: this.selection.selected[0].document } }
      });

      dialogRef.afterClosed().subscribe(result => {

      });
    }
    else if (this.selection.selected[0].exited == 'Client') {
      paramdata = this.cs.encrypt({ code: this.selection.selected[0].document, pageflow: 'Display' });
      sessionStorage.setItem('client', paramdata);
      this.router.navigate(['/main/client/clientupdate/' + paramdata]);
    }
    else if (this.selection.selected[0].exited == 'Matter') {
      sessionStorage.removeItem('matter');
      paramdata = this.cs.encrypt({ code: this.selection.selected[0].document, pageflow: 'Display' });
      sessionStorage.setItem('matter', paramdata);
      this.router.navigate(['/main/matters/case-management/matter/' + paramdata]);
    }
    else if (this.selection.selected[0].exited == 'Invoice') {
      paramdata = this.cs.encrypt({ code: this.selection.selected[0].data, pageflow: 'Display' });

      this.router.navigate(['/main/accounts/billingdetails/' + paramdata]);
    }
  }
  ngOnInit(): void {

    let code = this.route.snapshot.params.code;
    let js = this.cs.decrypt(code);
    this.run(js);
  }

  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination


  displayedColumns: string[] = ['select', 'exited', 'name', 'firstName', 'lastName', 'email', 'document'];
  dataSource = new MatTableDataSource<PeriodicElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<PeriodicElement>(true, []);

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
  checkboxLabel(row?: PeriodicElement): string {
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

  run(data: any) {


    this.spin.show();
    this.ELEMENT_DATA = [];
    let searchTableNames: any[] = [];

    if (data.searchTableNames == 'ALL') {
      searchTableNames.push('POTENTIALCLIENT');
      searchTableNames.push('CLIENT');
      searchTableNames.push('INVOICE');
      searchTableNames.push('MATTER');
    }
    else
      searchTableNames.push(data.searchTableNames);

    this.sub.add(this.service.Get(data.searchFieldValue, searchTableNames).subscribe(res => {

      res?.potentialClients?.forEach((x: any, i: number) => {
        this.ELEMENT_DATA.push({
          no: i + 1, document: x.potentialClientId,
          name: x.firstNameLastName,
          firstName: x.firstName,
          lastName: x.lastName,
          email: x.emailId,
          exited: 'Prospective Client', data: x
        })
      });

      res?.clientGenerals?.forEach((x: any, i: number) => {
        this.ELEMENT_DATA.push({
          no: i + 1,
          document: x.clientId,
          name: x.firstNameLastName,
          firstName: x.firstName,
          lastName: x.lastName,
          email: x.emailId,
          exited: 'Client', data: x
        })
      });

      res?.matterGenerals?.forEach((x: any, i: number) => {
        this.ELEMENT_DATA.push({
          no: i + 1,
          document: x.matterNumber,
          name: x.referenceField23,
          firstName: x.referenceField21,
          lastName: x.referenceField22,
          email: x.referenceField24,
          exited: 'Matter', data: x
        })
      });


      res?.invoices?.forEach((x: any, i: number) => {
        this.ELEMENT_DATA.push({
          no: i + 1,
          document: x.invoiceNumber,
          name: x.referenceField23,
          firstName: x.referenceField21,
          lastName: x.referenceField22,
          email: x.referenceField24,
          data: x,
          exited: 'Invoice'
        })
      });


      this.spin.hide();

      if (this.ELEMENT_DATA.length == 0) {
        this.cs.commonerror("No records found.");
        this.location.back();


      }
      this.dataSource = new MatTableDataSource<PeriodicElement>(this.ELEMENT_DATA);
      this.selection = new SelectionModel<PeriodicElement>(true, []);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
      this.location.back();
    }));
  }
  back() {
    this.location.back();
  }


}
