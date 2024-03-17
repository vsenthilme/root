import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from 'src/app/core/core';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  lgForm: any;
  error: any;
  hide = 'password';
  title = environment.title;
  constructor(
    private fb: FormBuilder,
    private router: Router,
    public dialog: MatDialog,
    private auth: AuthService,
    public toastr: ToastrService,
    private messageService: MessageService
  ) { }

  
  ngOnInit() {
    localStorage.clear();
    sessionStorage.clear();

    this.inForm();
  }

  inForm() {
    this.lgForm = this.fb.group({
      userName: ["", Validators.required],
      password: ["", Validators.required]
    });

  }
  // login() {
  //   localStorage.clear();
  //   sessionStorage.clear();
  //   // stop here if form is invalid

  //   this.lgForm.controls.userName.patchValue(this.lgForm.controls.userName.value.toUpperCase() )
  //   if (this.lgForm.invalid) {
  //     this.error = "Please fill required fields to continue";

  //     this.messageService.add({key: 'br', severity:'error', summary: 'Error', detail: 'Please fill required fields to continue'});

  //     // this.toastr.error(
  //     //   "Invalid User name or Password!",
  //     //   "Login Failed",{
  //     //     timeOut: 2000,
  //     //     progressBar: false,
  //     //   }
  //     // );
  //     return;
  //   }

  //   if (this.lgForm.controls.userName.value != 'IWADMIN' || this.lgForm.controls.password.value != 'IWADMIN@2023') {
  //     this.error = "Please fill required fields to continue";

  //     this.messageService.add({key: 'br', severity:'error', summary: 'Error', detail: 'Invalid username or password'});
  //     return;
  //   }
  //   this.router.navigate(["main/dashboard"]);
  // }

  login() {
    sessionStorage.clear();
    localStorage.clear();
    // stop here if form is invalid
    if (this.lgForm.invalid) {
       this.messageService.add({key: 'br', severity:'error', summary: 'Error', detail: 'Please fill required fields to continue'});
      return;
    }
    this.auth.login(this.lgForm.value);

  }

}
