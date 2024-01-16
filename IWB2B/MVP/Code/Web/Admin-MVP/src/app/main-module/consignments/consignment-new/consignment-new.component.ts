import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { MenuItem, MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ConsignmentService } from '../consignment.service';
import { ConsignmentLinesComponent } from './consignment-lines/consignment-lines.component';
import { ConsignmentNewPopupComponent } from './consignment-new-popup/consignment-new-popup.component';

export interface  variant1 {
  code:  string;
  employeeName:  string;
  userName:  string;
  password:  string;
  userProfile:  string;
  accessType:  string;
  accessType1:  string;
  accessType2:  string;
  accessType3:  string;
} 
const ELEMENT_DATA1:  variant1[] = [
  {code:  '11020030',employeeName:  'Abdullah',userName:  '13132, Mubarak Al-kabir',password: 'P.O.Box: 15, Arabian Gulf Street, Safat 13001  Kuwait City',userProfile: 'Gift items',accessType: '5',
  accessType1: '5',accessType2: '11-02-2023', accessType3: 'New'}

];


@Component({
  selector: 'app-consignment-new',
  templateUrl: './consignment-new.component.html',
  styleUrls: ['./consignment-new.component.scss']
})
export class ConsignmentNewComponent implements OnInit {
  @ViewChild('consignmentNew') consignmentNew: Table | undefined;

  
  consignmentNew1: any;
  selectedconsignmentNew1 : any[] = [];


  

  advanceFilterShow: boolean;


  constructor(public dialog: MatDialog, public spinner: NgxSpinnerService, public cs: CommonService, private messageService: MessageService,
    private service: ConsignmentService, private router: Router,){ 
     
   }



  ngOnInit(): void {
  
    this.getAll();
  }
  sub = new Subscription();
  
  getAll(){
    this.spinner.show();
    this.sub.add(this.service.Getall().subscribe((res: any[]) => {
      this.consignmentNew1= (res);
      this.spinner.hide();
    }
    , err => {
      this.cs.commonerror(err);
      this.spinner.hide();
    }));
  }


  delete(): void {

    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '35%',
      maxWidth: '80%',
      data: this.consignmentNew1[0].customerId
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleterecord(this.consignmentNew1[0].orderId);
      }
    });
  }

  deleterecord(orderId: any,) {
    this.spinner.show();
    this.sub.add(this.service.Delete(orderId).subscribe((res) => {
      this.messageService.add({key: 'br', severity:'success', summary:'Success', detail: orderId + " Deleted successfully"});
      this.spinner.hide();
      this.getAll();
    }, err => {
      this.cs.commonerror(err);
      this.spinner.hide();
    }));
  }

  onChange() {
    const choosen= this.selectedconsignmentNew1[this.selectedconsignmentNew1.length - 1];   
    this.selectedconsignmentNew1.length = 0;
    this.selectedconsignmentNew1.push(choosen);
  }

  openDialog(data: any = 'New'): void {
    if (this.selectedconsignmentNew1.length === 0 && data != 'New') {
      this.messageService.add({key: 'br', severity:'warn', summary:'Warning', detail:'Kindly select any Row'});

      return;
    }

    let paramdata = "";
    paramdata = this.cs.encrypt({ pageflow: data, code: data != 'New' ? this.selectedconsignmentNew1[0] : null });
    this.router.navigate(['/main/consignment/consignmentCreate/' + paramdata]);
  }


  openLines(element): void {
    const dialogRef = this.dialog.open(ConsignmentLinesComponent, {
      disableClose: true,
      width: '70%',
      maxWidth: '80%',
      data: element
    });

    dialogRef.afterClosed().subscribe(result => {
      //this.getAll();
    });
  }
  downloadexcel( ) {
    var res: any = [];
    this.consignmentNew1.forEach(x => {
      res.push({
       "Order ID":x.orderId,
       "Customer Name":x.customerId,
       "Delivery Charge":x.deliveryCharge,
       "Order Date":this.cs.dateapi(x.createdOn),
       "Status":x.status,
  
      
      });
  
    })
    this.cs.exportAsExcel(res, "Consignment New");
  }

}