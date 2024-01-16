import { Subscription } from "rxjs";
import { Injectable, OnDestroy } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { catchError, switchMap } from "rxjs/operators";
import { MessageService } from "primeng/api";

@Injectable()
export class AuthService implements OnDestroy {
  redirectUrl: any;
  user: any;
  userData!: boolean;
  token!: string;
  // Subscription
  sub = new Subscription();
  constructor(
    private http: HttpClient,
    private router: Router,
    private spin: NgxSpinnerService,
    public toastr: ToastrService,
    private messageService: MessageService

  ) {

    this.userData = true;


    // if (JSON.parse(localStorage.getItem("user") as '{}')) {
    //   this.userData = true;
    // }
  }

  get isLoggedIn() {
    return this.userData ? true : false;
  }

  login(user: { userName: string; password: any; }) {
    localStorage.clear();
    sessionStorage.clear();
    this.spin.show();

    return new Promise((resolve, reject) => {
      this.sub.add(
        this.http.get<any>(`/mv-master-service/login/validate?userID=${user.userName}&password=${user.password}`).subscribe(
          (res) => {
            console.log(res);
            this.spin.hide();
            localStorage.setItem("user", JSON.stringify(res))
            // let resp: any = res.result.response[0];
            // // For token
            // localStorage.setItem("token", res.result.bearerToken);
            // sessionStorage.setItem("token", res.result.bearerToken);

            this.router.navigate(["main/dashboard"]);

          }
          ,
          (rej) => {
            this.spin.hide();
         // this.toastr.error("", rej.error['error-message']);
            
          this.messageService.add({key: 'br', severity:'error', summary: 'Error', detail: rej.error.error});
            resolve(false);
          }
        )
      );
    });
  }

  refreshToken(apiName: string) {
    return this.http.post<any>('/mv-master-service/auth-token', {
      clientId: "pixeltrice",
      clientSecretKey: "pixeltrice-secret-key",
      grantType: "password",
      oauthPassword: "!wYemVeePee@123",
      oauthUserName: "IWMVP",
      apiName: apiName
    });
  }
  public saveToken(token: any, apiName: string): void {
    window.sessionStorage.removeItem(apiName);
    window.sessionStorage.setItem(apiName, token);
  }
  public saveTokenfrom(token: any, apiName: string): void {
    window.sessionStorage.removeItem(apiName);
    window.sessionStorage.setItem(apiName, token.access_token);
  }

  public getToken(apiName: string): string | null {
    return window.sessionStorage.getItem(apiName);
  }


  get accessJson() {
    return JSON.parse(localStorage.getItem("user") as '{}').accessJson;
  }

  logout() {
    window.sessionStorage.clear();

    localStorage.clear();
    this.userData = false;
    this.spin.show();
    this.router.navigate(["/"]);

    this.spin.hide();
  }

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }
  }
  // get dateformat() {
  //   if (this.userData) {
  //     return this.company.dateformat;
  //   }
  //   return "";
  // }

  get firstname() {
    return JSON.parse(localStorage.getItem("user") as '{}').firstname;
  }
  get lastname() {
    return JSON.parse(localStorage.getItem("user") as '{}').lastname;
  }
  get role() {
    return JSON.parse(localStorage.getItem("user") as '{}').role;
  }
  get userTypeId() {
    return JSON.parse(localStorage.getItem("user") as '{}').userTypeId;
  }
  get email() {
    return JSON.parse(localStorage.getItem("user") as '{}').email;
  }
  get phoneNo() {
    return JSON.parse(localStorage.getItem("user") as '{}').phoneNo;
  }
  get city() {
    return JSON.parse(localStorage.getItem("user") as '{}').city;
  }
  get state() {
    return JSON.parse(localStorage.getItem("user") as '{}').state;
  }
  get country() {
    return JSON.parse(localStorage.getItem("user") as '{}').country;
  }
  get username() {
    return JSON.parse(localStorage.getItem("user") as '{}').username;
  }
  get customerId() {
    return JSON.parse(localStorage.getItem("customer") as '{}').customerId;
  }
}
