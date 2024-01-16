import { Component, OnInit, ViewChild } from '@angular/core';
import { Table } from 'primeng/table';

export interface  variant1 {
  code:  string;
  employeeName:  string;
  userName:  string;
  password:  string;
  userProfile:  string;
  accessType:  string;
  accessType1:  string;
  accessType2:  string;
} 
const ELEMENT_DATA1:  variant1[] = [
  {code:  '11020030',employeeName:  'Test',userName:  '2',password: '3',userProfile: '3',accessType: '5',
  accessType1: '5',accessType2: '3'}

];
@Component({
  selector: 'app-consignment-new-popup',
  templateUrl: './consignment-new-popup.component.html',
  styleUrls: ['./consignment-new-popup.component.scss']
})
export class ConsignmentNewPopupComponent implements OnInit {
  @ViewChild('consignmentNew') consignmentNew: Table | undefined;

  
  consignmentNew1: any;
  selectedconsignmentNew1 : variant1[];
  constructor() {}
  
  ngOnInit(): void {
    this.consignmentNew1= (ELEMENT_DATA1)
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

