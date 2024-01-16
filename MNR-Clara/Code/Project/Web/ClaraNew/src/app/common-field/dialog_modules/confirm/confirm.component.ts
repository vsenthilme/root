import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DialogExampleComponent, DialogData } from '../../dialog-example/dialog-example.component';

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.scss']
})
export class ConfirmComponent implements OnInit {

  panelOpenState = false;
  constructor(
    public dialogRef: MatDialogRef<any>,
      @Inject(MAT_DIALOG_DATA) public data: any,) { }
  ngOnInit(): void {
  }

  Confirm(): void {
    this.dialogRef.close("Yes");
  }

}
