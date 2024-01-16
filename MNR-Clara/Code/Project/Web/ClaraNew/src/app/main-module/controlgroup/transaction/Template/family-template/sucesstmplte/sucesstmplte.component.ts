import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import swal from 'sweetalert2'
@Component({
  selector: 'app-sucesstmplte',
  templateUrl: './sucesstmplte.component.html',
  styleUrls: ['./sucesstmplte.component.scss']
})
export class SucesstmplteComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<SucesstmplteComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  showControlTestTick = false;
  showCombinedTestTick = false;
  showbrotherSisterTestTick = false;

  ngOnInit(): void {
    console.log(this.data)
    this.controlTest()
  }
  controlTestValue: number = 0;

  controlTest() {
    let interval = setInterval(() => {
      this.controlTestValue = this.controlTestValue + 10;
      if (this.controlTestValue >= 100) {
        this.controlTestValue = 100;
        this.showControlTestTick = true;
      
        this.successControlTest();
        clearInterval(interval);
      }
    }, 300);
  }

 
  combinedTestValue: number = 0;
  effectiveControlTest() {
    let interval = setInterval(() => {
      this.combinedTestValue = this.combinedTestValue + 10;
      if (this.combinedTestValue >= 100) {
          this.showCombinedTestTick = true;
      
        this.successCombinedControlTest();
        clearInterval(interval);
      }
    }, 300);
  }
  successCombinedControlTest() {
    console.log(this.data.effectiveControlTest);
    if (this.data.effectiveControlTest == true) {
      swal.fire({
        icon: 'success',
        title: 'Combined Control  Success',
        text: 'Validation passed successfully against the selected ownership',
        showConfirmButton: false,
        timer: 1800,
      })
      this.showSubmit = true;
    } if (this.data.effectiveControlTest == false) {
      swal.fire({
        icon: 'error',
        title: 'Combined Control  Failed',
        text: 'Validation failed against the selected ownership',
        showConfirmButton: false,
        timer: 1800,
      })
      this.showSubmit = true;
    }
    setTimeout(() => {
      this.showSubmit = true;
    }, 2000);

  }
  showSubmit = false;
  successControlTest() {
    if (this.data.ControlTest == true ) {
      console.log(this.data.ControlTest);
      console.log(this.data.effectiveControlTest);
      swal.fire({
        icon: 'success',
        title: 'Control Interest Success',
        text: 'Validation passed successfully against the selected ownership',
        showConfirmButton: false,
        timer: 1800,
      })
     
    } if (this.data.ControlTest == false || this.data.effectiveControlTest == false) {
      swal.fire({
        icon: 'error',
        title: 'Control Interest Failed',
        text: 'Validation failed against the selected ownership',
        showConfirmButton: false,
        timer: 1800,
      })
     
    }
    setTimeout(() => {
      this.effectiveControlTest();
    }, 2000);
    //this.showSubmit = true;
  }

 

}