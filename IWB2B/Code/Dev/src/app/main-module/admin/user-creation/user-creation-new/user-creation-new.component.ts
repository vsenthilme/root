import { Component, OnInit } from '@angular/core';

  @Component({
    selector: 'app-user-creation-new',
    templateUrl: './user-creation-new.component.html',
    styleUrls: ['./user-creation-new.component.scss']
  })
  export class UserCreationNewComponent implements OnInit {
  
   
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
  
