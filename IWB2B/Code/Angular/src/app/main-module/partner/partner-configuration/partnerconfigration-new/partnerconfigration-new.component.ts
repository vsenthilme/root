import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-partnerconfigration-new',
  templateUrl: './partnerconfigration-new.component.html',
  styleUrls: ['./partnerconfigration-new.component.scss']
})
export class PartnerconfigrationNewComponent implements OnInit {

 
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


