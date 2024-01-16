
  import { Component, OnInit, ViewChild } from '@angular/core';
  import { MatDialog } from '@angular/material/dialog';
  import { Table } from 'primeng/table';
  
  export interface  variant {
    partnerType:  string;
    apiSubs:  string;
    organization:  string;
    systemType:  string;
  } 
  const ELEMENT_DATA:  variant[] = [
    {organization:  '',apiSubs:  '',partnerType:  '',systemType:  '',},
  ];
  



  @Component({
    selector: 'app-hubops',
    templateUrl: './hubops.component.html',
    styleUrls: ['./hubops.component.scss']
  })
  export class HubopsComponent implements OnInit {
  
  
    
    @ViewChild('userProfile') userProfile: Table | undefined;
    products: any;
    pickup: any;
    selectedProducts : variant[];
    advanceFilterShow: boolean;
  
    constructor(public dialog: MatDialog,) { 
      
    }
  
    ngOnInit(): void {
      this.products= (ELEMENT_DATA)
    }
  
    applyFilterGlobal($event: any, stringVal: any) {
      this.userProfile!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
    }
    

  
    advanceFilter(){
      this.advanceFilterShow = !this.advanceFilterShow;
    }



  }
  
  

