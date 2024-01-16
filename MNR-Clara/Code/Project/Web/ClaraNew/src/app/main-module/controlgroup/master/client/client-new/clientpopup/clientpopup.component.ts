import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DialogExampleComponent } from 'src/app/common-field/dialog-example/dialog-example.component';

@Component({
  selector: 'app-clientpopup',
  templateUrl: './clientpopup.component.html',
  styleUrls: ['./clientpopup.component.scss']
})
export class ClientpopupComponent implements OnInit {

  constructor(public dialog: MatDialog,
    public dialogRef: MatDialogRef<DialogExampleComponent>,
   private fb:FormBuilder) { }

  ngOnInit(): void {
  }

  submit(){
    this.dialogRef.close("Yes");
  }

}
