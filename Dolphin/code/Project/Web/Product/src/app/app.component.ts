import { Component, HostListener } from '@angular/core';
import { Router, NavigationStart, NavigationEnd, NavigationCancel } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { WebSocketAPIService } from './WebSocketAPIService';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Classic WMS';

  constructor(
    private router: Router,
    private spinner: NgxSpinnerService,
    // private auth: AuthService,
    // private rs: RoleAccessService
  ) { }

  ngAfterViewInit() {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.spinner.show();
        // if (this.auth.userID) {
        //   console.log(event.url);
        //   console.log(this.rs.getAccessBySearch(event.url));
        // }
      } else if (
        event instanceof NavigationEnd ||
        event instanceof NavigationCancel
      ) {
        this.spinner.hide();
      }
    });

    // window.addEventListener('beforeunload', (event) => {
    //   console.log(event)
    //   event.returnValue = `You have unsaved changes, leave anyway?`;
    // });
    
    
  }

  
//   ngOnDestroy(): void {
//     window.addEventListener('beforeunload', (event) => {
//       console.log(event)
//       event.returnValue = `You have unsaved changes, leave anyway?`;
//     });
// }
}