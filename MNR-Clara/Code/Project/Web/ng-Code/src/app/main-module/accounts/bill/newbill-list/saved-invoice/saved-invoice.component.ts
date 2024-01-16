import { SelectionModel } from '@angular/cdk/collections';
import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { BillService } from '../../bill.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-saved-invoice',
  templateUrl: './saved-invoice.component.html',
  styleUrls: ['./saved-invoice.component.scss']
})
export class SavedInvoiceComponent implements OnInit {
  ELEMENT_DATA: any[] = [];
  constructor(public dialogRef: MatDialogRef<any>, public dialog: MatDialog, private cas: CommonApiService, private fb: FormBuilder, private auth: AuthService, private cs: CommonService,
    private router: Router,     private service: BillService,
    private spin: NgxSpinnerService, public toastr: ToastrService, @Inject(MAT_DIALOG_DATA) public data: any,
    private http: HttpClient,) { }

  ngOnInit(): void {
    this.dropdownlist();

    this.dataSource = new MatTableDataSource<any>(this.data.createdInvoiceHeaders);
    this.dataSource1 = new MatTableDataSource<any>(this.data.erroredOutPrebillNumbers);
    this.dataSource2 = new MatTableDataSource<any>(this.data.duplicateInvoiceHeaders);

      if(this.data.createdInvoiceHeaders.length > 0){
        this.selectedIndex = 0;
      }
      if(this.data.duplicateInvoiceHeaders.length > 0){
        this.selectedIndex = 1;
      }
      if(this.data.erroredOutPrebillNumbers.length > 0){
        this.selectedIndex = 2;
      }
  }

  clientname: any
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.client.clientId.url,
    ]).subscribe((results) => {
      this.clientname = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.client.clientId.key);
      this.data.createdInvoiceHeaders.forEach(x => {
        x['clientname'] = this.clientname.find(y => y.key == x.clientId)?.value;
      })
      this.data.erroredOutPrebillNumbers.forEach(x => {
        x['clientname'] = this.clientname.find(y => y.key == x.clientId)?.value;
      })
      this.data.duplicateInvoiceHeaders.forEach(x => {
        x['clientname'] = this.clientname.find(y => y.key == x.clientId)?.value;
      })
    })
    this.spin.hide();
  }
selectedIndex: number = 0;
 tabChanged(tabChangeEvent: MatTabChangeEvent): void {
  this.selectedIndex = tabChangeEvent.index;
} 

  @ViewChild(MatSort)
  sort: MatSort;
  @ViewChild(MatPaginator)
  paginator: MatPaginator; // Pagination



  displayedColumns: string[] = ['clientId', 'matterNumber', 'partnerAssigned', 'preBillNumber', 'clientId1', 'invoiceAmount' ];
  displayedColumns1: string[] = ['clientId', 'matterNumber', 'partnerAssigned', 'preBillNumber', 'clientId1', 'invoiceAmount' ];
  displayedColumns2: string[] = ['clientId', 'matterNumber', 'partnerAssigned', 'preBillNumber', 'clientId1', 'invoiceAmount' ];
  dataSource = new MatTableDataSource<any>([]);
  dataSource1 = new MatTableDataSource<any>([]);
  dataSource2 = new MatTableDataSource<any>([]);
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
  checkboxLabel(row?: any): string {
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
  

  back(){
    this.router.navigate(['/main/accounts/billlist']);
  }
  sub = new Subscription();
  resbumit(){
    this.spin.show();
    this.sub.add(this.service.Create(this.data.erroredOutPrebillNumbers).subscribe(res => {
      if(res.erroredOutPrebillNumbers.length == 0){
        this.toastr.success("Invoice saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
      }else{
        this.toastr.error("Invoice failed!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
      }
      this.spin.hide();
    }, (err) => {
      this.spin.hide();
    }))
  }
}
