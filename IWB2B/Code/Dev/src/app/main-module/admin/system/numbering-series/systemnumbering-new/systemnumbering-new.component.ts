import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-systemnumbering-new',
  templateUrl: './systemnumbering-new.component.html',
  styleUrls: ['./systemnumbering-new.component.scss']
})
export class SystemnumberingNewComponent implements OnInit {


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
