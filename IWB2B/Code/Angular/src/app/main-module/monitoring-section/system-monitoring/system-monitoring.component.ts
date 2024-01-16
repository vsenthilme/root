


  
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
  } 
  const ELEMENT_DATA:  variant[] = [
    {organization:  '100001',apiSubs:  'Asyad',partnerType:  '4',systemType:  '0.4%',systemType1:  '0.4%',systemType4:  '20', systemType2:  '10%',systemType3:  '5', accessType:  '00.09.00'},
    {organization:  '100002',apiSubs:  'Naquel',partnerType:  '4',systemType:  '0.4%',systemType1:  '0.4%',systemType4:  '20', systemType2:  '10%',systemType3:  '5', accessType:  '00.09.00'},
    {organization:  '100003',apiSubs:  'Ecomm Partner',partnerType:  '4',systemType:  '0.4%',systemType1:  '0.4%',systemType4:  '20', systemType2:  '10%',systemType3:  '5', accessType:  '00.09.00'},
  
  ];
  
  

  @Component({
    selector: 'app-system-monitoring',
    templateUrl: './system-monitoring.component.html',
    styleUrls: ['./system-monitoring.component.scss']
  })
  export class SystemMonitoringComponent implements OnInit {
  
    
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
  
  