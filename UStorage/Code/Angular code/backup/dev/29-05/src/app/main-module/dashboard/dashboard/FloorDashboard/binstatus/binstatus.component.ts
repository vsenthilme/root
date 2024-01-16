import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { BinPopupComponent } from '../bin-popup/bin-popup.component';



export interface  firstFloor {
  status:  string;
  qtyAvailable:  string;
  BinNo:  string;
} 
const ELEMENT_DATA:  firstFloor[] = [
  {BinNo:  '1000', qtyAvailable:  '223', status:  'Available',},
  {BinNo:  '1001', qtyAvailable:  '323', status:  'Filled',},
  {BinNo:  '1002', qtyAvailable:  '323', status:  'Available',},
  {BinNo:  '1003', qtyAvailable:  '32', status:  'Filled',},
  {BinNo:  '1004', qtyAvailable:  '423', status:  'Filled',},
  {BinNo:  '1005', qtyAvailable:  '323', status:  'Available',},
  {BinNo:  '1006', qtyAvailable:  '32', status:  'Filled',},

];


@Component({
  selector: 'app-binstatus',
  templateUrl: './binstatus.component.html',
  styleUrls: ['./binstatus.component.scss']
})
export class BinstatusComponent implements OnInit {
  bincolor: string;

  constructor(  public dialog: MatDialog,) { }


  binStatus: any[] = [];
  ngOnInit(): void {
    this.binStatus = (ELEMENT_DATA);
    console.log(this.binStatus)
  }


  openDialog(binNo): void {
    const dialogRef = this.dialog.open(BinPopupComponent, {
      disableClose: true,
      width: '55%',
      maxWidth: '80%',
      data: { No: binNo,}
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }
}
