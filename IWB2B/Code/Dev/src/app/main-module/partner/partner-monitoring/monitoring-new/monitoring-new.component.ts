import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-monitoring-new',
  templateUrl: './monitoring-new.component.html',
  styleUrls: ['./monitoring-new.component.scss']
})
export class MonitoringNewComponent implements OnInit {

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


