import { Component, OnInit } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { MenuService } from 'src/app/common-service/menu.service';
import { AuthService } from 'src/app/core/core';

@Component({
  selector: 'app-config-tabbar',
  templateUrl: './config-tabbar.component.html',
  styleUrls: ['./config-tabbar.component.scss']
})
export class ConfigTabbarComponent implements OnInit {

  listmenu = [
    { screenid: 1003, url: "/main/setting/configuration/language", title: "Language", icon: "fas fa-globe font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1 mb-0 px-4 blue ",lock: "fa fa-lock ml-3 font_1-5 lockcolour" },

    // { url: "/main/setting/configuration/screen", title: "Screen", icon: "fas fa-file-contract font_1-5 me-2", class: "blue-text fw-bold  mb-0 px-4 blue " },
    { screenid: 1008, url: "/main/setting/configuration/status", title: "Status", icon: "fas fa-check-square font_1-5 me-2", class: "blue-text  fw-bold bg-white nav-border  mb-0 px-4 mx-1 blue",lock: "fa fa-lock ml-3 font_1-5 lockcolour"},

    // { url: "/main/setting/configuration/message", title: "Message", icon: "fas fa-envelope font_1-5 me-2", class: "blue-text fw-bold  mb-0 px-4  blue" },
    { screenid: 1006, url: "/main/setting/configuration/transaction", title: "Transaction", icon: "fas fa-exchange-alt  font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold  mb-0 px-4 mx-1 blue",lock: "fa fa-lock ml-3 font_1-5 lockcolour" },
    { screenid: 1045, url: "/main/setting/configuration/numberrange", title: "Number Range", icon: "fas fa-sort-amount-down font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue ",lock: "fa fa-lock ml-3 font_1-5 lockcolour" },
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
    if (this.router.url == '/main/setting/configuration') {
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
