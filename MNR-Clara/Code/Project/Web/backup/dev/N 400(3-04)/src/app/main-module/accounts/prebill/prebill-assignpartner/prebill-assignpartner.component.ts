import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { AssignPartnerComponent } from "../prebill-new/assign-partner/assign-partner.component";
import { TimetocketDetailsComponent } from "../prebill-new/timetocket-details/timetocket-details.component";
import { ExpenceDetailsComponent } from "./expence-details/expence-details.component";

export interface PeriodicElement {
  documenttype: string;
  approvaldate: string;
  expirationdate: string;
  eligibility: string;
  remainder: string;
}
const ELEMENT_DATA: PeriodicElement[] = [
  { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', eligibility: 'Test', remainder: 'Test', },
  { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', eligibility: 'Test', remainder: 'Test', },
  { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', eligibility: 'Test', remainder: 'Test', },
  { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', eligibility: 'Test', remainder: 'Test', },
  { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', eligibility: 'Test', remainder: 'Test', },
  { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', eligibility: 'Test', remainder: 'Test', },
  { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', eligibility: 'Test', remainder: 'Test', },
  { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', eligibility: 'Test', remainder: 'Test', },
  { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', eligibility: 'Test', remainder: 'Test', },


];
@Component({
  selector: 'app-prebill-assignpartner',
  templateUrl: './prebill-assignpartner.component.html',
  styleUrls: ['./prebill-assignpartner.component.scss']
})
export class PrebillAssignpartnerComponent implements OnInit {


  //screenid: 1128 | undefined;
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
    console.log('show:' + this.showFloatingButtons);
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

  constructor(public dialog: MatDialog) { }
  assign() {
    const dialogRef = this.dialog.open(AssignPartnerComponent, {
      disableClose: true,
      width: '100%',
      maxWidth: '55%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => { });
  }
  timeticket() {
    const dialogRef = this.dialog.open(TimetocketDetailsComponent, {
      disableClose: true,
   
    });

    dialogRef.afterClosed().subscribe(result => { });
  }
  expense() {
    const dialogRef = this.dialog.open(ExpenceDetailsComponent, {
      disableClose: true,
      width: '100%',
      maxWidth: '75%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => { });
  }
  ngOnInit(): void {
  }

  displayedColumns: string[] = ['select', 'documenttype', 'approvaldate', 'expirationdate', 'eligibility', 'expense', 'remainder',];
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.documenttype + 1}`;
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


  disabled = false;
  step = 0;

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }

  panelOpenState = false;

}

