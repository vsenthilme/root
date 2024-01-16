import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-subsmanagement-new',
  templateUrl: './subsmanagement-new.component.html',
  styleUrls: ['./subsmanagement-new.component.scss']
})
export class SubsmanagementNewComponent implements OnInit {

  constructor() {}
  
  ngOnInit(): void {
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


