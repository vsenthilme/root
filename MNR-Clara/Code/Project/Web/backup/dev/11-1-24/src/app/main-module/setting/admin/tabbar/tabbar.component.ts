import { Component, OnInit } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from 'src/app/core/core';

@Component({
  selector: 'app-tabbar',
  templateUrl: './tabbar.component.html',
  styleUrls: ['./tabbar.component.scss']
})
export class TabbarComponent implements OnInit {

  listmenu = [{ url: "/main/setting/admin/company", title: "Company", screenid: 1082, icon: "fas fa-user font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },
  { url: "/main/setting/admin/class", title: "Class", screenid: 1019, icon: "fas fa-users font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },
  { url: "/main/setting/admin/intake", title: "Intake", screenid: 1011, icon: "fas fa-file-signature font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },
  // { url: "/main/setting/admin/notification", title: "Notification", screenid: 1059, icon: "fas fa-bell font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },
  //spirgnt 3
  { url: "/main/setting/admin/clientcategory", title: "Client Category", screenid: 1010, icon: "fas fa-user-tag font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },
  { url: "/main/setting/admin/clienttype", title: "Client Type", screenid: 1007, icon: "fas fa-user-check font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },

  { url: "/main/setting/admin/usertype", title: "User Type", screenid: 1005, icon: "fas fa-users font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },
  { url: "/main/setting/admin/userprofile", title: "User Profile", screenid: 1015, icon: "fas fa-user-shield font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },
  { url: "/main/setting/admin/userrole", title: "User Role", screenid: 1013, icon: "fas fa-user-lock font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },
  { url: "/main/setting/admin/glmapping", title: "GL Mapping", screenid: 1138, icon: "fas fa-map-marked-alt font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
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
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  toggleFloat() {

    this.isShowDiv = !this.isShowDiv;
    this.toggle = !this.toggle;

    if (this.icon === 'expand_more') {
      this.icon = 'chevron_left';
    } else {
      this.icon = 'expand_more'
    }
    this.showFloatingButtons = !this.showFloatingButtons;

  }
  // menu = [1105, 1023, 1049]
  toggleBackground() {
    this.background = this.background ? undefined : 'primary';
  }
  ngOnInit(): void {
    // this.listmenu = this.listmenu.filter(x => this.menu.includes(x.screenid))
    this.activeLink = this.router.url;
    if (this.router.url == '/main/setting/admin') {
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