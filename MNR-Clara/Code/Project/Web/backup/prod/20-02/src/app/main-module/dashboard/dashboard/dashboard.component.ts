import { Component, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { Router } from "@angular/router";
import { UserIdleService } from "angular-user-idle";
import { LogoutPopupComponent } from "src/app/common-field/dialog_modules/logout-popup/logout-popup.component";
import { AuthService } from "src/app/core/Auth/auth.service";



@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})

export class DashboardComponent implements OnInit {
  screenid = 1173;
  public icon = 'expand_more';
  
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
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

  disabled = true;
  panelOpenState = false;
  checked = true;
  listmenu = ["Enterprise Setup", "2", "3", "4", "5", "7"]
  constructor(private userIdle: UserIdleService, private router: Router,public dialog: MatDialog, private auth: AuthService) { }
  active = 0;
  pathname = "CRM"
  onTabChange(e) {
    console.log(e)
    if(e == 0){
      this.pathname = "CRM"
    }
    if(e == 1){
      this.pathname = "Matter"
    }
    if(e == 2){
      this.pathname = "Billing"
    }
  }
  RA: any = {};
  ngOnInit(): void {
    // this.userIdle.startWatching();
    
    // this.userIdle.onTimerStart().subscribe(count => console.log(count));
    // this.userIdle.onTimeout().subscribe(() => 
   
    //   this.deleteDialog(),
    //   );

   // this.RA = this.auth.getRoleAccess(this.screenid);
    console.log(this.RA)
  }
  stop() {
    this.userIdle.stopTimer();
  }

 

  startWatching() {
    this.userIdle.startWatching();
  }

  restart() {
    this.userIdle.resetTimer();
  }


  deleteDialog() {
    this.stop()
    const dialogRef = this.dialog.open(LogoutPopupComponent, {
      disableClose: true,
      width: '30%',
      maxWidth: '60%',
      //position: { top: '6.5%' },
    });
    dialogRef.afterClosed().subscribe(result => {
      this.router.navigate([''])
    });
  }
}
