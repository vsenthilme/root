
  
  import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Table } from 'primeng/table';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { UserprofileNewComponent } from '../../user-profile/userprofile-new/userprofile-new.component';
import { CountryNewComponent } from './country-new/country-new.component';



  export interface  variant {
    name:  string;
    code:  string;
    desc:  string;
  } 
  const ELEMENT_DATA:  variant[] = [
    {code:  '1001',name:  'KW', desc: 'Kuwait'},
    {code:  '1003',name:  'OM', desc: 'Oman'},
    {code:  '1004',name:  'KSA', desc: 'Saudi'}
  
  ];
  
  

  @Component({
    selector: 'app-partner-country',
    templateUrl: './partner-country.component.html',
    styleUrls: ['./partner-country.component.scss']
  })
  export class PartnerCountryComponent implements OnInit {
      
      @ViewChild('systemTypeSearch') systemTypeSearch: Table | undefined;
      systemType: any;
      selectedSystemType : variant[];
      advanceFilterShow: boolean;
    
      constructor(public dialog: MatDialog,) { 
        
      }
    
      ngOnInit(): void {
        this.systemType= (ELEMENT_DATA)
      }
    
      applyFilterGlobal($event: any, stringVal: any) {
        this.systemTypeSearch!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
      }
  
    
      new(): void {
    
        const dialogRef = this.dialog.open(CountryNewComponent, {
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
    


