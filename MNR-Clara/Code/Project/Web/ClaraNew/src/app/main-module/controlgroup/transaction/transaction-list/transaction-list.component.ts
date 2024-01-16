import { Component, OnInit } from '@angular/core';
import { ThemePalette } from "@angular/material/core";
import { NavigationEnd, Router } from "@angular/router";
import { filter } from "rxjs/operators";
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from "src/app/core/core";

@Component({
  selector: 'app-transaction-list',
  templateUrl: './transaction-list.component.html',
  styleUrls: ['./transaction-list.component.scss']
})
export class TransactionListComponent implements OnInit {

  code: any = sessionStorage.getItem('controlGroupsSummary');
  code1: any = this.cs.decrypt(sessionStorage.getItem('controlGroupsSummary'));

    listmenu = [
      { url: "/main/controlgroup/transaction/ownership/" + this.code,title: "Request", icon: "fas fa-file-alt font_1-5 mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
      { url: "/main/controlgroup/transaction/validation/" + this.code, title: "Validated", icon: "fas fa-question-circle font_1-5 mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
      { url: "/main/controlgroup/transaction/proposed/" + this.code, title: "Assigned", icon: "fas fa-hands-helping font_1-5 mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
      { url: "/main/controlgroup/transaction/approval/" + this.code,  title: "Approval", icon: "fas fa-check-circle font_1-5 mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    ]
    constructor(private router: Router, private auth: AuthService, private cs: CommonService) {
  

      this.router.events.pipe(
        filter((event: any) => event instanceof NavigationEnd),
      ).subscribe(x => {
        let l = this.listmenu;
        this.activeLink = l.filter(x => x.url.includes(this.router.url.substring(0, 32)))[0].title;
        console.log(this.router.url.substring(0, 32));
      });
    }
    activeLink = this.listmenu[0].url;
    background: ThemePalette = undefined;
  
    toggleBackground() {
      this.background = this.background ? undefined : 'primary';
    }
    ngOnInit(): void {
      let l = this.listmenu;
      this.activeLink = l.filter(x => x.url.includes(this.router.url.substring(0, 32)))[0].title;
      console.log(this.activeLink)
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
  


// /" + this.code,