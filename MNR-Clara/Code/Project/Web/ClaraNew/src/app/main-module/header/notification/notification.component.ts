import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BehaviorSubject, Subscribable, Subscription } from 'rxjs';
import { AuthService } from 'src/app/core/Auth/auth.service';
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
  username = this.auth.firstName; 
  @Output() notificationRead = new EventEmitter<boolean>();
  
  constructor(private headerComponent: HeaderComponent, private auth: AuthService,   private service: PrebillService,) { }

  ngOnInit(): void {
    this.subscription = this.headerComponent.notificationArray.subscribe((message: any[])  => {
    (message.sort((a, b) => (a.createdOn > b.createdOn) ? -1 : 1))
      if (message != null && message.length != 0) {
        this.notificationData = message;
      } else {
        this.notificationData = [];
      }
    });
  }

  readAllNotification(){
    this.notificationRead.emit(true);
  }

  

  delete(e){
    this.service.readOnenotification(e.id).subscribe(res=>{
      this.headerComponent.getNotificationCount();
    });
  }
}
