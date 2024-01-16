import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-setup-new',
  templateUrl: './setup-new.component.html',
  styleUrls: ['./setup-new.component.scss']
})
export class SetupNewComponent implements OnInit {
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

