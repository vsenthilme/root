import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Table } from 'primeng/table';
import { variant } from '../../admin/client-assignment/client-assignment.component';
import { OrdersService } from '../orders.service';
import { Subscription } from 'rxjs';
import { NgxSpinnerService } from 'ngx-spinner';
import { CommonService } from 'src/app/common-service/common-service.service';

@Component({
  selector: 'app-orders-list',
  templateUrl: './orders-list.component.html',
  styleUrls: ['./orders-list.component.scss']
})
export class OrdersListComponent implements OnInit {

  @ViewChild('userProfile') userProfile: Table | undefined;
  orders: any[] = [];
  selectedOrders : any[] = [];
  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog, private service: OrdersService,  private spin: NgxSpinnerService, private cs: CommonService) { 
    
  }
  sub = new Subscription();
  ngOnInit(): void {
    this.getAll();
  }


  getAll(){
    this.spin.show();
    this.sub.add(this.service.Getall().subscribe((res: any[]) => {
      this.orders = res;
      this.spin.hide();
    }, err => {
      this.spin.hide();      
this.cs.commonerror(err);
    }));

  }
  applyFilterGlobal($event: any, stringVal: any) {
    this.userProfile!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }
  



  advanceFilter(){
    this.advanceFilterShow = !this.advanceFilterShow;
  }
  

  onChange() {
    const choosen= this.selectedOrders[this.selectedOrders.length - 1];   
    this.selectedOrders.length = 0;
    this.selectedOrders.push(choosen);
  } 

}

