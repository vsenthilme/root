import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-uploadoutscan',
  templateUrl: './uploadoutscan.component.html',
  styleUrls: ['./uploadoutscan.component.scss']
})
export class UploadoutscanComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }
  selectRider: '';

  onInputChange(e){
console.log(this.selectRider.length)
  }
}
