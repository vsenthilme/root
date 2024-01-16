import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DialogExampleComponent, DialogData } from 'src/app/common-field/dialog-example/dialog-example.component';
import swal from 'sweetalert2'
@Component({
  selector: 'app-success-template',
  templateUrl: './success-template.component.html',
  styleUrls: ['./success-template.component.scss']
})
export class SuccessTemplateComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<SuccessTemplateComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  showControlTestTick = false;
  showEffectiveControlTestTick = false;
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

  effectiveTestValue: number = 0;
  effectiveControlTest() {
    let interval = setInterval(() => {
      this.effectiveTestValue = this.effectiveTestValue + 10;
      if (this.effectiveTestValue >= 100) {
        this.effectiveTestValue = 100;
        this.showEffectiveControlTestTick = true;
        this.successEffectiveControlTest();
        clearInterval(interval);
      }
    }, 300);
  }


  brotherSisterTestValue: number = 0;
  brotherSisterTest() {
    let interval = setInterval(() => {
      this.brotherSisterTestValue = this.brotherSisterTestValue + 10;
      if (this.brotherSisterTestValue >= 100) {
        this.brotherSisterTestValue = 100;
        this.showbrotherSisterTestTick = true;
        this.successbrotherSisterTest();
        clearInterval(interval);
      }
    }, 300);
  }


  successControlTest() {
    if (this.data.ControlTest == true) {
      swal.fire({
        icon: 'success',
        title: 'Control Interest Success',
        text: 'Validation passed successfully against the selected ownership',
        showConfirmButton: false,
        timer: 1800,
      })
    } if (this.data.ControlTest == false) {
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

  }
  successEffectiveControlTest() {
    if (this.data.effectiveControlTest == true) {
      swal.fire({
        icon: 'success',
        title: 'Effective Control Interest Success',
        text: 'Validation passed successfully against the selected ownership',
        showConfirmButton: false,
        timer: 1800,
      })
    } if (this.data.effectiveControlTest == false) {
      swal.fire({
        icon: 'error',
        title: 'Effective Control Interest Failed',
        text: 'Validation failed against the selected ownership',
        showConfirmButton: false,
        timer: 1800,
      })
    }
    setTimeout(() => {
      this.brotherSisterTest();
    }, 2000);

  }

  showSubmit = false;
  successbrotherSisterTest() {
    if (this.data.ControlTest == true && this.data.effectiveControlTest == true) {
      swal.fire({
        icon: 'success',
        title: 'Brother-Sister Success',
        text: 'Validation passed successfully against the selected ownership',
        showConfirmButton: false,
        timer: 1800,
      })
      this.showSubmit = true;
    } if (this.data.ControlTest == false || this.data.effectiveControlTest == false) {
      swal.fire({
        icon: 'error',
        title: 'Brother-Sister Failed',
        text: 'Validation failed against the selected ownership',
        showConfirmButton: false,
        timer: 1800,
      })
      this.showSubmit = true;
    }

  }
}