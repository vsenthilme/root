import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from 'src/app/core/Auth/auth.service';

@Component({
  selector: 'app-delivery-tab',
  templateUrl: './delivery-tab.component.html',
  styleUrls: ['./delivery-tab.component.scss']
})
export class DeliveryTabComponent implements OnInit {

 
  link = [
    { url: "/main/delivery/driver", title: "Driver",  class: "mx-1 d-flex justify-content-center align-items-center" },
    { url: "/main/delivery/vehicle", title: "Vehicle",  class: "mx-1 d-flex justify-content-center align-items-center" },
    { url: "/main/delivery/route", title: "Route",  class: "mx-1 d-flex justify-content-center align-items-center" },
    { url: "/main/delivery/drivervehicleassign", title: "Driver Vechile Assignment",  class: "mx-1 d-flex justify-content-center align-items-center" },
   
    //{ url: "/main/productsetup/variant", title: "Variant",  class: "mx-1 d-flex justify-content-center align-items-center" },
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
