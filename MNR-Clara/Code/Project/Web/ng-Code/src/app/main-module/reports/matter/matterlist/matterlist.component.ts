import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-matterlist',
  templateUrl: './matterlist.component.html',
  styleUrls: ['./matterlist.component.scss']
})
export class MatterlistComponent implements OnInit {
  isChecked = true;
  isShowDiv = false;
  table = false;
  div1Function(){
    this.table = !this.table;
  }
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
  toggleFloat() {

    this.isShowDiv = !this.isShowDiv;
    this.toggle = !this.toggle;

    if (this.icon === 'expand_more') {
      this.icon = 'chevron_left';
    } else {
      this.icon = 'expand_more'
    }
    this.showFloatingButtons = !this.showFloatingButtons;

  }
  showFiller = false;
  constructor() { }

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
}