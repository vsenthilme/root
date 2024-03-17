import { Component, OnInit } from '@angular/core';
import { ThemePalette } from "@angular/material/core";
import { NavigationEnd, Router } from "@angular/router";
import { filter } from "rxjs/operators";
import { AuthService } from "src/app/core/core";
@Component({
  selector: 'app-idmasterlist',
  templateUrl: './idmasterlist.component.html',
  styleUrls: ['./idmasterlist.component.scss']
})
export class IdmasterlistComponent implements OnInit {

  listmenu = [
    // { url: "/main/controlgroup/idmaster/language", title: "Language", icon: "fas fa-globe  font_1-5 mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    // { url: "/main/controlgroup/idmaster/company", title: "Company",  icon: "fas fa-building mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/controlgroup/idmaster/country", title: "Country",  icon: "fas fa-globe-africa mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/controlgroup/idmaster/state", title: "State",  icon: "fas fa-landmark font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/controlgroup/idmaster/city", title: "City",  icon: "fas fa-city fa-city mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/controlgroup/idmaster/status", title: "Status",  icon: "fas fa-check-square mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/controlgroup/idmaster/numberrange", title: "Number Range ",  icon: "fas fa-sort-amount-down mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/controlgroup/idmaster/store", title: "Store",  icon: "fas fa-store mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/controlgroup/master/client", title: "Co-Owner",  icon: "fas fa-user  font_1-5 mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/controlgroup/idmaster/relationship", title: "Relationship",  icon: "fas fa-handshake mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/controlgroup/idmaster/entity", title: "Entity",  icon: "fas fa-users-cog mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/controlgroup/idmaster/controlgrouptype", title: "Group Type ",  icon: "fas fa-users mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    //{ url: "/main/controlgroup/idmaster/subgroup", title: "Sub Group Type",  icon: "fas fa-user-friends mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/controlgroup/master/controlgroup", title: "Group",  icon: "fas fa-people-carry mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/controlgroup/master/clientcontrolgroup", title: "Group Mapping",  icon: "fas fa-tasks mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
   // { url: "/main/controlgroup/master/cliententityassignment", title: "Client Store Assignment ",  icon: "fas fa-chalkboard-teacher mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
   
   
   
  ]
  constructor(private router: Router, private auth: AuthService) {

    this.router.events.pipe(
      filter((event: any) => event instanceof NavigationEnd),
    ).subscribe(x => {
      this.activeLink = this.router.url;
    });
  }
  activeLink = this.listmenu[0].url;
  background: ThemePalette = undefined;

  toggleBackground() {
    this.background = this.background ? undefined : 'primary';
  }
  ngOnInit(): void {

    this.activeLink = this.router.url;
    if (this.router.url == '/main/controlgroup/idmaster') {
      this.router.navigate([this.listmenu[0].url]);
      this.activeLink = this.listmenu[0].url;

    }

  }
  isdiabled(id: any) {
    this.auth.isMenudata();

    if (id == 6)
      return false;
    let fileterdata = this.auth.MenuData.filter((x: any) => x.subScreenId == id && (x.view || x.delete || x.createUpdate));

    if (fileterdata.length > 0) {
      return false;
    }


    return true;
  }

}
