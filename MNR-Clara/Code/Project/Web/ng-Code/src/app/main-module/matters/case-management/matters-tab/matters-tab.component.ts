import { Component, OnInit } from "@angular/core";
import { ThemePalette } from "@angular/material/core";
import { ActivatedRoute, NavigationEnd, Router } from "@angular/router";
import { filter } from "rxjs/operators";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";


@Component({
  selector: 'app-matters-tab',
  templateUrl: './matters-tab.component.html',
  styleUrls: ['./matters-tab.component.scss']
})
export class MattersTabComponent implements OnInit {
  background: ThemePalette = undefined;
  // menu = [1105, 1023, 1049]
  toggleBackground() {
    this.background = this.background ? undefined : 'primary';
  }

  code: any = this.cs.decrypt(sessionStorage.getItem('matter')).code;
  code1: any = this.cs.decrypt(sessionStorage.getItem('matter')).code1;
  codes: any = sessionStorage.getItem('matter');
  listmenu = [
    // { url: "/main/setting/business/activity", title: "Activity" },

    { url: "/main/matters/case-management/matter/" + this.codes, title: "General", screenid: 1099, icon: "fas fa-folder font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue", pageFlow: 'Display' },
    { url: "/main/matters/case-management/accounting/" + this.codes, title: "Billing", screenid: 1107, icon: "fas fa-dollar-sign font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue", pageFlow: 'Display' },
    { url: "/main/matters/case-management/notes/" + this.codes, title: "Notes", screenid: 1100, icon: "fas fa-clipboard font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue",  pageFlow: 'Display' },
    { url: "/main/matters/case-management/document/" + this.codes, title: "DocuSign Documents", screenid: 1104, icon: "fas fa-file-alt font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue",  pageFlow: 'Display'  },
    { url: "/main/matters/case-management/rate/" + this.codes, title: "Rate", screenid: 1108, icon: "fas fa-file-invoice-dollar font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue", pageFlow: 'Display' },
    { url: "/main/matters/case-management/feessharing/" + this.codes, title: "Fees Sharing", screenid: 1110, icon: "fas fa-hand-holding-usd font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue", pageFlow: 'Display' },
    { url: "/main/matters/case-management/expenses/" + this.codes, title: "Expense", screenid: 1112, icon: "fas fa-search-dollar font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue", pageFlow: 'Display' },
    { url: "/main/matters/case-management/intake/" + this.codes, title: "Intake", screenid: 1126, icon: "fas fa-file-signature font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue", pageFlow: 'Display' },

    //mugilan

    { url: "/main/matters/case-management/tasklist/" + this.codes, title: "Task", screenid: 1114, icon: "far fa-calendar-check font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue", pageFlow: 'Display' },
    { url: "/main/matters/case-management/timeticket/" + this.codes, title: "Time Ticket", screenid: 1116, icon: "far fa-clock font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue", pageFlow: 'Display' },
    { url: "/main/matters/case-management/receiptno/" + this.codes, title: "Receipt No", screenid: 1132, icon: "fas fa-file-invoice font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue", pageFlow: 'Display' },
    { url: "/main/matters/case-management/expirationdate/" + this.codes, title: "Expiration Date", screenid: 1130, icon: "far fa-calendar-times font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue", pageFlow: 'Display' },

    
    { url: "/main/matters/case-management/clientdocument/" + this.codes, title: "Client Portal Documents", screenid: 1157,  icon: "fas fa-file-signature font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue", pageFlow: 'Display' },
    { url: "/main/matters/case-management/documenttab/" + this.codes, title: "Client Portal Checklist", screenid: 1168,  icon: "fas fa-file-signature font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue", pageFlow: 'Display' },



    // { url: "/main/matters/case-management/documenttab", title: "Client Portal Documents", screenid: 1142, icon: "fas fa-file-signature font_1-5 me-2", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue" },

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
      this.activeLink = l.filter(x => x.url.includes(this.router.url.substring(0, 40)))[0]?.title;
    });
  }

  ngOnInit(): void {
    let l = this.listmenu;

    this.activeLink = l.filter(x => x.url.includes(this.router.url.substring(0, 40)))[0]?.title;

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
