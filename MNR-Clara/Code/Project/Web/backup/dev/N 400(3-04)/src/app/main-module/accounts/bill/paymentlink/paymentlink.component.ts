import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-paymentlink',
  templateUrl: './paymentlink.component.html',
  styleUrls: ['./paymentlink.component.scss']
})
export class PaymentlinkComponent implements OnInit {
  constructor(public dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any) { }

  public toggleButton: boolean = false;



  ngOnInit(): void {
    if(this.data != null){
     this.toggleButton = true;
    }
  }
  submit() {

    this.dialogRef.close(this.data);
    console.log(this.data)
  }
  edit(){
    this.toggleButton = false;
  }
}

