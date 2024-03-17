import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from 'src/app/core/core';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { CronJob } from 'cron';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  lgForm: any;
  error: any;
  hide = true;
  title = environment.title;
  cronJob: CronJob;
  constructor(
    private fb: FormBuilder,
    private router: Router,
    public dialog: MatDialog,
    private auth: AuthService,
    public toastr: ToastrService
  ) {  this.currentEnv = environment.name;
  
    this.cronJob = new CronJob('0 * * * *', async () => {
      try {
        await this.reload();
      } catch (e) {
        console.error(e);
      }
    });
    
  }

  async reload(): Promise<void> {
  //  window.location.reload();
  }

  currentEnv: string;
  prod: boolean;
  
  ngOnInit() {

    if(this.currentEnv == 'prod'){
      this.prod = true;
    }else{
      this.prod = false;
    }

    localStorage.clear();
    sessionStorage.clear();

    this.inForm();
  }

  inForm() {
    this.lgForm = this.fb.group({
      userName: ["", Validators.required],
      password: ["", Validators.required],
      version: ["Web"]
    });

  }
  login() {
    sessionStorage.clear();
    localStorage.clear();
    // stop here if form is invalid
    if (this.lgForm.invalid) {
      this.error = "Please fill required fields to continue";

      this.toastr.error(
        "Invalid User name or Password!",
        "Login Failed",{
          timeOut: 2000,
          progressBar: false,
        }
      );
      return;
    }
    this.auth.login(this.lgForm.value);

  }
}
