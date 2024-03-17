
  import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Table } from 'primeng/table';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { UserprofileNewComponent } from '../../user-profile/userprofile-new/userprofile-new.component';
import { PartnernumberingNewComponent } from './partnernumbering-new/partnernumbering-new.component';



  export interface  variant {
    name:  string;
    code:  string;
    from:  string;
    to:  string;
  } 
  const ELEMENT_DATA:  variant[] = [
    {code:  '1001',name:  'Shipsy', from: '0001', to: '999'},
    {code:  '1003',name:  'Ecomm', from: '2001', to: '2999'},
    {code:  '1004',name:  'WMS', from: '3001', to: '3999'}
  
  ];
  
  

  @Component({
    selector: 'app-partner-numbering',
    templateUrl: './partner-numbering.component.html',
    styleUrls: ['./partner-numbering.component.scss']
  })
  export class PartnerNumberingComponent implements OnInit {
      
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
    
        const dialogRef = this.dialog.open(PartnernumberingNewComponent, {
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
    

