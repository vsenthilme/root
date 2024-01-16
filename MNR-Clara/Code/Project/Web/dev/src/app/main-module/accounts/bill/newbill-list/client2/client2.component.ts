import { SelectionModel } from "@angular/cdk/collections";
import { HttpClient } from "@angular/common/http";
import { Component, Inject, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { FormService } from "src/app/customerform/form.service";
import { GeneralMatterService } from "src/app/main-module/matters/case-management/General/general-matter.service";
import { TotalamountComponent } from "../totalamount/totalamount.component";

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-client2',
  templateUrl: './client2.component.html',
  styleUrls: ['./client2.component.scss']
})
export class Client2Component implements OnInit {


  ELEMENT_DATA: any[] = [];
  constructor(public dialogRef: MatDialogRef<any>, public dialog: MatDialog, private cas: CommonApiService, private fservice: FormService, private fb: FormBuilder, private auth: AuthService, private cs: CommonService,
    private router: Router, private service: GeneralMatterService,
    private spin: NgxSpinnerService, public toastr: ToastrService, @Inject(MAT_DIALOG_DATA) public data: any,
    private http: HttpClient,) { }
  ngOnInit(): void {
    this.dropdownlist();

    this.dataSource = new MatTableDataSource<any>(this.data.addInvoiceLine_t);
  }
  @ViewChild(MatSort)
  sort: MatSort;
  @ViewChild(MatPaginator)
  paginator: MatPaginator; // Pagination



  displayedColumns: string[] = ['no', 'dimensions1', 'dimensions2', 'dimensions3', 'delete'];;
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
  totalamount(): void {

    const dialogRef = this.dialog.open(TotalamountComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => {


    });
  }

  Add() {
    console.log(this.dataSource.data)
    this.dataSource.data.push({
      fAmount: 0,
      cAmount: 0,
      clientId: []
    });
    console.log(this.dataSource.data)
    this.dataSource._updateChangeSubscription();

  }
  getamount(element: any) {
  if(element){
    return Number(element.fAmount) + Number(element.cAmount)
  }else{
    return 0;
  }
   
  }
  remove(element: any) {

    this.data.addInvoiceLine_t.forEach((value: any, index: any) => {
      if (value.clientId == element.clientId) this.data.addInvoiceLine_t.splice(index, 1);
    });
    this.dataSource = new MatTableDataSource<any>(this.data.addInvoiceLine_t);
  }

  save() {
    this.dialogRef.close();
  }
  sub = new Subscription();

  selectedItems: SelectItem[] = [];
  multiselectclientList: SelectItem[] = [];
  multiclientList: SelectItem[] = [];

  dropdownSettings = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };
  referralList: any = [];
  dropdownlist() {
    this.spin.show();
    this.sub.add(this.service.GetClientdetails().subscribe(res => {
      this.referralList = res;
      this.referralList.forEach((x: { clientId: string; firstNameLastName: string; }) => this.multiclientList.push({ id: x.clientId, itemName: x.clientId + '-' + x.firstNameLastName }))
      this.multiselectclientList = this.multiclientList;

      this.spin.hide();

    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    this.spin.hide();

  }
  onItemSelect(item: any) {
    console.log(item);
    console.log(this.selectedItems);
  }
  OnItemDeSelect(item: any) {
    console.log(item);
    console.log(this.selectedItems);
  }
  onSelectAll(items: any) {
    console.log(items);
  }
  onDeSelectAll(items: any) {
    console.log(items);
  }
}
