import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from 'src/app/core/core';

@Component({
  selector: 'app-admin-tab',
  templateUrl: './admin-tab.component.html',
  styleUrls: ['./admin-tab.component.scss']
})
export class AdminTabComponent implements OnInit {

  link = [
    { url: "/main/admin/userprofile", title: "User Profile  ", screenid: 3082, class: "mx-1 hovertab  d-flex justify-content-center align-items-center" },
    { url: "/main/admin/system-type", title: "System ", screenid: 3082, class: "mx-1 hovertab  d-flex justify-content-center align-items-center" },
    { url: "/main/admin/partnercode", title: "Partner ", screenid: 3082, class: "mx-1 hovertab  d-flex justify-content-center align-items-center" },
    { url: "/main/admin/userCreation", title: "User Creation ", screenid: 3082, class: "mx-1 hovertab  d-flex justify-content-center align-items-center" },
    { url: "/main/admin/clientAssignment", title: "Client Assignment ", screenid: 3082, class: "mx-1 hovertab  d-flex justify-content-center align-items-center" },
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

 