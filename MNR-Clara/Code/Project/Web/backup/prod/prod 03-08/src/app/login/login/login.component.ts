import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from 'src/app/core/core';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { Config } from 'ng-otp-input/lib/models/config';
import { NgxSpinnerService } from 'ngx-spinner';
import { Subscription } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  screenid = 1001;
  config: Config = {
    allowNumbersOnly: true,
    length: 6,
    isPasswordInput: false,
    disableAutoFocus: true,
    placeholder: '',
    inputStyles: {
      'width': '40px',
      'height': '40px',
    }
  };
  lgForm: any;
  error: any;
  //hide = 'password';
  hide = true;
  title = environment.title;

  showEmailVerify: boolean;
  showLoginDeatils: boolean;
  userEmail: any;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    public dialog: MatDialog,
    private auth: AuthService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private http: HttpClient,
    private cs: CommonService
  ) { }
  ngOnInit() {


    this.inForm();

    this.showLoginDeatils = true;
    this.showEmailVerify = false  ;
  }

  inForm() {
    this.lgForm = this.fb.group({
      userName: ["", Validators.required],
      password: ["", Validators.required]
    });

  }
  login() {

    // stop here if form is invalid
    if (this.lgForm.invalid) {
      this.error = "Please fill the required fields to continue";

      this.toastr.error(
        "Invalid Username or Password!",
        "Login Failed", {
        timeOut: 2000,
        progressBar: false,
      }
      );
      return;
    }

    // if(this.lgForm.controls.userName.value == 'MW' || this.lgForm.controls.userName.value == 'JI'  || this.lgForm.controls.userName.value == 'ji'  || this.lgForm.controls.userName.value == 'mw'  || this.lgForm.controls.userName.value == 'Mw'  || this.lgForm.controls.userName.value == 'mW'  || this.lgForm.controls.userName.value == 'Ji'  || this.lgForm.controls.userName.value == 'jI'){
    //   this.login1(this.lgForm.value);
    // }else{
    //   this.auth.login(this.lgForm.value);
    // }

    //  if(this.lgForm.controls.userName.value == 'RS' || this.lgForm.controls.userName.value == 'rs'  || this.lgForm.controls.userName.value == 'Rs'  || this.lgForm.controls.userName.value == 'rS'){
    //   this.auth.login(this.lgForm.value);
    // }else{
    //   this.login1(this.lgForm.value);
    // }
    this.login1(this.lgForm.value);
  //  this.login1(this.lgForm.value);
     //this.auth.login(this.lgForm.value);

  }
  



  sub = new Subscription();
  login1(user: { userName: string; password: any; }) {
    sessionStorage.clear();
    localStorage.clear();
    this.spin.show();
    // this.router.navigate(["main/dashboard"]);
    return new Promise((resolve, reject) => {
      this.sub.add(
        this.http.get<any>(`/mnr-setup-service/login?userId=${user.userName}&password=${user.password}`).subscribe(
          (res) => {

            this.spin.hide();
            this.userEmail = res.emailId;
            // let resp: any = res.result.response[0];
            // // For token
            // sessionStorage.setItem("token", res.result.bearerToken);
            // sessionStorage.setItem("token", res.result.bearerToken);
            sessionStorage.setItem("user", JSON.stringify(res));

            this.showLoginDeatils = false;
            this.showEmailVerify = true;
      


       
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

  back(){
    this.showLoginDeatils = true;
    console.log(this.showLoginDeatils)
    this.showEmailVerify = false;
  }

  otp: any;
  onOtpChange(otp) {
    this.otp = otp;
  }
  
verifyOtp(){
  this.auth.verifyOtp(this.lgForm.controls.userName.value, this.otp);
}

resendOTP(){
  this.spin.show();
  return new Promise((resolve, reject) => {
    this.sub.add(
      this.http.get<any>(`/mnr-setup-service/login/emailOTP?userId=${this.lgForm.controls.userName.value}`).subscribe(
        (res) => {
          this.toastr.success("Verification code successfully resent to the email.", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
          this.spin.hide();
        },
        (rej) => {
          this.spin.hide();
          this.cs.commonerror(rej);
          resolve(false);
        }
      )
    );
  });
}
 
}
