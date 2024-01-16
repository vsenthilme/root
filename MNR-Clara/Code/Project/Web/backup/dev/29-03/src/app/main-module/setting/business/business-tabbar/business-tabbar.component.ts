import { Component, OnInit, ViewChild, ElementRef } from "@angular/core";
import { ThemePalette } from "@angular/material/core";
import { NavigationEnd, Router } from "@angular/router";
import { filter } from "rxjs/operators";
import { AuthService } from "src/app/core/core";


@Component({
  selector: 'app-business-tabbar',
  templateUrl: './business-tabbar.component.html',
  styleUrls: ['./business-tabbar.component.scss']
})
export class BusinessTabbarComponent implements OnInit {
  listmenu = [
    // { url: "/main/setting/business/activity", title: "Activity" },

    { url: "/main/setting/business/casecategory", title: "Case Category", screenid: 1021, icon: "fas fa-folder font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/setting/business/casesubcategory", title: "Case Sub Category", screenid: 1023, icon: "fas fa-folder-open font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/setting/business/agreementtemplate", title: "Agreement Template", screenid: 1047, icon: "fas fa-file-alt font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/setting/business/inquirymode", title: "Inquiry Mode", screenid: 1053, icon: "fas fa-external-link-alt font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/setting/business/referral", title: "Referral", screenid: 1055, icon: "fas fa-retweet font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/setting/business/notetype", title: "Note Type", screenid: 1057, icon: "fas fa-clipboard font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    // sprint 3

    { url: "/main/setting/business/timekeeper", title: "Time Keeper", screenid: 1017, icon: "fas fa-business-time font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/setting/business/activity", title: "Activity", screenid: 1025, icon: "fas fa-clipboard font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/setting/business/tasktype", title: "Task Type", screenid: 1033, icon: "far fa-calendar-alt font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/setting/business/taskbased", title: "Task Based", screenid: 1027, icon: "far fa-calendar-check font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/setting/business/expense", title: "Expense", screenid: 1031, icon: "far fa-credit-card font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/setting/business/deadlinecalculator", title: "Deadline Calculator", screenid: 1043, icon: "far fa-calendar-times font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/setting/business/documenttemplate", title: "Document Template", screenid: 1049, icon: "fas fa-file-alt  font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },

    { url: "/main/setting/business/documentchecklist", title: " Checklist Template", screenid: 1128 , icon: "fas fa-tasks font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },

    { url: "/main/setting/business/coc", title: "Chart Of Accounts", screenid: 1029, icon: "fas fa-chart-line font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/setting/business/admincost", title: "Admin Cost", screenid: 1035, icon: "fas fa-donate  font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/setting/business/billingmode", title: "Billing Mode", screenid: 1037, icon: "fas fa-file-invoice  font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/setting/business/billingfrequency", title: "Billing Frequency", screenid: 1039, icon: "fas fa-file-alt  font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: "/main/setting/business/billingformat", title: "Billing Format", screenid: 1041, icon: "fas fa-file-alt  font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },

    { url: "/main/setting/business/expirationdocument", title: "Expiration Document Type", screenid: 1129, icon: "far fa-calendar-times  font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    // { url: "/main/setting/business/glmapping", title: "GL Mapping", screenid: 1138, icon: "fas fa-map-marked-alt font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },

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
  // menu = [1105, 1023, 1049]
  toggleBackground() {
    this.background = this.background ? undefined : 'primary';
  }
  ngOnInit(): void {
    // this.listmenu = this.listmenu.filter(x => this.menu.includes(x.screenid))
    this.activeLink = this.router.url;
    if (this.router.url == '/main/setting/business') {
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
