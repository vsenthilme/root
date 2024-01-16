import { Component, OnInit } from "@angular/core";
import { ThemePalette } from "@angular/material/core";
import { NavigationEnd, Router } from "@angular/router";
import { filter } from "rxjs/operators";


@Component({
  selector: 'app-client-tab',
  templateUrl: './client-tab.component.html',
  styleUrls: ['./client-tab.component.scss']
})
export class ClientTabComponent implements OnInit {
  listmenu = [
    // { url: "/main/setting/business/activity", title: "Activity" },

    { url: "/main/client/general", title: "General", screenid: 1021, icon: "fas fa-folder font_1-5 me-2", class: "blue-text fw-bold  mb-0 px-4  blue" },
    { url: "/main/client/matters", title: "Matters", screenid: 1047, icon: "fas fa-briefcase font_1-5 me-2", class: "blue-text fw-bold  mb-0 px-4 blue " },



    // { url: "/main/client/notes", title: "Notes", screenid: 1023, icon: "fas fa-clipboard font_1-5 me-2", class: "blue-text fw-bold  mb-0 px-4 blue "  },
    // { url: "/main/client/email", title: "Email", screenid: 1053, icon: "fas fa-envelope font_1-5 me-2", class: "blue-text fw-bold  mb-0 px-4  blue"  },
    // { url: "/main/client/documents", title: "Documents", screenid: 1055, icon: "fas fa-file-alt font_1-5 me-2", class: "blue-text fw-bold  mb-0 px-4  blue"  },

  ]
  constructor(private router: Router) {

    this.router.events.pipe(
      filter((event: any) => event instanceof NavigationEnd),
    ).subscribe(x => {
      let l = this.listmenu;
      this.activeLink = l.filter(x => x.url.includes(this.router.url.substring(0, 40)))[0]?.title;
    });
  }
  activeLink = this.listmenu[0].url;
  background: ThemePalette = undefined;
  // menu = [1105, 1023, 1049]
  toggleBackground() {
    this.background = this.background ? undefined : 'primary';
  }
  ngOnInit(): void {
    // this.listmenu = this.listmenu.filter(x => this.menu.includes(x.screenid))
    // this.activeLink = this.router.url;
    // if (this.router.url == '/main/setting/business') {
    //   this.router.navigate([this.listmenu[0].url]);
    //   this.activeLink = this.listmenu[0].url;

    // }
    let l = this.listmenu;

    this.activeLink = l.filter(x => x.url.includes(this.router.url.substring(0, 40)))[0]?.title;
  }

}
