import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from 'src/app/core/core';

@Component({
  selector: 'app-setting-tab',
  templateUrl: './setting-tab.component.html',
  styleUrls: ['./setting-tab.component.scss']
})
export class SettingTabComponent implements OnInit {

  
  
  link = [
    { url: "/main/settings/userProfile", title: "User Profile",  icon: "fas fa-user font_1-5 me-2", class: "mx-1 hovertab  d-flex justify-content-center align-items-center" },
   // { url: "/main/settings/userProfile", title: "User Profile",  icon: "fas fa-user font_1-5 me-2", class: "mx-1 hovertab  d-flex justify-content-center align-items-center" },
 ];
 activeLink = this.link[0].url;
 
 constructor(private router: Router, private auth: AuthService) { 
   this.router.events.pipe(
     filter((event: any) => event instanceof NavigationEnd),
   ).subscribe(x => {
     this.activeLink = this.router.url;
   });
 }

 ngOnInit(): void {
   this.activeLink = this.router.url;
 }
}

 