import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-print',
  templateUrl: './print.component.html',
  styleUrls: ['./print.component.scss']
})
export class PrintComponent implements OnInit {

  constructor(      public dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) public data: any,) { }
    Label: '';
  ngOnInit(): void {
  }
  onInputChange(e){
      }

}
