import { SelectionModel } from "@angular/cdk/collections";
import { DecimalPipe } from "@angular/common";
import { Component, Inject, OnInit, ViewChild } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
export interface variant {
  no: number,

  fAmount: number;
  cAmount: number;
}
const ELEMENT_DATA: variant[] = [


];
@Component({
  selector: 'app-totalamount',
  templateUrl: './totalamount.component.html',
  styleUrls: ['./totalamount.component.scss']
})
export class TotalamountComponent implements OnInit {


  ELEMENT_DATA: any[] = [];
  constructor(public dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any,     private decimalPipe: DecimalPipe,) { }
  ngOnInit(): void {
    this.ELEMENT_DATA.push({ no: 0, fAmount: this.data.addInvoiceLine_t[0].fAmount, cAmount: this.decimalPipe.transform(this.data.addInvoiceLine_t[0].cAmount, "1.2-2"), })
console.log(this.ELEMENT_DATA)

this.ELEMENT_DATA.forEach((element: any) => {
  element['positivecAmount'] = (element.cAmount < 0 ? element.cAmount * -1 : element.cAmount)
})



    this.dataSource = new MatTableDataSource<variant>(this.ELEMENT_DATA);
  }
  @ViewChild(MatSort)
  sort: MatSort;
  @ViewChild(MatPaginator)
  paginator: MatPaginator; // Pagination



  displayedColumns: string[] = ['no', 'dimensions',];
  dataSource = new MatTableDataSource<variant>(ELEMENT_DATA);
  selection = new SelectionModel<variant>(true, []);
  submit() {
    this.data.addInvoiceLine_t[0].fAmount = Number(this.dataSource.data[0].fAmount);
    this.data.addInvoiceLine_t[0].cAmount = Number(this.dataSource.data[0].cAmount);
    this.dialogRef.close(this.data);
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
  checkboxLabel(row?: variant): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.no + 1}`;
  }
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  /** The label for the checkbox on the passed row */
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }

  onInputChange(event: string) {
    this.data.addInvoiceLine_t[0].fAmount = Number.parseFloat(event).toFixed(2);
  }
}
