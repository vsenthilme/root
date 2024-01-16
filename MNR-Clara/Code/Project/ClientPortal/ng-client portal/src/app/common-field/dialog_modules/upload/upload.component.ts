import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DialogExampleComponent, DialogData } from '../../dialog-example/dialog-example.component';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.scss']
})
export class UploadComponent implements OnInit {

  panelOpenState = false;
  constructor(
    public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) { }
  ngOnInit(): void {
  }

  delete(): void {
    this.dialogRef.close("delete");
  }

}

