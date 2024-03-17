import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { HomeComponent } from '../home.component';
import { WmsIdmasterService } from '../../wms-idmaster-service.service';
import { AuthService } from 'src/app/core/core';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {

  notificationData: any[] = [];
  subscription: Subscription;
  username = this.auth.username; 
  @Output() notificationRead = new EventEmitter<boolean>();
  
  constructor(private homeComponent: HomeComponent,  private router: Router, private auth: AuthService,   private service: WmsIdmasterService,) { }

  ngOnInit(): void {
    this.subscription = this.homeComponent.notificationArray.subscribe((message: any[])  => {
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

  

  delete(e){
    this.service.readOnenotification(e.id).subscribe(res=>{
      this.homeComponent.getNotificationCount();
    });
  }


  routeTo(path){
    if(path == 'Picking'){
      this.router.navigate(["main/outbound/pickup-main"]);
    }
  }
}
