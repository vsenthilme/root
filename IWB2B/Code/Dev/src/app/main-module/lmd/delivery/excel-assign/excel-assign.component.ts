import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-excel-assign',
  templateUrl: './excel-assign.component.html',
  styleUrls: ['./excel-assign.component.scss']
})
export class ExcelAssignComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }
  selectRider: '';

  onInputChange(e){
console.log(this.selectRider.length)
  }
}
