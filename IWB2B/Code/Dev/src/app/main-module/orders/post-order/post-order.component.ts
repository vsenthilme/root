import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { OrdersService } from '../orders.service';
import { MessageService } from 'primeng/api';
import { NgxSpinnerService } from 'ngx-spinner';
import { CommonService } from 'src/app/common-service/common-service.service';

@Component({
  selector: 'app-post-order',
  templateUrl: './post-order.component.html',
  styleUrls: ['./post-order.component.scss']
})
export class PostOrderComponent implements OnInit {

  @ViewChild('userProfile') userProfile: Table | undefined;
  postOrder: any[] = [];
  selectedPostOrder: any[] = [];
  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog, private service: OrdersService,     private messageService: MessageService,
    private spin: NgxSpinnerService, public cs : CommonService,) { 
    
  }
  sub = new Subscription();
  ngOnInit(): void {
  
    this.getAll();
  }


  getAll(){
    this.spin.show();
    this.sub.add(this.service.GetOrderType('handover_courier_partner').subscribe((res: any[]) => {
      this.postOrder = res;
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
    const choosen= this.selectedPostOrder[this.selectedPostOrder.length - 1];   
    this.selectedPostOrder.length = 0;
    this.selectedPostOrder.push(choosen);
  } 
  

  callPostOrder(element){
    this.spin.show();
    this.sub.add(this.service.postJNT(element.reference_number).subscribe((res: any[]) => {
    if(res){
      this.messageService.add({key: 'br', severity:'success', summary:'Success', detail:  "Order Posted successfully"});
      this.spin.hide();
    }
    }, err => {
      this.spin.hide();      
this.cs.commonerror(err);
    }));
  }

}

