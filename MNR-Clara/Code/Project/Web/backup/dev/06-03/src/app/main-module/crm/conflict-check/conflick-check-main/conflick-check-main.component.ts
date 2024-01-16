import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";

@Component({
  selector: 'app-conflick-check-main',
  templateUrl: './conflick-check-main.component.html',
  styleUrls: ['./conflick-check-main.component.scss']
})
export class ConflickCheckMainComponent implements OnInit {
  screenid = 1080;
  public icon = 'expand_more';
  animal: string | undefined;
  name: string | undefined;
  data = false;
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  toggleFloat() {

    this.isShowDiv = !this.isShowDiv;
    this.toggle = !this.toggle;

    if (this.icon === 'expand_more') {
      this.icon = 'chevron_left';
    } else {
      this.icon = 'expand_more'
    }
    this.showFloatingButtons = !this.showFloatingButtons;

  }
  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);

  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  form = this.fb.group({
    searchFieldValue: [, Validators.required],
    searchTableNames: [, Validators.required],
  });
  constructor(
    private toastr: ToastrService,
    private router: Router,
    private cs: CommonService,
    private fb: FormBuilder, private auth: AuthService,
  ) { }
  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);

  }


  routetolist() {

    this.submitted = true;
    if (this.form.invalid) {
      this.toastr.error(
        "Please fill the required fields to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      this.cs.notifyOther(true);
      return;
    }

    this.cs.notifyOther(false);

    let parms = this.cs.encrypt(this.form.getRawValue());
    this.router.navigate(['/main/crm/conflictlist/' + parms]);
  }

}
