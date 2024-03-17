import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-partnernumbering-new',
  templateUrl: './partnernumbering-new.component.html',
  styleUrls: ['./partnernumbering-new.component.scss']
})
export class PartnernumberingNewComponent implements OnInit {

 
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
