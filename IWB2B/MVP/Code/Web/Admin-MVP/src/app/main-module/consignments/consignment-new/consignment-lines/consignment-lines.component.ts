import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-consignment-lines',
  templateUrl: './consignment-lines.component.html',
  styleUrls: ['./consignment-lines.component.scss']
})
export class ConsignmentLinesComponent implements OnInit {
  orderLines: any;

  constructor(    public dialogRef: MatDialogRef < ConsignmentLinesComponent > ,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService) { }


    

  ngOnInit(): void {
   this.orderLines = (this.data.orderDetailsLines);
   console.log(this.orderLines)
  }

  disabled = false;
  step = 0;

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }

  panelOpenState = false;

}
