import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from 'src/app/core/core';

@Component({
  selector: 'app-manufactur-tab',
  templateUrl: './manufactur-tab.component.html',
  styleUrls: ['./manufactur-tab.component.scss']
})
export class ManufacturTabComponent implements OnInit {

  
  link = [
    { url: "/main/manufacturing/orderDetails", title: "Order Management",  class: "mx-1 d-flex justify-content-center align-items-center" },
    { url: "/main/manufacturing/cutting", title: "Operation 1",  class: "mx-1 d-flex justify-content-center align-items-center" },
    { url: "/main/manufacturing/modling", title: "Operation 2",  class: "mx-1 d-flex justify-content-center align-items-center" },
    { url: "/main/manufacturing/fabrication", title: "Operation 3",  class: "mx-1 d-flex justify-content-center align-items-center" },
    { url: "/main/manufacturing/assembly", title: "Operation 4",  class: "mx-1 d-flex justify-content-center align-items-center" },
    { url: "/main/manufacturing/delivery", title: "Operation 5",  class: "mx-1 d-flex justify-content-center align-items-center" },
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
