import { Subscription } from "rxjs";
import { Injectable, OnDestroy } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { catchError, switchMap } from "rxjs/operators";
import { CommonService } from "src/app/common-service/common-service.service";
import { UserRoleService } from "src/app/main-module/setting/admin/user-role/user-role.service";
import { WebSocketAPIService } from "src/app/WebSocketAPIService";
import { LoginComponent } from "src/app/login/login/login.component";

@Injectable()
export class AuthService implements OnDestroy {
  redirectUrl: any;
  user: any;
  userData!: boolean;
  token!: string;
  // Subscription
  sub = new Subscription();
  webSocketAPI: WebSocketAPIService = new WebSocketAPIService();
  constructor(
    private http: HttpClient,
    private router: Router,
    private spin: NgxSpinnerService,
    public toastr: ToastrService,
    private cs: CommonService,
    private service: UserRoleService,
//  /   private email: LoginComponent,

  ) {

    this.userData = true;
    //this.isuserdata();


    // if (JSON.parse(sessionStorage.getItem("user") as '{}')) {
    //   this.userData = true;
    // }
  }

  get isLoggedIn() {
    return sessionStorage.getItem("user") ? true : false;
  }
  get isMenu() {
    return sessionStorage.getItem("menu") ? true : false;
  }
  get MenuData() {
    return JSON.parse(sessionStorage.getItem("menu") as '[]')
  }
  isuserdata() {
    if (!this.isLoggedIn)
      this.logout();

  }
  isMenudata() {
    if (!this.isMenu)
      this.logout();

  }

