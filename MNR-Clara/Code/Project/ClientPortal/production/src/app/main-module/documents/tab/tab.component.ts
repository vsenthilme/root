import { Component, OnInit } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-tab',
  templateUrl: './tab.component.html',
  styleUrls: ['./tab.component.scss']
})
export class TabComponent implements OnInit {

  listmenu = [
    { url: "/main/documents/document-MR", title: "Documents from M&R", icon: " font_1-5 me-2", class: "blue-text  mb-0 px-4  blue" },
  { url: "/main/documents/documentupload", title: "Doucment Upload", icon: " font_1-5 me-2", class: "blue-text  mb-0 px-4  blue" },
  { url: "/main/documents/documents", title: "Document Checklist", icon: " font_1-5 me-2", class: "blue-text  mb-0 px-4 blue" },

  ]
  constructor(private router: Router) {

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

}