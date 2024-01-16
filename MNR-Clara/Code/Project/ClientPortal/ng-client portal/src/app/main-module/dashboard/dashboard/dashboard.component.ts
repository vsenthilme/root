import {
  BreakpointObserver
} from "@angular/cdk/layout";
import {
  Component,
  OnInit,
  ViewChild
} from "@angular/core";
import {
  MatSidenav
} from "@angular/material/sidenav";
import {
  NgxSpinnerService
} from "ngx-spinner";
import {
  Subscription
} from "rxjs";
import {
  AuthService
} from "src/app/core/core";
import {
  DashboardService
} from "./dashboard.service";



@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})

export class DashboardComponent {
  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;
  sub = new Subscription();
  quotation: any;
  matter: any;
  paymentPlan: any;
  invoice: any;
  documentsChecklist: any;
  document: any;
  constructor(private observer: BreakpointObserver, private spin: NgxSpinnerService, private auth: AuthService, private service: DashboardService, ) {}
  logout() {
    this.auth.logout();
  }
  ngAfterViewInit() {
    this.observer.observe(['(max-width: 800px)']).subscribe((res) => {
      if (res.matches) {
        this.sidenav.mode = 'over';
        this.sidenav.close();
      } else {
        this.sidenav.mode = 'side';
        this.sidenav.open();
      }
    });
  }

  clientfullName = this.auth.clientfullName;
  clientmiddleName = this.auth.clientmiddleName;
  clientfirstName = this.auth.clientfirstName;
  clientlastName = this.auth.clientlastName;
  clientcontactNo = this.auth.clientcontactNo;
  clientaddress1 = this.auth.clientaddress1;
  clientaddress2 = this.auth.clientaddress2;
  clientcity = this.auth.clientcity;
  clientzipcode = this.auth.clientzipcode;
  suiteDoorNo = this.auth.suiteno

  ngOnInit(): void {
    this.spin.show();
    // this.sub.add(this.service.Getall().subscribe(res => {
    //   let result = res.filter((x: any) => x.clientId == this.auth.clientId)
    //   console.log(result)
    //   result.forEach(element => {
    //     this.quotation = element.quotation;
    //     this.matter = element.matter;
    //     this.paymentPlan = element.paymentPlan;
    //     this.invoice = element.invoice;
    //     this.documents = element.documents
    //   });
    //   this.spin.hide();
    // }))

    this.sub.add(this.service.findClientUser({clientId: [this.auth.clientId]}).subscribe(res => {
      res.forEach(element => {
        this.quotation = element.quotation;
        this.matter = element.matter;
        this.paymentPlan = element.paymentPlan;
        this.invoice = element.invoice;
        this.documentsChecklist = element.documents
        this.document = element.referenceField2
      });
      this.spin.hide();
    }))
  }
}
