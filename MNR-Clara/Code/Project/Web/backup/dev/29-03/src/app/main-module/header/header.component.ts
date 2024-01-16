import { OverlayContainer } from "@angular/cdk/overlay";
import { Component, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { Title } from "@angular/platform-browser";
import { ActivatedRoute, NavigationEnd, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { BehaviorSubject, Subscription } from "rxjs";
import { filter } from "rxjs/operators";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { MenuService, NavItem } from "src/app/common-service/menu.service";
import { AuthService } from "src/app/core/core";
import { WebSocketAPIService } from "src/app/WebSocketAPIService";
import { environment } from "src/environments/environment";
import { PrebillService } from "../accounts/prebill/prebill.service";
import { InquiryUpdate3Component } from "../crm/inquiries/inquiry-update3/inquiry-update3.component";
import { TaskNewComponent } from "../matters/case-management/task/task-new/task-new.component";
import { TimeNewComponent } from "../matters/case-management/time-tickets/time-new/time-new.component";


interface Food {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  snaplist = ['Inquiry Validation', 'Intake Form', 'Prospective Client', 'Agreement List'];
  snaplist_Client = ['Client List'];
  snaplist_Matters = ['General', 'Matter', 'Task List'];
  navItems: NavItem[] = [];
  menulist: number[] = [];
  pageTitle: string = "";
  card = false;
  card_matter = false;
  card_Client = false;
  animal: string | undefined;
  name: string | undefined;
  pageTitle_Agg = "";

  subscription: Subscription;
  notificationCount = 0;

  private notificationDataFromDB = new BehaviorSubject([]);
  notificationArray = this.notificationDataFromDB.asObservable();
  
  currentEnv: string;
  constructor(public dialog: MatDialog, private menu: MenuService, private router: Router,
    private activatedRoute: ActivatedRoute,
    private cas: CommonApiService,
    private spin: NgxSpinnerService, public toastr: ToastrService,
    private titleService: Title, private auth: AuthService,
    private webSocketAPIService: WebSocketAPIService,
    private service: PrebillService,
    private overlayContainer: OverlayContainer
  ) {
    this.currentEnv = environment.name; 

    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
    )
      .subscribe(() => {

        var rt = this.getChild(this.activatedRoute)

        rt.data.subscribe((data: { module: string, title: string }) => {

          this.titleService.setTitle("CLARA - " + data.module)
          this.pageTitle = data.module;

          if (data.title == 'Agreement List')
            this.pageTitle_Agg = data.title;
          else
            this.pageTitle_Agg = data.module;
          this.snap = this.snaplist.includes(data.title);//== 'Inquiry Validation' ? true : false;
          this.snap_Matters = this.snaplist_Matters.includes(data.title);//== 'Inquiry Validation' ? true : false;
          this.snap_Client = this.snaplist_Client.includes(data.title);//== 'Inquiry Validation' ? true : false;
          this.card = false;
          this.card_matter = false;
          this.card_Client = false;
        })
      });
    this.webSocketAPIService._connect();
  }

  inquiryId = 1065;
  inquiryValidate: any = {};

  openDialog(): void {

    this.inquiryValidate = this.auth.getRoleAccess(this.inquiryId);
    if (this.inquiryValidate == null || this.inquiryValidate.view == false || this.inquiryValidate == undefined) {
    } else {
      const dialogRef = this.dialog.open(InquiryUpdate3Component, {
        disableClose: true,
        width: '42rem',
        maxWidth: '80%',
        position: { top: '6.5%' },
        data: { code: null, pageflow: 'Inquiry New - H' }

      });

      dialogRef.afterClosed().subscribe(result => {

       // window.location.reload();
      });
    }



  }
  openDialogTimeTicket(): void {


    this.timeTicketValidate = this.auth.getRoleAccess(this.timeTicket);
    if (this.timeTicketValidate == null || this.timeTicketValidate.view == false || this.timeTicketValidate == undefined) {
    } else {
      const dialogRef = this.dialog.open(TimeNewComponent, {
        disableClose: true,
        width: '70%',
        maxWidth: '80%',
        // position: { top: '6.5%' },
        data: { code: null, pageflow: 'New', matter: null }

      });

      dialogRef.afterClosed().subscribe(result => { });
    }


  }
  openDashboard() {
    this.dashboardValidate = this.auth.getRoleAccess(this.dashboard);
    console.log(this.dashboardValidate)
    if (this.dashboardValidate == null || this.dashboardValidate.view == false || this.dashboardValidate == undefined) {
      // this.toastr.error("You Don't have access to the screen", "Notification", {
      //   timeOut: 2000,
      //   progressBar: false,
      // });
    } else {
      console.log('Dashboard')
      this.router.navigate(['/main/dashboard']);
    }

  }

  prebill = 1137;
  prebillValidate: any = {};

  openPrebillApprove() {
    this.prebillValidate = this.auth.getRoleAccess(this.prebill);
    console.log(this.prebillValidate)
    if (this.prebillValidate == null || this.prebillValidate.view == false || this.prebillValidate == undefined) {
      // this.toastr.error("You Don't have access to the screen", "Notification", {
      //   timeOut: 2000,
      //   progressBar: false,
      // });
    } else {
      this.router.navigate(['/main/accounts/prebilllist/Approve']);
    }
  }

  conflictCheck = 1080;
  conflictCheckValidate: any = {};

  openDialog2(): void {
    this.conflictCheckValidate = this.auth.getRoleAccess(this.conflictCheck);
    console.log(this.conflictCheckValidate)
    if (this.conflictCheckValidate == null || this.conflictCheckValidate.view == false || this.conflictCheckValidate == undefined) {
      // this.toastr.error("You Don't have access to the screen", "Notification", {
      //   timeOut: 2000,
      //   progressBar: false,
      // });
    } else {

      this.router.navigate(['/main/crm/conflictcheck']);
    }

  }
  snap = false;//check with raj
  snap_Matters = false;//check with raj
  snap_Client = false;
  username = this.auth.userfullName;

  dashboardValidate: any = {};
  dashboard = 1173;

  timeTicketValidate: any = {};
  timeTicket = 1117;
  
  prod: boolean;
  uat: boolean;
  ngOnInit(): void {

    if(this.currentEnv == 'prod'){
      this.prod = true;
    }else{
      this.prod = false;
    }

    this.getNotificationCount();
    this.subscription = this.webSocketAPIService.currentMessage.subscribe(message => {
      if (message != "") {
        this.getNotificationCount();
      }
    });

    if (!this.auth.isLoggedIn)
      this.auth.logout();
    // this.navItems = this.menu.getMeuList();k


    // if (sessionStorage.getItem("pagetitle"))
    //   this.pageTitle = sessionStorage.getItem("pagetitle") as string;
    // else
    //   this.pageTitle = "Home";
    this.navItems = this.menu.getMeuList();
    console.log(this.navItems);

    // let url: string = this.router.url;
    // if (!this.pageTitle)
    //   this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
    //     this.router.navigate([url]);
    //   })

    // this.dashboardValidate = this.auth.getRoleAccess(this.dashboard);
    // console.log(this.dashboardValidate)
  }

  getNotificationCount(){
    this.service.getNotificationsDBData(this.auth.userID).subscribe(res=>{
      console.log(res);
      if(res != null && res.length != 0) {
        this.notificationCount = res.length;
      } else {
        this.notificationCount = 0;
      }
      this.notificationDataFromDB.next(res);
    },error=>{
      this.notificationCount = 0;
      this.notificationDataFromDB.next([]);
    });
  }

  notificationMenuClosed(){
    //for remove style to click outside
    this.overlayContainer.getContainerElement().classList.remove('disable-backdrop-click');
  }

  readAllNotifications(event){
    console.log(event)
      //update notifications as read
      if(event == true){
        this.service.markNotificationsAsRead().subscribe(res=>{
          this.notificationCount = 0;
          this.notificationDataFromDB.next([]);
        });
      }
  }

  preventCloseOnClickOut() {
    this.overlayContainer.getContainerElement().classList.add('disable-backdrop-click');
  }

  logout() {
    this.router.navigate(['']);
    setTimeout(() => {
      window.location.reload();
    }, 200);
  }

  getChild(activatedRoute: ActivatedRoute): any {
    if (activatedRoute.firstChild) {
      return this.getChild(activatedRoute.firstChild);
    } else {
      return activatedRoute;
    }

  }

  forcard() {
    if (this.snap_Matters) {
      this.card_matter = true;
      this.dashboardcount();
    }
    else if (this.snap_Client) {
      this.card_Client = true;
      this.dashboardcount();
    }
    else {
      this.card = true;
      this.dashboardcount();
    }

  }
  forcard_list() {
    this.card = false;
    this.card_matter = false;
    this.card_Client = false;
  }
  inquiry: any = 0;
  intake = 0;
  potentialClient = 0;
  agreement = 0;
  agreementReceived = 0;
  agreementResent = 0;
  agreementSent = 0;
  agreementTotal = 0;

  recentClients = 0;
  total = 0;
  active = 0;
  recent = 0;
  conversion = 0;
  ret = 0;
  open = 0;
  filed = 0;
  closed = 0;
  dashboardcount() {
    this.spin.show();

    if (this.card_matter)
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.dashboard_matters.total.url,
        this.cas.dropdownlist.dashboard_matters.ret.url,
        this.cas.dropdownlist.dashboard_matters.open.url,
        this.cas.dropdownlist.dashboard_matters.filed.url,
        this.cas.dropdownlist.dashboard_matters.closed.url,
      ]).subscribe((results: any[]) => {

        this.total = results[0].count;
        this.ret = results[1].count;
        this.open = results[2].count;
        this.filed = results[3].count;
        this.closed = results[4].count;

      }, (err) => {
        this.toastr.error(err, "");
      });
    else if (this.card_Client)
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.dashboard_client.active.url,
        this.cas.dropdownlist.dashboard_client.recentClients.url,
        this.cas.dropdownlist.dashboard_client.total.url,
      ]).subscribe((results: any[]) => {

        this.active = results[0].count;
        this.recentClients = results[1].count;
        this.total = results[2].count;

      }, (err) => {
        this.toastr.error(err, "");
      });
    else
      this.cas.getalldropdownlist([this.cas.dropdownlist.dashboard.inquiry.url,
      this.cas.dropdownlist.dashboard.pcIntakeForm.url,
      this.cas.dropdownlist.dashboard.potentialClient.url,
      this.cas.dropdownlist.dashboard.agreement.url,
      this.cas.dropdownlist.dashboard.agreementReceived.url,
      this.cas.dropdownlist.dashboard.agreementResent.url,
      this.cas.dropdownlist.dashboard.agreementSent.url,
      this.cas.dropdownlist.dashboard.agreementTotal.url,


      ]).subscribe((results: any[]) => {

        this.inquiry = results[0].fiteredCount;
        this.intake = results[1].fiteredCount;
        this.potentialClient = results[2].fiteredCount;
        this.agreement = results[3].fiteredCount;
        this.agreementReceived = results[4].fiteredCount;
        this.agreementResent = results[5].fiteredCount;
        this.agreementSent = results[6].fiteredCount;
        this.agreementTotal = results[7].totalCount;


      }, (err) => {
        this.toastr.error(err, "");
      });
    this.spin.hide();

  }

  // displayname(page_title: string) {
  //   this.pageTitle = page_title;;
  //   sessionStorage.setItem("pagetitle", this.pageTitle);
  // }
  task = 1115
  taskValidate: any = {};

  openTask(data: any = 'New'): void {


    this.taskValidate = this.auth.getRoleAccess(this.task);
    if (this.taskValidate == null || this.taskValidate.view == false || this.taskValidate == undefined) {
    } else {
      const dialogRef = this.dialog.open(TaskNewComponent, {
        disableClose: true,
        width: '70%',
        maxWidth: '80%',
        position: { top: '6.5%' },
        data: { pageflow: data, matter: null, code: null }
      });

      dialogRef.afterClosed().subscribe(result => {
        window.location.reload();
      });
    }
  }
}
