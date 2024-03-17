import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Table } from 'primeng/table';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { UserprofileNewComponent } from '../../user-profile/userprofile-new/userprofile-new.component';
import { SystemTypeNewComponent } from './system-type-new/system-type-new.component';

export interface  variant {
  desc:  string;
  code:  string;
} 
const ELEMENT_DATA:  variant[] = [
  {code:  '1001',desc:  'Shipy',},
  {code:  '1002',desc:  'Odoo',},
  {code:  '1003',desc:  'Ecomm',},
  {code:  '1004',desc:  'WMS',}

];


@Component({
  selector: 'app-system-type',
  templateUrl: './system-type.component.html',
  styleUrls: ['./system-type.component.scss']
})
export class SystemTypeComponent implements OnInit {
    
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
  
      const dialogRef = this.dialog.open(SystemTypeNewComponent, {
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
  