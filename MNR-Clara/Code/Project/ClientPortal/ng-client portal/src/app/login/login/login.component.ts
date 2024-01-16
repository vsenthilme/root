import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl, ValidationErrors, AbstractControl, ValidatorFn } from '@angular/forms';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from 'src/app/core/core';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { Config } from 'ng-otp-input/lib/models/config';
import { Subscription } from 'rxjs';
import {
  SearchCountryField,
  CountryISO,
} from 'ngx-intl-tel-input';
import { NgxSpinnerComponent, NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  mobileOTP: any;
  emailOTP: any;
  otp: string;
  onOtpChange(otp) {
    this.otp = otp;
  }

  otp1: string;
  onOtpChange1(otp1) {
    this.otp1 = otp1;
  }
  public myModel = ''
    public mask = [/\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]

  SearchCountryField = SearchCountryField;
  CountryISO = CountryISO;
  preferredCountries: CountryISO[] = [
    CountryISO.UnitedStates,
    CountryISO.UnitedKingdom,
  ];
  
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
  hide = 'password';
  title = environment.title;
  sub = new Subscription;
  loginButtonDisabled = true;
  loginButtonEmailDisabled = true;
  constructor(
    private fb: FormBuilder,
    private router: Router,
    public dialog: MatDialog,
    private auth: AuthService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService
  ) { }
  ngOnInit() {
    sessionStorage.clear();
    localStorage.clear();

    this.inForm();
  }

  inForm() {
    this.lgForm = this.fb.group({
      //userName: ["", [Validators.required, this.regexValidator(new RegExp(/^(\+\d{1,3}[- ]?)?\d{10}$/), { 'numberPattern': true, 'message': 'Phone Number is not valid' })]],
      password: ["", ],
      userName: [, [Validators.pattern('[0-9 -]+$')]],
      email: [, [Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")]],
      emailPassword: ["", ],
    });

  }
  login() {
    sessionStorage.clear();
    localStorage.clear();
    this.lgForm.controls.password.patchValue(this.otp);
    // stop here if form is invalid
    if (this.lgForm.invalid) {
      this.error = "Please fill required fields to continue";

      this.toastr.warning(
        "Please fill required fields to continue",
        "Username/Password"
      );
      return;
    }
   // let phoneNumber = this.phoneNumberConvert();
    this.auth.verifyOtp(this.lgForm.controls.userName.value, this.lgForm.controls.password.value);

  }
password = false;
  sendOtp() {
   // let phoneNumber = this.phoneNumberConvert();
    this.sub.add(
      this.auth.sendOtp(this.lgForm.controls.userName.value).subscribe((result: any) => {
        if(result == true){
          this.loginButtonDisabled = false;
          this.toastr.success("Authentication Code successfully sent to the Registered Phone Number", "Login", {
            timeOut: 2000,
            progressBar: false,
          });
          this.password = true;
        }
      }, (err: any) => {
        this.toastr.error(err, "");
      }));
  }
  phoneNumberConvert() {
    let phoneNumber = this.lgForm.controls.userName.value;
    let numberStringArray: any[] = phoneNumber.split("");
    return phoneNumber = numberStringArray[0] + numberStringArray[1] + numberStringArray[2] + "-" + numberStringArray[3] + numberStringArray[4] + numberStringArray[5] + "-" + numberStringArray[6] + numberStringArray[7] + numberStringArray[8] + numberStringArray[9];
  }
  regexValidator(regex: RegExp, error: ValidationErrors): ValidatorFn {
    return (control: AbstractControl): any => {
      if (!control.value) {
        return null;
      }
      const valid = regex.test(control.value);
      return valid ? null : error;
    };
  }

  hasError() {
    let control = this.lgForm.controls.userName;
    if (control === undefined) return false;
    if (!control.errors) return false;
    else return true;
  }

  errorMessage() {
    if (!this.lgForm.controls.userName.errors) return "";
    return this.lgForm.controls.userName.errors.message;
  }


  sendEmailOtp() {
    this.spin.show();
    // let phoneNumber = this.phoneNumberConvert();
     this.sub.add(
       this.auth.sendEmailOtp(this.lgForm.controls.email.value).subscribe((result: any) => {
         if(result == true){
           this.loginButtonEmailDisabled = false;
           this.toastr.success("Authentication Code successfully sent to the Registered Email ID", "Login", {
            timeOut: 2000,
            progressBar: false,
          });
           this.password = true;
           this.spin.hide();
         }
       }, (err: any) => {
         this.toastr.error(err, "");
       }));
   }
   emailLogin() {
    sessionStorage.clear();
    localStorage.clear();
    this.lgForm.controls.emailPassword.patchValue(this.otp1);
    if (this.lgForm.invalid) {
      this.error = "Please fill required fields to continue";

      this.toastr.warning(
        "Please fill required fields to continue",
        "Username/Password"
      );
      return;
    }
   // let phoneNumber = this.phoneNumberConvert();
    this.auth.verifyEmailOtp(this.lgForm.controls.email.value, this.lgForm.controls.emailPassword.value);

  }
}