import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-configuration-new',
  templateUrl: './configuration-new.component.html',
  styleUrls: ['./configuration-new.component.scss']
})
export class ConfigurationNewComponent implements OnInit {

 
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


