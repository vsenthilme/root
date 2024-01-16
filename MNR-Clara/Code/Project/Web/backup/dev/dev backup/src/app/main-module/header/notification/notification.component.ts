import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BehaviorSubject, Subscribable, Subscription } from 'rxjs';
import { WebSocketAPIService } from 'src/app/WebSocketAPIService';
import { PrebillService } from '../../accounts/prebill/prebill.service';
import { HeaderComponent } from '../header.component';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {

  notificationData: any[] = [];
  subscription: Subscription;
  
  @Output() notificationRead = new EventEmitter<boolean>();
  
  constructor(private headerComponent: HeaderComponent) { }

  ngOnInit(): void {
    this.subscription = this.headerComponent.notificationArray.subscribe((message: any[])  => {
    (message.sort((a, b) => (a.createdOn > b.createdOn) ? -1 : 1))
      if (message != null && message.length != 0) {
        this.notificationData = message;
        console.log(this.notificationData);
      } else {
        this.notificationData = [];
      }
    });
  }

  readAllNotification(){
    this.notificationRead.emit(true);
  }

}
