import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-upload-excel',
  templateUrl: './upload-excel.component.html',
  styleUrls: ['./upload-excel.component.scss']
})
export class UploadExcelComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }
  selectRider: '';
  selecthub: '';

  onInputChange(e){
console.log(this.selectRider.length)
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

