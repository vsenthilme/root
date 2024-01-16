import { SelectionModel } from '@angular/cdk/collections';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { TimetocketDetailsComponent } from '../timetocket-details/timetocket-details.component';
// export interface variant {


//   no: string;
//   dimensions: string;
//   timekeeper: string;
//   description: string;
//   hours: string;
//   amount: string;
//   billtype: string;
// }
// const ELEMENT_DATA: variant[] = [
//   { no: "Value", dimensions: 'Value', timekeeper: 'dropdown', description: 'dropdown', hours: 'dropdown', amount: 'dropdown', billtype: 'dropdown', },
//   { no: "Value", dimensions: 'Value', timekeeper: 'dropdown', description: 'dropdown', hours: 'dropdown', amount: 'dropdown', billtype: 'dropdown', },
//   { no: "Value", dimensions: 'Value', timekeeper: 'dropdown', description: 'dropdown', hours: 'dropdown', amount: 'dropdown', billtype: 'dropdown', },
//   { no: "Value", dimensions: 'Value', timekeeper: 'dropdown', description: 'dropdown', hours: 'dropdown', amount: 'dropdown', billtype: 'dropdown', },
//   { no: "Value", dimensions: 'Value', timekeeper: 'dropdown', description: 'dropdown', hours: 'dropdown', amount: 'dropdown', billtype: 'dropdown', },
//   { no: "Value", dimensions: 'Value', timekeeper: 'dropdown', description: 'dropdown', hours: 'dropdown', amount: 'dropdown', billtype: 'dropdown', },
//   { no: "Value", dimensions: 'Value', timekeeper: 'dropdown', description: 'dropdown', hours: 'dropdown', amount: 'dropdown', billtype: 'dropdown', },



// ];
@Component({
  selector: 'app-expense-details',
  templateUrl: './expense-details.component.html',
  styleUrls: ['./expense-details.component.scss']
})
export class ExpenseDetailsComponent implements OnInit {


  ELEMENT_DATA: any[] = [];
  constructor(public dialogRef: MatDialogRef<ExpenseDetailsComponent>, @Inject(MAT_DIALOG_DATA,) public data: any[],) { }


  ngOnInit(): void {
console.log(this.data)
this.data.forEach((element: any) => {
  element['positiveExpenseAmount'] = (element.expenseAmount < 0 ? element.expenseAmount * -1 : element.expenseAmount)
  console.log(element.positiveExpenseAmount )
})
console.log(this.data)
    this.dataSource = new MatTableDataSource<any>(this.data);
    this.selection = new SelectionModel<any>(true, []);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }
  sub = new Subscription();
  @ViewChild(MatSort)
  sort: MatSort;
  @ViewChild(MatPaginator)
  paginator: MatPaginator; // Pagination
  timekeeperCodelist: any[] = [];
  statuslist: any[] = [];



  displayedColumns: string[] = ['no', 'dimensions', 'timekeeper',];
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

  getTotalAmount() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.expenseAmount != null ? element.expenseAmount : 0);
    })
    let positiveTotal = (total < 0 ? total * -1 : total)
    return positiveTotal;
  }

}
