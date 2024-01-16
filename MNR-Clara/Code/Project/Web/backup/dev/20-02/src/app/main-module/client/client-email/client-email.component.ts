import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { AuthService } from "src/app/core/core";
import { EmailNewComponent } from "./email-new/email-new.component";

export interface PeriodicElement {
  from: string;
  type: string;
  to: string;
  cc: string;
  subject: string;
  body: string;
  status: string;
  date: string;
}
const ELEMENT_DATA: PeriodicElement[] = [
  { from: "Crodriguez@montyramirezlaw.com", type: 'Outgoing', to: 'carlos091971@yahoo.com', cc: '', subject: '10001-04', date: '01-07-2022 10:35:42 AM', status: 'status', body: 'Test', },
  { from: "Crodriguez@montyramirezlaw.com", type: 'Outgoing', to: 'carlos091971@yahoo.com', cc: '', subject: '10001-04', date: '01-07-2022 10:35:42 AM', status: 'status', body: 'Test', },
  { from: "Crodriguez@montyramirezlaw.com", type: 'Outgoing', to: 'carlos091971@yahoo.com', cc: '', subject: '10001-04', date: '01-07-2022 10:35:42 AM', status: 'status', body: 'Test', },
  { from: "Crodriguez@montyramirezlaw.com", type: 'Outgoing', to: 'carlos091971@yahoo.com', cc: '', subject: '10001-04', date: '01-07-2022 10:35:42 AM', status: 'status', body: 'Test', },
  { from: "Crodriguez@montyramirezlaw.com", type: 'Outgoing', to: 'carlos091971@yahoo.com', cc: '', subject: '10001-04', date: '01-07-2022 10:35:42 AM', status: 'status', body: 'Test', },
  { from: "Crodriguez@montyramirezlaw.com", type: 'Outgoing', to: 'carlos091971@yahoo.com', cc: '', subject: '10001-04', date: '01-07-2022 10:35:42 AM', status: 'status', body: 'Test', },
  { from: "Crodriguez@montyramirezlaw.com", type: 'Outgoing', to: 'carlos091971@yahoo.com', cc: '', subject: '10001-04', date: '01-07-2022 10:35:42 AM', status: 'status', body: 'Test', },
  { from: "Crodriguez@montyramirezlaw.com", type: 'Outgoing', to: 'carlos091971@yahoo.com', cc: '', subject: '10001-04', date: '01-07-2022 10:35:42 AM', status: 'status', body: 'Test', },
  { from: "Crodriguez@montyramirezlaw.com", type: 'Outgoing', to: 'carlos091971@yahoo.com', cc: '', subject: '10001-04', date: '01-07-2022 10:35:42 AM', status: 'status', body: 'Test', },


];
@Component({
  selector: 'app-client-email',
  templateUrl: './client-email.component.html',
  styleUrls: ['./client-email.component.scss']
})
export class ClientEmailComponent implements OnInit {
  screenid = 1089;
  public icon = 'expand_more';

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
  constructor(public dialog: MatDialog, private auth: AuthService) { }
  new(): void {

    const dialogRef = this.dialog.open(EmailNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => {

      window.location.reload();
    });
  }
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
  }

  displayedColumns: string[] = ['select', 'type', 'from', 'to', 'cc', 'subject', 'body', 'date',];
  dataSource = new MatTableDataSource<PeriodicElement>(ELEMENT_DATA);
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.type + 1}`;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  reset() {
    // this.searhform.reset();
  }
}


