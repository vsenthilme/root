import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DialogExampleComponent, DialogData } from 'src/app/common-field/dialog-example/dialog-example.component';

@Component({
  selector: 'app-update-qb',
  templateUrl: './update-qb.component.html',
  styleUrls: ['./update-qb.component.scss']
})
export class UpdateQbComponent implements OnInit {

  panelOpenState = false;
  constructor(
    public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) { }
  ngOnInit(): void {
  }

  update(): void {
    this.dialogRef.close("update");
  }

}