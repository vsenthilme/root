import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { AuthService } from "src/app/core/core";
import { UserprofileCopyComponent } from "../user-profile/userprofile-copy/userprofile-copy.component";

export interface PeriodicElement {
  class: string;
  clinetuserid: string;
  clientcatid: string;
  firstname: string;
  lastname: string;
  fullname: string;
  status: string;
  languageid: string;
  by: string;
  on: string;
}
const ELEMENT_DATA: PeriodicElement[] = [
  { class: "1001", clinetuserid: '2001', clientcatid: '3001', firstname: 'first', lastname: 'last', fullname: 'full', languageid: '4001', status: 'status', by: 'Admin', on: '08-05-2021' },
  { class: "1002", clinetuserid: '2001', clientcatid: '3001', firstname: 'first', lastname: 'last', fullname: 'full', languageid: '4001', status: 'status', by: 'Admin', on: '08-05-2021' },
  { class: "1003", clinetuserid: '2001', clientcatid: '3001', firstname: 'first', lastname: 'last', fullname: 'full', languageid: '4001', status: 'status', by: 'Admin', on: '08-05-2021' },
  { class: "1004", clinetuserid: '2001', clientcatid: '3001', firstname: 'first', lastname: 'last', fullname: 'full', languageid: '4001', status: 'status', by: 'Admin', on: '08-05-2021' },
  { class: "1005", clinetuserid: '2001', clientcatid: '3001', firstname: 'first', lastname: 'last', fullname: 'full', languageid: '4001', status: 'status', by: 'Admin', on: '08-05-2021' },
  { class: "1006", clinetuserid: '2001', clientcatid: '3001', firstname: 'first', lastname: 'last', fullname: 'full', languageid: '4001', status: 'status', by: 'Admin', on: '08-05-2021' },
  { class: "1007", clinetuserid: '2001', clientcatid: '3001', firstname: 'first', lastname: 'last', fullname: 'full', languageid: '4001', status: 'status', by: 'Admin', on: '08-05-2021' },
  { class: "1008", clinetuserid: '2001', clientcatid: '3001', firstname: 'first', lastname: 'last', fullname: 'full', languageid: '4001', status: 'status', by: 'Admin', on: '08-05-2021' },
  { class: "1009", clinetuserid: '2001', clientcatid: '3001', firstname: 'first', lastname: 'last', fullname: 'full', languageid: '4001', status: 'status', by: 'Admin', on: '08-05-2021' },


];
@Component({
  selector: 'app-client-user',
  templateUrl: './client-user.component.html',
  styleUrls: ['./client-user.component.scss']
})
export class ClientUserComponent implements OnInit {
  screenid = 1051;
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
  display(): void {

    const dialogRef = this.dialog.open(UserprofileCopyComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => {


    });
  }
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  } RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
  }

  displayedColumns: string[] = ['select', 'class', 'clinetuserid', 'clientcatid', 'firstname', 'lastname', 'fullname', 'status', 'languageid', 'by', 'on'];
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.class + 1}`;
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