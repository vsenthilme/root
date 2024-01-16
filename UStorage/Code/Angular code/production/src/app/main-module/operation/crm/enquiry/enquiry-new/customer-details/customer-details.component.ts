

  import { SelectionModel } from '@angular/cdk/collections';
  import { Component, Inject, OnInit, ViewChild } from '@angular/core';
  import { FormBuilder } from '@angular/forms';
  import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
  import { MatPaginator } from '@angular/material/paginator';
  import { MatSort } from '@angular/material/sort';
  import { MatTableDataSource } from '@angular/material/table';
  import { Router } from '@angular/router';
  import { NgxSpinnerService } from 'ngx-spinner';
  import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
  import { CommonApiService } from 'src/app/common-service/common-api.service';
  import { CommonService } from 'src/app/common-service/common-service.service';
  import { AuthService } from 'src/app/core/core';
import { CustomerService } from 'src/app/main-module/Masters -1/customer-master/customer.service';
  import { StorageunitService } from 'src/app/main-module/Masters -1/material-master/storage-unit/storageunit.service';
  
  @Component({
    selector: 'app-customer-details',
    templateUrl: './customer-details.component.html',
    styleUrls: ['./customer-details.component.scss']
  })
  export class CustomerDetailsComponent implements OnInit {
  
    constructor(
      public dialogRef: MatDialogRef<any>,
      @Inject(MAT_DIALOG_DATA) public data: any,
      public toastr: ToastrService,
      private spin: NgxSpinnerService,
      private auth: AuthService,
      private fb: FormBuilder,
      public cs: CommonService,
      private router: Router,
      public service: CustomerService,
      private cas: CommonApiService,
      public dialog: MatDialog,
      
    ) { }
    sub = new Subscription();
    ngOnInit(): void {

      this.sub.add(this.service.search({mobileNumber: [this.data.mobileNumber]}).subscribe(res => {
        this.dataSource = new MatTableDataSource<any>(res);
        this.selection = new SelectionModel<any>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      }))
    }
  
    ELEMENT_DATA: any[] = [];
    displayedColumns: string[] = ['select', 'customerCode', 'customerName', 'mobileNumber', 'civilId', 'type'];
    dataSource = new MatTableDataSource < any > (this.ELEMENT_DATA);
    selection = new SelectionModel < any > (true, []);
  
  
    /** Whether the number of selected elements matches the total number of rows. */
    isAllSelected() {
      const numSelected = this.selection.selected.length;
      const numRows = this.dataSource.data.length;
      return numSelected === numRows;
    }
  
    /** Selects all rows if they are not all selected; otherwise clear selection. */
    toggleAllRows() {
      if (this.isAllSelected()) {
        this.selection.clear();
        return;
      }
  
      this.selection.select(...this.dataSource.data);
    }
  
    /** The label for the checkbox on the passed row */
    checkboxLabel(row ? : any): string {
      if (!row) {
        return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
      }
      return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.invoiceNumber + 1}`;
    }
  
    clearselection(row: any) {
      this.selection.clear();
      this.selection.toggle(row);
    }
  
  
    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;
  
    submit(){
      if (this.selection.selected.length === 0) {
        this.toastr.warning("Kindly select any Row", "Notification",{
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
      this.dialogRef.close(this.selection.selected[0].customerCode);
    }
    
  }
  