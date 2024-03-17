


  
  import { Component, OnInit, ViewChild } from '@angular/core';
  import { MatDialog } from '@angular/material/dialog';
  import { Table } from 'primeng/table';
  
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
    alerts:  string;
    activehrs:  string;
  } 
  const ELEMENT_DATA:  variant[] = [
    {organization:  '100001',apiSubs:  'Asyad',partnerType:  'FMD',systemType:  '1',systemType1:  '0',systemType4:  'Available-Active', systemType2:  '57%',systemType3:  '10%', accessType:  'Medium', alerts:  '2', activehrs:  '17.09.00', },
    {organization:  '100002',apiSubs:  'Naqel',partnerType:  'LMD',systemType:  '1',systemType1:  '0',systemType4:  'Available-Active', systemType2:  '57%',systemType3:  '10%', accessType:  'Medium', alerts:  '2', activehrs:  '17.09.00', },
    {organization:  '100003',apiSubs:  'Ecomm-Partner',partnerType:  'ECOMM',systemType:  '1',systemType1:  '0',systemType4:  'Available-Active', systemType2:  '57%',systemType3:  '10%', accessType:  'Medium', alerts:  '2', activehrs:  '17.09.00', },
    {organization:  '100004',apiSubs:  'Odoo ERP',partnerType:  'IWE-ERP',systemType:  '1',systemType1:  '0',systemType4:  'Available-Active', systemType2:  '57%',systemType3:  '10%', accessType:  'Medium', alerts:  '2', activehrs:  '17.09.00', },
    {organization:  '100005',apiSubs:  'Classic WMS ',partnerType:  'IWE-WMS',systemType:  '1',systemType1:  '0',systemType4:  'Available-Active', systemType2:  '57%',systemType3:  '10%', accessType:  'Medium', alerts:  '2', activehrs:  '17.09.00', },
  
  ];
  

  @Component({
    selector: 'app-api-console',
    templateUrl: './api-console.component.html',
    styleUrls: ['./api-console.component.scss']
  })
  export class ApiConsoleComponent implements OnInit {
  
    
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
    

  
    advanceFilter(){
      this.advanceFilterShow = !this.advanceFilterShow;
    }
    
  }
  
  
