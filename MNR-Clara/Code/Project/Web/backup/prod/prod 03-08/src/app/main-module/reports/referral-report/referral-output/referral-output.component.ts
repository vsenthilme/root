import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { NotesComponent } from "src/app/common-field/notes/notes.component";


export interface PeriodicElement {
  name: string;
  email: string;
  contact: string;
  clientno: string;
  inquiry: string;
  date: string;
  by: string;
  referred: string;

}

const ELEMENT_DATA: PeriodicElement[] = [
  { inquiry: '100001', clientno: '200001', name: 'Lorem', email: 'lorem@gmail.com', referred: 'lorem', date: '10-21-2021', by: 'admin', contact: 'lorem', },
  { inquiry: '100001', clientno: '200001', name: 'Lorem', email: 'lorem@gmail.com', referred: 'lorem', date: '10-21-2021', by: 'admin', contact: 'lorem', },
  { inquiry: '100001', clientno: '200001', name: 'Lorem', email: 'lorem@gmail.com', referred: 'lorem', date: '10-21-2021', by: 'admin', contact: 'lorem', },
  { inquiry: '100001', clientno: '200001', name: 'Lorem', email: 'lorem@gmail.com', referred: 'lorem', date: '10-21-2021', by: 'admin', contact: 'lorem', },
  { inquiry: '100001', clientno: '200001', name: 'Lorem', email: 'lorem@gmail.com', referred: 'lorem', date: '10-21-2021', by: 'admin', contact: 'lorem', },
  { inquiry: '100001', clientno: '200001', name: 'Lorem', email: 'lorem@gmail.com', referred: 'lorem', date: '10-21-2021', by: 'admin', contact: 'lorem', },
  { inquiry: '100001', clientno: '200001', name: 'Lorem', email: 'lorem@gmail.com', referred: 'lorem', date: '10-21-2021', by: 'admin', contact: 'lorem', },
  { inquiry: '100001', clientno: '200001', name: 'Lorem', email: 'lorem@gmail.com', referred: 'lorem', date: '10-21-2021', by: 'admin', contact: 'lorem', },
];

@Component({
  selector: 'app-referral-output',
  templateUrl: './referral-output.component.html',
  styleUrls: ['./referral-output.component.scss']
})
export class ReferralOutputComponent implements OnInit {

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


  displayedColumns: string[] = ['select', 'inquiry', 'clientno', 'name', 'contact', 'email', 'referred', 'date'];
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.name + 1}`;
  }
  constructor(public dialog: MatDialog) { }
  ngOnInit(): void {
  }
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(NotesComponent, {
      width: '50%',
      maxWidth: '80%',
    });

    dialogRef.afterClosed().subscribe(result => {

    });
  }


}
