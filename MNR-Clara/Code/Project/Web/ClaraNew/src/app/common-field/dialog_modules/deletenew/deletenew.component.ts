import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DialogExampleComponent, DialogData } from '../../dialog-example/dialog-example.component';

@Component({
  selector: 'app-deletenew',
  templateUrl: './deletenew.component.html',
  styleUrls: ['./deletenew.component.scss']
})
export class DeletenewComponent implements OnInit {
  panelOpenState = false;
  constructor(
    public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) { }
  ngOnInit(): void {
  }

  onCancelClick(): void {
    // Emit 'cancel' when the cancel button is clicked
    this.dialogRef.close('cancel');
  }

  onConfirmClick(): void {
    // Emit 'confirm' when the confirm button is clicked
    this.dialogRef.close('confirm');
  }
}

