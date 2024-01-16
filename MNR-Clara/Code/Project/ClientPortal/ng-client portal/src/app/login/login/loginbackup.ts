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

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  otp: string;
  onOtpChange(otp) {
    this.otp = otp;
  }

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
  constructor(
    private fb: FormBuilder,
    private router: Router,
    public dialog: MatDialog,
    private auth: AuthService,
    public toastr: ToastrService
  ) { }
  ngOnInit() {
    sessionStorage.clear();
    localStorage.clear();

    this.inForm();
  }

  inForm() {
    this.lgForm = this.fb.group({
      userName: ["", [Validators.required, Validators.pattern('^(\([0-9]{3}\) |[0-9]{3}-)[0-9]{3}-[0-9]{4}$'), this.regexValidator(new RegExp(/^(\+\d{1,3}[- ]?)?\d{10}$/), { 'numberPattern': true, 'message': 'Phone Number is not valid' })]],
      password: ["", Validators.required]
    });

  }
  login() {

    sessionStorage.clear();
    localStorage.clear();
    this.lgForm.controls.password.patchValue(this.otp);
    console.log(this.lgForm.controls.password.value)
    // stop here if form is invalid
    if (this.lgForm.invalid) {
      this.error = "Please fill required fields to continue";

      this.toastr.warning(
        "Please fill required fields to continue",
        "Username/Password"
      );
      return;
    }

   // this.lgForm.controls.password.patchValue(this.otp)
    
    let phoneNumber = this.phoneNumberConvert();
    this.auth.verifyOtp(phoneNumber, this.lgForm.controls.password.value);

  }
password = false;
  sendOtp() {
    console.log(this.lgForm.controls.userName.value.number)
    let phoneNumber = this.phoneNumberConvert();
    this.sub.add(
      this.auth.sendOtp(phoneNumber).subscribe((result: any) => {
        if(result == true){
          this.loginButtonDisabled = false;
          this.toastr.success("OTP successfully sent to the Registered Phone Number", "Login");
          this.password = true;
        }
      }, (err: any) => {
        this.toastr.error(err, "");
      }));
  }
  phoneNumberConvert() {
    let phoneNumber = this.lgForm.controls.userName.value.number;
    let numberStringArray: any[] = phoneNumber.split("");
    return phoneNumber = numberStringArray[0] + numberStringArray[1] + numberStringArray[2] + "-" + numberStringArray[4] + numberStringArray[5] + numberStringArray[6] + "-" + numberStringArray[8] + numberStringArray[9] + numberStringArray[10] + numberStringArray[11];
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
}