  getRoleAccess(id: any) {
 
    // debugger
    this.isuserdata();
    this.isMenudata();
    console.log(this.MenuData)
    let fileterdata = this.MenuData.filter((x: any) => x.subScreenId == id && (x.view || x.delete || x.createUpdate));

    if (fileterdata.length > 0) {
      console.log(fileterdata[0]);
      return fileterdata[0];
    }
    else {
   //   this.router.navigate(["main/dashboard"]);
     // this.cs.commonerror("You Don't have access to the screen ");
           this.toastr.error("You Don't have access to the screen", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
    }
  }


  getLoginRoleAccess(id: any) {
 
    // debugger
    this.isuserdata();
    this.isMenudata();
    console.log(this.MenuData)
    let fileterdata = this.MenuData.filter((x: any) => x.subScreenId == id && (x.view || x.delete || x.createUpdate));

    if (fileterdata.length > 0) {
      console.log(fileterdata[0]);
      return fileterdata[0];
    }
  }

  inquiryId = 1060;
  inquiryValidate: any = {};

  
  timeTicketIDSpecificSuers = 1175;
  timeTicketIDSpecificSuersValidate: any = {};

  timeTicketIDGeneral = 1171;
  timeTickeValidateGeneral: any = {};

  

  login(user: { userName: string; password: any; }) {
    sessionStorage.clear();
    localStorage.clear();
    this.spin.show();
    // this.router.navigate(["main/dashboard"]);
    return new Promise((resolve, reject) => {
      this.sub.add(
        this.http.get<any>(`/mnr-setup-service/login?userId=${user.userName}&password=${user.password}`).subscribe(
          (res) => {

            this.spin.hide();

            // let resp: any = res.result.response[0];
            // // For token
            // sessionStorage.setItem("token", res.result.bearerToken);
            // sessionStorage.setItem("token", res.result.bearerToken);
            sessionStorage.setItem("user", JSON.stringify(res));

            this.sub.add(this.service.Get(res.userRoleId).subscribe(resuser => {

              sessionStorage.setItem('menu', JSON.stringify(resuser));

              this.timeTickeValidateGeneral = this.getLoginRoleAccess(this.timeTicketIDGeneral);
              console.log(this.timeTickeValidateGeneral)

        //      this.email.emailValidate()
              if(res.classId == 2){
                this.router.navigate(["/main/matters/case-management/general"]);
              }else{
                this.router.navigate(["/main/accounts/timeticket"]);
              }
              this.spin.hide();
            }, err => {
              this.cs.commonerror(err);
              this.spin.hide();
            }));


       
       //     this.router.navigate(["main/dashboard"]);
            // this.spin.show();

       


          }
          ,
          (rej) => {
            this.spin.hide();
            this.cs.commonerror(rej);
            resolve(false);
          }
        )
      );
    });







  }


  verifyOtp(contactNumber: string, otp: any) {
   // sessionStorage.clear();
    localStorage.clear();
    this.spin.show();
    return new Promise((resolve, reject) => {
      this.sub.add(
        this.http.get<any>(`/mnr-setup-service/login/verifyEmailOTP?userId=${contactNumber}&otp=${otp}`).subscribe(
          (res) => {
            // if (res == true) {
             if(res == true){
              this.sub.add(this.service.Get(this.userRoleId).subscribe(resuser => {

                sessionStorage.setItem('menu', JSON.stringify(resuser));
  
                this.timeTickeValidateGeneral = this.getLoginRoleAccess(this.timeTicketIDGeneral);
                console.log(this.timeTickeValidateGeneral)
                if(res.classId == 2){
                  this.router.navigate(["/main/matters/case-management/general"]);
                }else{
                  this.router.navigate(["/main/accounts/timeticket"]);
                }
                this.spin.hide();
              }, err => {
                this.cs.commonerror(err);
                this.spin.hide();
              }));
             }else{
              this.toastr.error('Invalid Verification Code', "Notification", {
                timeOut: 2000,
                progressBar: false,
              });
             }
          }
          ,
          (rej) => {
            this.spin.hide();
            this.cs.commonerror(rej);
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
  //       
  //       this.spin.hide();
  //       // let resp: any = res.result.response[0];
  //       // // For token
  //       // sessionStorage.setItem("token", res.result.bearerToken);
  //       // sessionStorage.setItem("token", res.result.bearerToken);
  //       sessionStorage.setItem('token', res.access_token);
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
    return this.http.post<any>('/auth-token', {
      clientId: "pixeltrice",
      clientSecretKey: "pixeltrice-secret-key",
      grantType: "password",
      oauthPassword: "test",
      oauthUserName: "test",
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
    if (sessionStorage.getItem("user"))
      return JSON.parse(sessionStorage.getItem("user") as '{}').userId;
    return 'raj';
    // return JSON.parse(sessionStorage.getItem("user") as '{}').userId;
  }
  get languageId() {
    if (sessionStorage.getItem("user"))
      return JSON.parse(sessionStorage.getItem("user") as '{}').languageId;
  }
  get defaultRate() {
    if (sessionStorage.getItem("user"))
      return JSON.parse(sessionStorage.getItem("user") as '{}').defaultRate;
    return 'raj';
    // return JSON.parse(sessionStorage.getItem("user") as '{}').userId;
  }


  get userRoleId() {
    if (sessionStorage.getItem("user"))
      return JSON.parse(sessionStorage.getItem("user") as '{}').userRoleId;
    return 'raj';
    // return JSON.parse(sessionStorage.getItem("user") as '{}').userId;
  }

  get classId() {
    if (sessionStorage.getItem("user"))
      return JSON.parse(sessionStorage.getItem("user") as '{}').classId;
  }

  get username() {
    return JSON.parse(sessionStorage.getItem("user") as '{}')
      ? JSON.parse(sessionStorage.getItem("user") as '{}').username
      : null;
  }
  get userclassId() {
    return JSON.parse(sessionStorage.getItem("user") as '{}').classId;
  }
  get userfullName() {
    return JSON.parse(sessionStorage.getItem("user") as '{}').fullName;
  }
  get firstName() {
    return JSON.parse(sessionStorage.getItem("user") as '{}').firstName;
  }


  get userType() {
    return JSON.parse(sessionStorage.getItem("user") as '{}').userType;
  }
  get userTypeId() {
    return JSON.parse(sessionStorage.getItem("user") as '{}').userTypeId;
  }
  get userId() {
    return JSON.parse(sessionStorage.getItem("user") as '{}').userId;
  }

  get warehouseType() {
    return JSON.parse(sessionStorage.getItem("user") as '{}').warehouseType;
  }

  get warehouseTypeName() {
    return JSON.parse(sessionStorage.getItem("user") as '{}').warehouseTypeName;
  }

  get warehouseList() {
    const warehouseList = JSON.parse(sessionStorage.getItem("user") as '{}')
      .warehouseList;
    if (warehouseList) {
      return warehouseList;
    } else {
      return [];
    }
  }

  get company() {
    return JSON.parse(sessionStorage.getItem("user") as '{}').company;
  }

  get companyCurrency() {
    return JSON.parse(sessionStorage.getItem("user") as '{}').companyCurrency;
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
    return JSON.parse(sessionStorage.getItem("user") as '{}').accessJson;
  }

  logout() {
    this.userData = false;
    const data = {
      userName: [this.username],
      requestType: "Logout",
    };
    // this.spin.show();
    // this.spin.hide();


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

  disconnect(){
    this.webSocketAPI._disconnect();
  }

  // sendMessage(){
  //   this.webSocketAPI._send(this.name);
  // }

  // handleMessage(message) {
  //   this.greeting = message;
  // }
}
