import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-createsystem-new',
  templateUrl: './createsystem-new.component.html',
  styleUrls: ['./createsystem-new.component.scss']
})
export class CreatesystemNewComponent implements OnInit {

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

