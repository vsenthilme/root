import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from 'src/app/core/core';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { Config } from 'ng-otp-input/lib/models/config';

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
  hide = 'password';
  title = environment.title;
  constructor(
    private fb: FormBuilder,
    private router: Router,
    public dialog: MatDialog,
    private auth: AuthService,
    public toastr: ToastrService
  ) { }
  ngOnInit() {


    this.inForm();

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

    this.auth.login(this.lgForm.value);

  }
}
