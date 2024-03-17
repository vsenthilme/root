import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from 'src/app/core/core';

@Component({
  selector: 'app-orders-tab',
  templateUrl: './orders-tab.component.html',
  styleUrls: ['./orders-tab.component.scss']
})
export class OrdersTabComponent implements OnInit {

  link = [
    { url: "/main/orders/ordersMain", title: "Order", screenid: 3082, icon: "fa-solid fa-boxes-stacked font_1-5 me-2", class: "mx-1 hovertab  d-flex justify-content-center align-items-center" },
    { url: "/main/orders/postORder", title: "Post Order", screenid: 3082, icon: "fa-solid fa-boxes-packing font_1-5 me-2", class: "mx-1 hovertab  d-flex justify-content-center align-items-center" },
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

 