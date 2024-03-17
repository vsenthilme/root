import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-userprofile-new',
  templateUrl: './userprofile-new.component.html',
  styleUrls: ['./userprofile-new.component.scss']
})
export class UserprofileNewComponent implements OnInit {
  profileType: any[];
  userName: any[];
  constructor() {
    this.profileType = [
      {value: 'Admin', label: 'Admin'},
      {value: 'Operation', label: 'Operation'},
      {value: 'Report User', label: 'Report User'},
      {value: 'Type - 04', label: 'Type - 04'},
  ];

  this.userName= [
    {value: 'IWE_ADMIN', label: 'IWE_ADMIN'},
    {value: 'IWE_OPR', label: 'IWE_OPR'},
    {value: 'IWE_REP', label: 'IWE_REP'},
];
   }

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
