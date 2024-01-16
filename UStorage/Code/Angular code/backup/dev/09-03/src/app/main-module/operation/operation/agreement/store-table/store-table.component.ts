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

export interface Element {
  codeId: string;
  storeSizeMeterSquare: string;
  sno: number;
  location: string;
  rent: string;
  editable: false;
}


const ELEMENT_DATA: Element[] = [
  {sno: 1, codeId: '', storeSizeMeterSquare: '', location: '', rent: '', editable: false},
];

@Component({
  selector: 'app-store-table',
  templateUrl: './store-table.component.html',
  styleUrls: ['./store-table.component.scss']
})
export class StoreTableComponent implements OnInit {
  enableInputEdit: boolean;

  constructor(
    public dialogRef: MatDialogRef<StoreTableComponent>,
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
    this.enableInputEdit=  false
    this.dropdownlist();

      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
  }


  ELEMENT_DATA: any[] = [];
  displayedColumns: string[] = ['sno', 'codeId', 'storeSizeMeterSquare', 'location','rent', 'actions'];;
  dataSource = new MatTableDataSource<any>([]);


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  submit(){
 //console.log(this.dataSource.data)
 this.dialogRef.close({ data: this.dataSource.data, totalRent: this.rentTotal});
 
  }
  

  add(){
    this.dataSource.data.push( {sno: 1, codeId: '', storeSizeMeterSquare: '', location: '', rent: '', editable: true},);
    console.log(this.dataSource.data)
    this.dataSource._updateChangeSubscription();
  }


  removeEmail(index:any,element:any) {
    this.dataSource.data.splice(index, 1);
    this.dataSource._updateChangeSubscription();
  }

  enableEdit(){ 
    this.enableInputEdit = !this.enableInputEdit;
  }

  
editDomain(element: any){
  element.editable = !element.editable;
}


onItemSelect(e, element){
  console.log(this.data.rentPeriod)
  this.storageUnfiltered.forEach(x => {
    if(x.itemCode == e.value){
      element.size = x.storeSizeMeterSquare;
      element.location = x.phase;
      if ( this.data.rentPeriod == "WEEKLY") {
        element.rent = x.weekly
      }
      if (this.data.rentPeriod == "MONTHLY") {
        element.rent = x.monthly
      }
      if (this.data.rentPeriod == "QUARTERLY") {
        element.rent = x.quarterly
      }
      if (this.data.rentPeriod == "HALFYEARLY") {
        element.rent = x.halfYearly
      }
      if (this.data.rentPeriod == "YEARLY") {
        element.rent = x.yearly
      }
    }
  })
}
rentTotal: number;
  
getTotalRentAmount(){
  let total = 0;
  this.dataSource.data.forEach(element => {
    total = total + (element.rent != null ? parseInt(element.rent) : 0);
  })
  this.rentTotal = (Math.round(total * 100) / 100)
  return (Math.round(total * 100) / 100);
}


  storageunitList: any[] = [];
  storageUnfiltered: any;
  storageunitList1: any[] = [];
  storenumbersizeList: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.trans.storageunit.url,
    //  this.cas.dropdownlist.setup.storenumbersize.url,
      
    ]).subscribe((results) => {
      this.storageUnfiltered = results[0];
      this.storageUnfiltered.forEach((x: any) => {
           if(x.availability == "Vacant"){
          this.storageunitList.push({
            value: x.itemCode,
            label: x.codeId
          });
          this.storenumbersizeList.push({
            value: x.storeSizeMeterSquare,
            label: x.storeSizeMeterSquare,
            storeUnit: x.itemCode
          });
      }
       this.storageunitList1.push({
        value: x.itemCode,
        label: x.codeId
      });
      });
     // this.storenumbersizeList = this.cas.foreachlist1(results[1], this.cas.dropdownlist.setup.storenumbersize.key);
if(this.data.storeSize != undefined){
  this.data.storeSize.forEach(element => {
    console.log(this.storageunitList)
    element['storeNumber1'] = this.storageunitList1.find(y => y.value == element.storeNumber)?.label;
  });
}
      this.dataSource = new MatTableDataSource<any>(this.data.storeSize);
      this.spin.hide();
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }
}
