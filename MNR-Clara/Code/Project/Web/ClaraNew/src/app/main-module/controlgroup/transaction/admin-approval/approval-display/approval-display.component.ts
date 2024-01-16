import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { ApprovalService } from '../../approval/approval.service';
import { OwnershipService } from '../../ownership/ownership.service';
import { SummaryService } from '../../summary/summary.service';
import { Location } from '@angular/common';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-approval-display',
  templateUrl: './approval-display.component.html',
  styleUrls: ['./approval-display.component.scss']
})
export class ApprovalDisplayComponent implements OnInit {

  public icon = 'expand_more';
  sub = new Subscription();
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  ELEMENT_DATA: any[] = [];
  js: any = {};

  code: any = sessionStorage.getItem('controlGroupsSummary1');
  code1: any = this.cs.decrypt(this.code);
  listmenu = [
    { url: "/main/controlgroup/transaction/proposal1/" + this.code, title: "Assigned", icon: "fas fa-file-alt font_1-5 mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
    { url: this.code1.code.groupTypeId == 1001 ? "/main/controlgroup/transaction/familytemplate/" + this.code : "/main/controlgroup/transaction/brotherSisterRemplate1/" + this.code, title: "Validated", icon: "fas fa-question-circle font_1-5 mx-2 mr-3 font_1-5", class: "blue-text bg-white nav-border fw-bold mx-1  mb-0 px-4 blue " },
  ]
  constructor(public dialog: MatDialog,
    private service: OwnershipService,
    private storePartnerListring: ApprovalService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private router: Router,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService,
    private location: Location,
    private summary: SummaryService,
    private route: ActivatedRoute) {
    this.router.events.pipe(
      filter((event: any) => event instanceof NavigationEnd),
    ).subscribe(x => {
      let l = this.listmenu;
      this.activeLink = l.filter(x => x.url.includes(this.router.url.substring(0, 32)))[0].title;
      console.log(this.router.url.substring(0, 32));
    });
  }
  activeLink = this.listmenu[0].url;
  
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


  ngOnInit(): void {
    console.log(this.code1)
    let l = this.listmenu;
    this.activeLink = l.filter(x => x.url.includes(this.router.url.substring(0, 32)))[0].title;
  }



  back() {
    this.location.back();
  }
}
