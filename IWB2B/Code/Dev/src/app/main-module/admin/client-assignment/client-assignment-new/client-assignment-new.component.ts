import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-client-assignment-new',
  templateUrl: './client-assignment-new.component.html',
  styleUrls: ['./client-assignment-new.component.scss']
})
export class ClientAssignmentNewComponent implements OnInit {

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

