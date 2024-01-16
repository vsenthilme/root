import { BreakpointObserver } from "@angular/cdk/layout";
import { Component, OnInit, ViewChild } from "@angular/core";
import { MatSidenav } from "@angular/material/sidenav";
import { AuthService } from "src/app/core/core";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  username = this.auth.clientfullName;

  authclassId = this.auth.classId;

  
  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;
 
   constructor(private observer: BreakpointObserver, private auth: AuthService) {}
  ngOnInit(): void {
  }
   logout() {
     this.auth.logout();
   }
   ngAfterViewInit() {
     this.observer.observe(['(max-width: 800px)']).subscribe((res) => {
       if (res.matches) {
         this.sidenav.mode = 'over';
         this.sidenav.close();
       } else {
         this.sidenav.mode = 'side';
         this.sidenav.open();
       }
     });
   }
 }