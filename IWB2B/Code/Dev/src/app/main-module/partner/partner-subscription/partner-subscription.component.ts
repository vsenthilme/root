
  import { Component, OnInit, ViewChild } from '@angular/core';
  import { MatDialog } from '@angular/material/dialog';
  import { Table } from 'primeng/table';
  import * as FileSaver from 'file-saver';
  import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { SubscriptionNewComponent } from './subscription-new/subscription-new.component';
  
  export interface  variant {
    partnerType:  string;
    apiSubs:  string;
    organization:  string;
    systemType:  string;
    systemType1:  string;
    systemType2:  string;
    systemType3:  string;
    systemType4:  string;
    accessType:  string;
  } 
  const ELEMENT_DATA:  variant[] = [
    {organization:  '1001',apiSubs:  'Asyad',partnerType:  'LMD',systemType:  'Asyad',systemType1:  'Shipsy',systemType4:  '*******', systemType2:  'Yes',systemType3:  'Full Access', accessType:  'Yes'},
    {organization:  '1002',apiSubs:  'Naquel',partnerType:  'LMD',systemType:  'Naquel',systemType1:  'Shipsy',systemType4:  '*******', systemType2:  'Yes',systemType3:  'Full Access', accessType:  'Yes'},
    {organization:  '1003',apiSubs:  'Ecomm Partner',partnerType:  'Ecomm',systemType:  'IWE',systemType1:  'Ecomm',systemType4:  '*******', systemType2:  'Yes',systemType3:  'Full Access', accessType:  'Yes'},
  
  ];
  
  
  @Component({
    selector: 'app-partner-subscription',
    templateUrl: './partner-subscription.component.html',
    styleUrls: ['./partner-subscription.component.scss']
  })
  export class PartnerSubscriptionComponent implements OnInit {
  
    
    @ViewChild('userProfile') userProfile: Table | undefined;
    products: any;
    selectedProducts : variant[];
    advanceFilterShow: boolean;
  
    constructor(public dialog: MatDialog,) { 
      
    }
  
    ngOnInit(): void {
      console.log(ELEMENT_DATA)
      this.products= (ELEMENT_DATA)
      console.log( this.products) 
      
    }
  
    applyFilterGlobal($event: any, stringVal: any) {
      this.userProfile!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
    }
    
  
    new(): void {
  
      const dialogRef = this.dialog.open(SubscriptionNewComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        position: { top: '6.5%', },
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }
  
    delete(): void {
  
      const dialogRef = this.dialog.open(DeleteComponent, {
        disableClose: true,
        width: '35%',
        maxWidth: '80%',
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }
  
  
    advanceFilter(){
      this.advanceFilterShow = !this.advanceFilterShow;
    }
    
  }
  
  