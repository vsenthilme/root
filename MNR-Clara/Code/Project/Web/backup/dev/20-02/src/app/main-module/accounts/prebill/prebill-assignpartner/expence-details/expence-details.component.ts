import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit } from "@angular/core";
import { MatTableDataSource } from "@angular/material/table";

export interface  variant {


  no: string;
  dimensions: string;
  timekeeper: string;
  description: string;
  hours: string;
  amount: string;
  billtype: string;
}
const ELEMENT_DATA:  variant[] = [
  { no: "Value", dimensions: 'Value', timekeeper: 'dropdown',description: 'dropdown',hours: 'dropdown',amount: 'dropdown',billtype: 'dropdown',  },
  { no: "Value", dimensions: 'Value', timekeeper: 'dropdown',description: 'dropdown',hours: 'dropdown',amount: 'dropdown',billtype: 'dropdown',  },
  { no: "Value", dimensions: 'Value', timekeeper: 'dropdown',description: 'dropdown',hours: 'dropdown',amount: 'dropdown',billtype: 'dropdown',  },
  { no: "Value", dimensions: 'Value', timekeeper: 'dropdown',description: 'dropdown',hours: 'dropdown',amount: 'dropdown',billtype: 'dropdown',  },
  { no: "Value", dimensions: 'Value', timekeeper: 'dropdown',description: 'dropdown',hours: 'dropdown',amount: 'dropdown',billtype: 'dropdown',  },
  { no: "Value", dimensions: 'Value', timekeeper: 'dropdown',description: 'dropdown',hours: 'dropdown',amount: 'dropdown',billtype: 'dropdown',  },
  { no: "Value", dimensions: 'Value', timekeeper: 'dropdown',description: 'dropdown',hours: 'dropdown',amount: 'dropdown',billtype: 'dropdown',  },



];
@Component({
  selector: 'app-expence-details',
  templateUrl: './expence-details.component.html',
  styleUrls: ['./expence-details.component.scss']
})
export class ExpenceDetailsComponent implements OnInit {

 
  ngOnInit(): void {
  }

  displayedColumns: string[] = ['no', 'dimensions','timekeeper', ];
  dataSource = new MatTableDataSource< variant>(ELEMENT_DATA);
  selection = new SelectionModel< variant>(true, []);

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
  checkboxLabel(row?:  variant): string {
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
  }}
