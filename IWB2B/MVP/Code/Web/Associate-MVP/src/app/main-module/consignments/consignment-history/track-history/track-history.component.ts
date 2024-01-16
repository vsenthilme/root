import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { PrimeIcons } from 'primeng/api';

@Component({
  selector: 'app-track-history',
  templateUrl: './track-history.component.html',
  styleUrls: ['./track-history.component.scss']
})
export class TrackHistoryComponent implements OnInit {
  constructor(public spinner: NgxSpinnerService) {}
  events: any[];
  ngOnInit(): void {

    this.spinner.show();

    setTimeout(() => {
      /** spinner ends after 5 seconds */
      this.events = [
        {status: 'Order Creation', date: '10th Feb 2023 10:10 AM', icon: PrimeIcons.SHOPPING_CART, color: '#9C27B0', image: 'game-controller.jpg'},
        {status: 'Pickup Assignment', date: '10th Feb 2023 12:30 PM', icon: 'fa-solid fa-clipboard-check', color: '#FDC60A'},
        {status: 'Picking Completed', date: '10th Feb 2023 4:00 PM', icon: 'fa-solid fa-truck-fast', color: '#673AB7'},
        {status: 'Out of Delivery', date: '11th Feb 2023 09:00 AM', icon: PrimeIcons.CHECK, color: '#01EF03'}
    ];
      this.spinner.hide();
    }, 1000);


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

