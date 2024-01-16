import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { PrimeIcons } from 'primeng/api';
import { Table } from 'primeng/table';
import { TrackHistoryComponent } from './track-history/track-history.component';


export interface  variant1 {
  code:  string;
  customerId:  string;
  employeeName:  string;
  userName:  string;
  password:  string;
  userProfile:  string;
  accessType:  string;
  accessType1:  string;
  accessType2:  string;
  accessType3:  string;
  accessType4:  string;
  accessType5:  string;
} 
const ELEMENT_DATA1:  variant1[] = [
  {code:  '11020030',customerId: '2', employeeName:  'Abdullah',userName:  '13132, Mubarak Al-kabir',password: 'P.O.Box: 15, Arabian Gulf Street, Safat 13001  Kuwait City',userProfile: 'Gift items',accessType: '5',
  accessType1: '5',accessType2: '11-02-2023', accessType3: 'Active', accessType4: '333', accessType5: '33'}

];
@Component({
  selector: 'app-consignment-history',
  templateUrl: './consignment-history.component.html',
  styleUrls: ['./consignment-history.component.scss']
})
export class ConsignmentHistoryComponent implements OnInit {

  @ViewChild('consignmentHistory') consignmentHistory: Table | undefined;

  
  consignmentNew1: any;
  selectedconsignmentNew1 : variant1[];


  

  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog,    private router: Router, public spinner: NgxSpinnerService) { 
    
  }

  events: any[];
  ngOnInit(): void {
  
    this.consignmentNew1= (ELEMENT_DATA1)
    this.spinner.show();

    setTimeout(() => {
      this.events = [
        {status: 'Order Creation', date: '10th Feb 2023 10:10 AM', icon: PrimeIcons.SHOPPING_CART, color: '#9C27B0', image: 'game-controller.jpg'},
        {status: 'Pickup Assignment', date: '10th Feb 2023 12:30 PM', icon: 'fa-solid fa-clipboard-check', color: '#FDC60A'},
        {status: 'Picking Completed', date: '10th Feb 2023 4:00 PM', icon: 'fa-solid fa-truck-fast', color: '#673AB7'},
        {status: 'Out of Delivery', date: '11th Feb 2023 09:00 AM', icon: PrimeIcons.CHECK, color: '#01EF03'}
    ];
      this.spinner.hide();
    }, 1000);
  }

  // applyFilterGlobal($event: any, stringVal: any) {
  //   this.userProfile!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  // }

  new(): void {

    const dialogRef = this.dialog.open(TrackHistoryComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
      //position: { top: '6.5%', },
    });

    dialogRef.afterClosed().subscribe(result => {
    });

   // this.router.navigate(['/main/consignment/tracking']);
  }
}

