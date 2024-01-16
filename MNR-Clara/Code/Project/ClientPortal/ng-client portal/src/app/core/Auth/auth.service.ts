import { Subscription } from "rxjs";
import { Injectable, OnDestroy } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { catchError, switchMap } from "rxjs/operators";
import { CommonService } from "src/app/common-service/common-service.service";

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
    private cs: CommonService,

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
  isuserdata() {
    if (!this.isLoggedIn)
      this.logout();
  }

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
            let menu = [1000, 1001, 1004, 2101, 2102, 2202, 2203];
            sessionStorage.setItem('menu', menu.toString());
            sessionStorage.setItem("user", JSON.stringify(res))
            // let resp: any = res.result.response[0];
            // // For token
            // sessionStorage.setItem("token", res.result.bearerToken);
            // sessionStorage.setItem("token", res.result.bearerToken);
            this.router.navigate(["main/dashboard"]);

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

  sendOtp(phoneNumber: any) {
    return this.http.get<any>(`/mnr-setup-service/login/clientUser/sendOTP?contactNumber=${phoneNumber}`)
  }
  
  sendEmailOtp(emailId: any) {
    return this.http.get<any>(`/mnr-setup-service/login/clientUser/emailOTP?emailId=${emailId}`)
  }

  verifyOtp(contactNumber: string, otp: any) {
    sessionStorage.clear();
    localStorage.clear();
    this.spin.show();
    return new Promise((resolve, reject) => {
      this.sub.add(
        this.http.get<any>(`/mnr-setup-service/login/clientUser/verifyOTP?contactNumber=${contactNumber}&otp=${otp}`).subscribe(
          (res) => {
            // if (res == true) {
            let menu = [1000, 1001, 1004, 2101, 2102, 2202, 2203];
            sessionStorage.setItem('menu', menu.toString());
            sessionStorage.setItem("user", JSON.stringify(res))
            let obj = { contactNumber: contactNumber }
            this.http.post<any>(`/mnr-management-service/clientgeneral/findClientGeneral`, obj).subscribe((clientdata) => {
              clientdata.forEach((element: any) => {
                if (element.contactNumber == contactNumber) {
                  sessionStorage.setItem("clientData", JSON.stringify(element))
                }
              });
              this.spin.hide();
              this.router.navigate(["main/dashboard"]);
            });
            // } else {
            //   this.spin.show();
            //   this.toastr.error("Enter Valid OTP", "Login");
            // }
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

  verifyEmailOtp(emailId: string, otp: any) {
    sessionStorage.clear();
    localStorage.clear();
    this.spin.show();
    return new Promise((resolve, reject) => {
      this.sub.add(
        this.http.get<any>(`/mnr-setup-service/login/clientUser/verifyEmailOTP?emailId=${emailId}&otp=${otp}`).subscribe(
          (res) => {
       //     if (res == true) {
            let menu = [1000, 1001, 1004, 2101, 2102, 2202, 2203];
            sessionStorage.setItem('menu', menu.toString());
            sessionStorage.setItem("user", JSON.stringify(res))
            let obj = { emailId: emailId }
            
            console.log(3)
            this.http.post<any>(`/mnr-management-service/clientgeneral/findClientGeneral`, obj).subscribe((clientdata) => {
                if(clientdata.length >= 0){
                  clientdata.forEach((element: any) => {
                    if (element.emailId == emailId) {
                      sessionStorage.setItem("clientData", JSON.stringify(element));
                      this.router.navigate(["main/dashboard"]);
                      
                console.log(2)
                    }
          });
         }
              this.spin.hide();
            });
            
      //    }
            // } else {
            //   this.spin.show();
            //   this.toastr.error("Enter Valid OTP", "Login");
            // }
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
  }
  get languageId() {
    if (sessionStorage.getItem("user"))
      return JSON.parse(sessionStorage.getItem("user") as '{}').languageId;
  }

  // get classId() {
  //   if (sessionStorage.getItem("user"))
  //     return JSON.parse(sessionStorage.getItem("user") as '{}').classId;
  // }

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


  get userType() {
    return JSON.parse(sessionStorage.getItem("user") as '{}').userType;
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

  get clientId() {
    if (sessionStorage.getItem("clientData"))
      return JSON.parse(sessionStorage.getItem("clientData") as '{}').clientId;
  }
  get clientUserId() {
    if (sessionStorage.getItem("user"))
      return JSON.parse(sessionStorage.getItem("user") as '{}').clientUserId;
  }
  get classId() {
    if (sessionStorage.getItem("clientData"))
      return JSON.parse(sessionStorage.getItem("clientData") as '{}').classId;
  }
  get clientfullName() {
    if (sessionStorage.getItem("clientData"))
      return JSON.parse(sessionStorage.getItem("clientData") as '{}').firstNameLastName;
  }
  get clientmiddleName() {
    if (sessionStorage.getItem("clientData"))
      return JSON.parse(sessionStorage.getItem("clientData") as '{}').referenceField1;
  }
  get clientfirstName() {
    if (sessionStorage.getItem("clientData"))
      return JSON.parse(sessionStorage.getItem("clientData") as '{}').firstName;
  }
  get clientlastName() {
    if (sessionStorage.getItem("clientData"))
      return JSON.parse(sessionStorage.getItem("clientData") as '{}').lastName;
  }
  get clientcontactNo() {
    if (sessionStorage.getItem("clientData"))
      return JSON.parse(sessionStorage.getItem("clientData") as '{}').contactNumber;
  }
  get clientaddress1() {
    if (sessionStorage.getItem("clientData"))
      return JSON.parse(sessionStorage.getItem("clientData") as '{}').addressLine1;
  }
  get clientaddress2() {
    if (sessionStorage.getItem("clientData"))
      return JSON.parse(sessionStorage.getItem("clientData") as '{}').addressLine2;
  }
  get suiteno() {
    if (sessionStorage.getItem("clientData"))
      return JSON.parse(sessionStorage.getItem("clientData") as '{}').suiteDoorNo;
  }

  get clientcity() {
    if (sessionStorage.getItem("clientData"))
      return JSON.parse(sessionStorage.getItem("clientData") as '{}').city;
  }
  get clientzipcode() {
    if (sessionStorage.getItem("clientData"))
      return JSON.parse(sessionStorage.getItem("clientData") as '{}').zipCode;
  }

  get UserId() {
    if (sessionStorage.getItem("clientData"))
      return JSON.parse(sessionStorage.getItem("clientData") as '{}').createdBy;
  }



  logout() {
    sessionStorage.clear();

    localStorage.clear();
    this.userData = false;
    const data = {
      userName: [this.username],
      requestType: "Logout",
    };
    // this.spin.show();
    // this.spin.hide();

    this.router.navigate(["/"]);

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
