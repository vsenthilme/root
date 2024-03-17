import { SelectionModel } from "@angular/cdk/collections";
import { Component, Inject, OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";

export interface storagebin {


  no: string;
  dimensions: string;
  length: string;
  width: string;
  height: string;
  uom: string;
}
const ELEMENT_DATA: storagebin[] = [
  { no: "1", dimensions: 'Value', length: 'Enter', width: 'Enter', height: 'Enter', uom: 'dropdown', },
  { no: "1", dimensions: 'Value', length: 'Enter', width: 'Enter', height: 'Enter', uom: 'dropdown', },
  { no: "1", dimensions: 'Value', length: 'Enter', width: 'Enter', height: 'Enter', uom: 'dropdown', },
  { no: "1", dimensions: 'Value', length: 'Enter', width: 'Enter', height: 'Enter', uom: 'dropdown', },
  { no: "1", dimensions: 'Value', length: 'Enter', width: 'Enter', height: 'Enter', uom: 'dropdown', },
  { no: "1", dimensions: 'Value', length: 'Enter', width: 'Enter', height: 'Enter', uom: 'dropdown', },
  { no: "1", dimensions: 'Value', length: 'Enter', width: 'Enter', height: 'Enter', uom: 'dropdown', },
  { no: "1", dimensions: 'Value', length: 'Enter', width: 'Enter', height: 'Enter', uom: 'dropdown', },



];
@Component({
  selector: 'app-putaway-details',
  templateUrl: './putaway-details.component.html',
  styleUrls: ['./putaway-details.component.scss']
})
export class PutawayDetailsComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<PutawayDetailsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,) { }
  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<any>(this.data);
    console.log(this.data)
  }

  displayedColumns: string[] = ['packBarcodes', 'putAwayNumber', 'putawayConfirmedQty', 'proposedStorageBin', 'confirmedStorageBin',];
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

  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: storagebin): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.no + 1}`;
  }
}
