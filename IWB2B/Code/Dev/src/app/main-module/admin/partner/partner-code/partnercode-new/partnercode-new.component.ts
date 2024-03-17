import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-partnercode-new',
  templateUrl: './partnercode-new.component.html',
  styleUrls: ['./partnercode-new.component.scss']
})
export class PartnercodeNewComponent implements OnInit {

 
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
