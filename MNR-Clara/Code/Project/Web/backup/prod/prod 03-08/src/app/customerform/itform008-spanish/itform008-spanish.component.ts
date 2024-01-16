import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-itform008-spanish',
  templateUrl: './itform008-spanish.component.html',
  styleUrls: ['./itform008-spanish.component.scss']
})
export class Itform008SpanishComponent implements OnInit {

  screenid: 1122 | undefined;
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
  constructor() { }


  ngOnInit(): void {
  }

}

