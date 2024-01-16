

import { Component, OnDestroy, Input, SimpleChanges, OnInit, Injectable } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
// import { Observable, timer } from 'rxjs';

@Component({
  selector: 'app-timer',
  templateUrl: './timer.component.html',
  styleUrls: ['./timer.component.scss']
})

@Injectable({
  providedIn: 'root'
})

export class TimerComponent implements OnInit {
  clock: any;
  minutes: any = '00';
  seconds: any = '00';
  hours: any = '00';
  counter: number = 0;
  timerRef: any;
  running: boolean = false;
  startText = 'play_arrow';
  minutelyCharge = 0;
  oldTimeTicketHours = 0;

  @Input() start: boolean;
  @Input() showTimerControls: boolean;
  @Input() bookedHours: FormGroup;

  constructor() {

  }
  ngOnInit(): void {
    this.oldTimeTicketHours = this.bookedHours.controls.timeTicketHours.value;
  }
  ngOnChanges(changes: SimpleChanges) {
    if (changes['start'].currentValue) {
      this.startTimer();
    }
    else {
      this.clearTimer();
    }
  }

  startTimer() {
    this.running = !this.running;
    if (this.running) {
      this.startText = 'pause';
      this.bookedHours.controls.timerValidation.setValue(true);
      const startTime = Date.now() - (this.counter || 0);
      this.timerRef = setInterval(() => {
        this.counter = Date.now() - startTime;
        this.transform();
        if (Number(this.minutes) == 0 && Number(this.hours) == 0) {
          this.bookedHours.controls.timeTicketHours.setValue(0.1);
        }
      });
    } else {
      this.startText = 'play_arrow';
      this.bookedHours.controls.timerValidation.setValue(false);
      clearInterval(this.timerRef);
      this.calculate();
      this.bookedHours.controls.timeTicketHours.setValue(this.minutelyCharge + this.oldTimeTicketHours);
    }
  }

  transform(): string {
    this.seconds = Math.floor((this.counter / 1000) % 60),
    this.minutes = Math.floor((this.counter / (1000 * 60)) % 60),
    this.hours = Math.floor((this.counter / (1000 * 60 * 60)) % 24);

    if (Number(this.minutes) < 10) {
      this.minutes = '0' + this.minutes;
    } else {
      this.minutes = '' + this.minutes;
    }
    if (Number(this.hours) < 10) {
      this.hours = '0' + this.hours;
    } else {
      this.hours = '' + this.hours;
    }
    if (Number(this.seconds) < 10) {
      this.seconds = '0' + this.seconds;
    } else {
      this.seconds = '' + this.seconds;
    }
    return this.hours + ':' + this.minutes + ':' + this.seconds;
  }


  clearTimer() {
    this.calculate();
    this.running = false;
    this.startText = 'play_arrow';
    this.counter = 0;
    this.hours = '00';
    this.seconds = '00';
    this.minutes = '00';

    clearInterval(this.timerRef);
    this.bookedHours.controls.timeTicketHours.setValue(this.oldTimeTicketHours);
  }

  calculate() {
    let clockedMinute = Number(this.minutes);
    let clockedSecond = Number(this.seconds);
    let hours = Number(this.hours);
    if (clockedMinute >= 0 && ((clockedMinute < 6) || (clockedMinute <= 6 && clockedSecond == 0))) {
      this.minutelyCharge = 0.1;
    } else if (clockedMinute >= 6 && ((clockedMinute < 12) || (clockedMinute <= 12 && clockedSecond == 0))) {
      this.minutelyCharge = 0.2;
    } else if (clockedMinute >= 12 && ((clockedMinute < 18) || (clockedMinute <= 18 && clockedSecond == 0))) {
      this.minutelyCharge = 0.3;
    } else if (clockedMinute >= 18 && ((clockedMinute < 24) || (clockedMinute <= 24 && clockedSecond == 0))) {
      this.minutelyCharge = 0.4;
    } else if (clockedMinute >= 24 && ((clockedMinute < 30) || (clockedMinute <= 30 && clockedSecond == 0))) {
      this.minutelyCharge = 0.5;
    } else if (clockedMinute >= 30 && ((clockedMinute < 36) || (clockedMinute <= 36 && clockedSecond == 0))) {
      this.minutelyCharge = 0.6;
    } else if (clockedMinute >= 36 && ((clockedMinute < 42) || (clockedMinute <= 42 && clockedSecond == 0))) {
      this.minutelyCharge = 0.7;
    } else if (clockedMinute >= 42 && ((clockedMinute < 48) || (clockedMinute <= 48 && clockedSecond == 0))) {
      this.minutelyCharge = 0.8;
    } else if (clockedMinute >= 48 && ((clockedMinute < 54) || (clockedMinute <= 54 && clockedSecond == 0))) {
      this.minutelyCharge = 0.9;
    } else if (clockedMinute >= 54 && ((clockedMinute < 59) || (clockedMinute <= 59 && clockedSecond == 0))) {
      this.minutelyCharge = 1;
    }
    this.minutelyCharge = this.minutelyCharge + hours;
  }

  ngOnDestroy() {
    clearInterval(this.timerRef);
  }

}