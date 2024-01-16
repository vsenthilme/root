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
    private messageService: MessageService,
    private router: Router,
    private spin: NgxSpinnerService,
    public toastr: ToastrService

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
        this.http.get<any>(`/iwe-integration-service/login?userName=${user.userName}&password=${user.password}`).subscribe(
          (res) => {
            console.log(res);
            this.spin.hide();
            localStorage.setItem("user", JSON.stringify(res));
            this.router.navigate(["main/dashboard"]);

          }
          ,
          (err) => {
            this.spin.hide();
            console.log(err)
          this.messageService.add({key: 'br', severity:'error', summary: 'Error', detail: err.error.error});
            resolve(false);
          }
        )
      );
    });







  }
  // RefreshToken() {
  //   var use2r = {
  //     clientId: "pixeltrice",
  //     clientSecretKey: "pixeltrice-secret-key",
  //     grantType: "password",
  //     oauthPassword: "welcome",
  //     oauthUserName: "muru"
  //   }


  //   this.http.post<any>(`/auth-token`, use2r).subscribe(
  //     (res) => {
  //       console.log(res);
  //       this.spin.hide();
  //       // let resp: any = res.result.response[0];
  //       // // For token
  //       // localStorage.setItem("token", res.result.bearerToken);
  //       // sessionStorage.setItem("token", res.result.bearerToken);
  //       localStorage.setItem('token', res.access_token);
  //       this.token = res.access_token;
  //       var token = res.access_token;
  //       return this.token;
  //     }
  //     ,
  //     (rej) => {
  //       this.spin.hide();
  //       // this.toastr.error(rej.result.error_description, "Error");
  //     }
  //   );
  // }

  refreshToken(apiName: string) {
    return this.http.post<any>('/iwe-integration-service/auth-token', {
      clientId: "pixeltrice",
      clientSecretKey: "pixeltrice-secret-key",
      grantType: "password",
      oauthPassword: apiName == 'b2b-portal-service' ? "IWMVP" : 'test',
      oauthUserName: apiName == 'b2b-portal-service' ? "IWMVP" : 'test',
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
  get userID() {
    return JSON.parse(localStorage.getItem("user") as '{}').userId;
    //return JSON.parse(localStorage.getItem("user") as '{}').username;
  }
  get plantId() {
    return JSON.parse(localStorage.getItem("user") as '{}').plantId;
    //return JSON.parse(localStorage.getItem("user") as '{}').username;
  }
  get warehouseId() {
    return JSON.parse(localStorage.getItem("user") as '{}').warehouseId;
    //return JSON.parse(localStorage.getItem("user") as '{}').username;
  }
  get companyId() {
    return JSON.parse(localStorage.getItem("user") as '{}').companyCode;
    //return JSON.parse(localStorage.getItem("user") as '{}').username;
  }
  get languageId() {
    return JSON.parse(localStorage.getItem("user") as '{}').languageId;
    //return JSON.parse(localStorage.getItem("user") as '{}').username;
  }
  get username() {
    return JSON.parse(localStorage.getItem("user") as '{}')
      ? JSON.parse(localStorage.getItem("user") as '{}').username
      : null;


  }

  get userType() {
    return JSON.parse(localStorage.getItem("user") as '{}').userType;
  }

  get userTypeId() {
    return JSON.parse(localStorage.getItem("user") as '{}').userTypeId;
  }

  get warehouseType() {
    return JSON.parse(localStorage.getItem("user") as '{}').warehouseType;
  }

  get warehouseTypeName() {
    return JSON.parse(localStorage.getItem("user") as '{}').warehouseTypeName;
  }

  get warehouseList() {
    const warehouseList = JSON.parse(localStorage.getItem("user") as '{}')
      .warehouseList;
    if (warehouseList) {
      return warehouseList;
    } else {
      return [];
    }
  }

  get company() {
    return JSON.parse(localStorage.getItem("user") as '{}').company;
  }

  get companyCurrency() {
    return JSON.parse(localStorage.getItem("user") as '{}').companyCurrency;
  }

  get isAdmin() {
    if (
      this.userType == "SuperAdmin" &&
      this.userID == "1"
    ) {
      return true;
    }
    return false;
  }

  get accessJson() {
    return JSON.parse(localStorage.getItem("user") as '{}').accessJson;
  }

  logout() {
    window.sessionStorage.clear();

    localStorage.clear();
    this.userData = false;
    // const data = {
    //   userName: [this.username],
    //   requestType: "Logout",
    // };
    this.spin.show();
    this.router.navigate(["/"]);
    setTimeout(() => {
      window.location.reload();
  }, 1000);
    this.spin.hide();
    // this.http.post<any>(`Login/Logout`, data).subscribe(
    //   (res) => {

    //     this.router.navigate(["/"]);
    //     this.spin.hide();
    //   },
    //   (err) => {
    //     this.userData = false;
    //     this.router.navigate(["/"]);
    //     this.spin.hide();
    //   }
    // );
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
}
