import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
@Component({
  selector: 'app-timeticket-notification',
  templateUrl: './timeticket-notification.component.html',
  styleUrls: ['./timeticket-notification.component.scss']
})
export class TimeticketNotificationComponent implements OnInit {

  constructor(    public dialogRef: MatDialogRef<TimeticketNotificationComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    private fb: FormBuilder,
    private cs: CommonService,) { }

  ngOnInit(): void {
    // if(this.data.title == "Congratulations"){
    //   this.pop();
    // }
      
 
  }

  pop(){
    this.confetti({
      angle: this.random(60, 120),
      spread: 100,
      ticks: 5000000,
      particleCount: 200,
      origin: {
          y: 0.6
      }
    });
  }
  random(min: number, max: number) {
    return Math.random() * (max - min) + min;
  }
  confetti(args: any) {
    return window['confetti'].apply(this, arguments);
  }

}
