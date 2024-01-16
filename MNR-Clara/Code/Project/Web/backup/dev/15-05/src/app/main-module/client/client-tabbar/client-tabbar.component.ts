import { Component, OnInit } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { ActivatedRoute, NavigationEnd, NavigationError, NavigationStart, Router, Event } from '@angular/router';
import { filter } from 'rxjs/operators';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';

@Component({
  selector: 'app-client-tabbar',
  templateUrl: './client-tabbar.component.html',
  styleUrls: ['./client-tabbar.component.scss']
})
export class ClientTabbarComponent implements OnInit {
  background: ThemePalette = undefined;
  // menu = [1105, 1023, 1049]
  toggleBackground() {
    this.background = this.background ? undefined : 'primary';
  }
  code: any = this.cs.decrypt(sessionStorage.getItem('client')).code;
  code1: any = this.cs.decrypt(sessionStorage.getItem('client')).code1;
  codes: any = sessionStorage.getItem('client');
  
  listmenu = [
    // { url: "/main/setting/business/activity", title: "Activity" },
    { url: "/main/client/clientupdate/" + sessionStorage.getItem('client'), title: "General", screenid: 1085, icon: "fas fa-folder font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },
    { url: "/main/client/matters/" + sessionStorage.getItem('client'), title: "Matter", screenid: 1088, icon: "fas fa-briefcase font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },
    { url: "/main/client/notes/" + sessionStorage.getItem('client'), title: "Notes", screenid: 1086, icon: "fas fa-clipboard  font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },
    { url: "/main/client/documents/" + sessionStorage.getItem('client'), title: "DocuSign Documents", screenid: 1091, icon: "fas fa-file-alt font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },




    // { url: "/main/matters/case-management/tasklist/", title: "Accounting", screenid :1023, icon: "fas fa-dollar-sign font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },
    // { url: "/", title: "Rates", screenid :1047, icon: "fas fa-file-alt font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },

    // { url: "/", title: "Fees Sharing", screenid :1053, icon: "fas fa-hand-holding-usd font_1-5 me-2", class: "blue-text fw-bold  mb-0 px-4 blue " },
    // { url: "/", title: "Email", screenid :1055, icon: "fas fa-envelope font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },
    // { url: "/", title: "Notes", screenid :1055, icon: "fas fa-clipboard font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },
    // { url: "/", title: "Documents", screenid :1055, icon: "fas fa-file-alt font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },
    // { url: "/", title: "Time Ticket", screenid :1055, icon: "far fa-clock font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },

  ]
  
  activeLink = this.listmenu[0].title;

  constructor(private router: Router,
    private cs: CommonService, private auth: AuthService) {

    this.router.events.pipe(
      filter((event: any) => event instanceof NavigationEnd),
    ).subscribe(x => {
      let l = this.listmenu;
      this.activeLink = l.filter(x => x.url.includes(this.router.url.substring(0, 28)))[0].title;
    });
  }

  ngOnInit(): void {
    let l = this.listmenu;
    console.log( l)
    this.activeLink = l.filter(x => x.url.includes(this.router.url.substring(0, 28)))[0].title;

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
