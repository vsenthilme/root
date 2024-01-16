import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-bulk-assign',
  templateUrl: './bulk-assign.component.html',
  styleUrls: ['./bulk-assign.component.scss']
})
export class BulkAssignComponent implements OnInit {

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


