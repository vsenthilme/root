
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
    accessType:  string;
  } 
  const ELEMENT_DATA:  variant[] = [
    {organization:  '100001',apiSubs:  'Asyad',partnerType:  '01',systemType:  '01',systemType1:  '01', systemType2:  '02 ', accessType:  '06'},
    {organization:  '100001',apiSubs:  'Naquel',partnerType:  '01',systemType:  '01',systemType1:  '01', systemType2:  '02 ', accessType:  '06'},
    {organization:  '100001',apiSubs:  'Ecomm Partner',partnerType:  '01',systemType:  '01',systemType1:  '01', systemType2:  '02 ', accessType:  '06'},
  
  ];
  
  @Component({
    selector: 'app-activiy-deck',
    templateUrl: './activiy-deck.component.html',
    styleUrls: ['./activiy-deck.component.scss']
  })
  export class ActiviyDeckComponent implements OnInit {
  
    
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
  
  