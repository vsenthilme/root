import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { NotesComponent } from "src/app/common-field/notes/notes.component";
import { InquiryUpdate3Component } from "../inquiry-update3/inquiry-update3.component";
import { IntakePopupComponent } from "../intake-popup/intake-popup.component";


export interface PeriodicElement {

  no: number;
  date: string;
  mode: string;
  name: string;
  last: string;
  email: string;
  phone: string;
  notes: string;
  assign: string;
  sms: string;
  status: string;
  action: string;
}
const ELEMENT_DATA: PeriodicElement[] = [
  { no: 100001, date: "07-13-2021", mode: 'Phone', name: 'Adelis', last: ' Villegas Ortega', email: 'autexasgroup@gmail.com', phone: '832-858-3033', assign: ' Joanna', notes: 'Enter', status: 'Open', sms: 'Open', action: 'Edit' },
  { no: 100023, date: "07-15-2021", mode: 'Website', name: 'Isabel', last: ' Salazar', email: 'carlos091971@yahoo.com', phone: ' 713-419-6595', assign: ' Claudia', notes: 'Enter', status: 'Open', sms: 'Open', action: 'Edit' },
  { no: 100025, date: "07-15-2021", mode: 'EMail', name: 'Jaime', last: ' Garcia', email: 'lenaprado5x5@gmail.com', phone: ' 281-889-1617', assign: ' Joanna', notes: 'Enter', status: 'Open', sms: 'Open', action: 'Edit' },
  { no: 100043, date: "07-19-2021", mode: 'Phone', name: 'Jaqueline', last: ' Marquez', email: 'david15ron@gmail.com', phone: ' 512-921-5899', assign: ' Claudia', notes: 'Enter', status: 'Open', sms: 'Open', action: 'Edit' },
  { no: 100049, date: "07-26-2021", mode: 'Phone', name: 'Elizabeth', last: 'Sanchez Vazquez', email: 'terroneseva1@yahoo.com', phone: ' 936-232-8205', assign: ' Joanna', notes: 'Enter', status: 'Open', sms: 'Open', action: 'Edit' },
  { no: 100054, date: "08-04-2021", mode: 'Phone', name: 'Erika', last: 'Castro', email: 'principe_negro1@hotmail.com', phone: ' 936-333-7623', assign: ' Claudia', notes: 'Enter', status: 'Open', sms: 'Open', action: 'Edit' },
  { no: 100061, date: "08-09-2021", mode: 'Website', name: 'Eva', last: 'Martinez', email: 'alexander.elvir2012@gmail.com', phone: ' 832-269-8671', assign: ' Joanna', notes: 'Enter', status: 'Open', sms: 'Open', action: 'Edit' },
  { no: 100065, date: "08-14-2021", mode: 'Phone', name: 'Fabian', last: ' Sanchez Vazquez', email: 'felix.2935@yahoo.com', phone: ' 409-454-5873', assign: ' Claudia', notes: 'Enter', status: 'Open', sms: 'Open', action: 'Edit' },
  { no: 100069, date: "08-19-2021", mode: 'Website', name: 'Felix', last: ' Castro', email: 'gabycerda07@gmail.com', phone: ' 832-771-1060', assign: ' Joanna', notes: 'Enter', status: 'Open', sms: 'Open', action: 'Edit' },
  { no: 100073, date: "08-23-2021", mode: 'Phone', name: 'Gabriela', last: ' Martinez', email: 'vlindaflor196668@gmail.com', phone: ' 832-221-9015', assign: ' Claudia', notes: 'Enter', status: 'Open', sms: 'Open', action: 'Edit' },
  { no: 100079, date: "08-27-2021", mode: 'Phone', name: 'Gerardo', last: 'Terrones Garcia', email: 'piffas@gmail.com', phone: ' 956-222-4544', assign: ' Joanna', notes: 'Enter', status: 'Open', sms: 'Open', action: 'Edit' },

];
@Component({
  selector: 'app-inquiry-intake',
  templateUrl: './inquiry-intake.component.html',
  styleUrls: ['./inquiry-intake.component.scss']
})
export class InquiryIntakeComponent implements OnInit {
  screenid: 1064 | undefined;
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
  animal: string | undefined;
  name: string | undefined;
  constructor(public dialog: MatDialog) { }

  openDialog(): void {

    const dialogRef = this.dialog.open(IntakePopupComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => {


    });
  }
  openDialog1(): void {

    const dialogRef = this.dialog.open(InquiryUpdate3Component, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => {


    });
  }
  openDialog3(): void {

    const dialogRef = this.dialog.open(NotesComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
    });

    dialogRef.afterClosed().subscribe(result => {


    });
  }
  openDialog2(): void {

    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
    });

    dialogRef.afterClosed().subscribe(result => {


    });
  }

  ngOnInit(): void {
  }
  displayedColumns: string[] = ['select', 'no', 'date', 'mode', 'name', 'last', 'email', 'phone', 'assign', 'notes', 'sms', 'status', 'action'];
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.no + 1}`;
  }
}


