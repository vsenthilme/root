import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-system-type-new',
  templateUrl: './system-type-new.component.html',
  styleUrls: ['./system-type-new.component.scss']
})
export class SystemTypeNewComponent implements OnInit {


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
