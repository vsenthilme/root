import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { not } from '@rxweb/reactive-form-validators';
import { UserIdleService } from 'angular-user-idle';
import { LogoutPopupComponent } from './common-field/dialog_modules/logout-popup/logout-popup.component';
import { WebSocketAPIService } from './WebSocketAPIService';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'MR';
  constructor(
    private userIdle: UserIdleService,
    private router: Router, public dialog: MatDialog
  ) { }

  ngOnInit(): void {
    // this.userIdle.startWatching();
    // this.userIdle.onTimerStart().subscribe(count => console.log(count));
    // this.userIdle.onTimeout().subscribe(() =>
    //   this.deleteDialog(),
    //   );
  }
  stop() {
    this.userIdle.stopTimer();
  }

  stopWatching() {
    this.userIdle.stopWatching();
  }

  startWatching() {
    this.userIdle.startWatching();
  }

  restart() {
    this.userIdle.resetTimer();
  }


  deleteDialog() {
    this.router.navigate([''])
    this.stopWatching()
    const dialogRef = this.dialog.open(LogoutPopupComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '60%',
      //position: { top: '6.5%' },
    });
    dialogRef.afterClosed().subscribe(result => {
      this.router.navigate([''])

      setTimeout(() => {
        window.location.reload();
      }, 1000);

    });
  }

}
