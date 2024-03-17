import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-subscription-new',
  templateUrl: './subscription-new.component.html',
  styleUrls: ['./subscription-new.component.scss']
})
export class SubscriptionNewComponent implements OnInit {

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


