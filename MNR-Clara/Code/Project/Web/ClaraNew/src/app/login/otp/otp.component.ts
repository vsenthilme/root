import { Component, OnInit } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { Router } from "@angular/router";
import { Config } from "ng-otp-input/lib/models/config";
import { ToastrService } from "ngx-toastr";
import { AuthService } from "src/app/core/core";
import { environment } from "src/environments/environment";



@Component({
  selector: 'app-otp',
  templateUrl: './otp.component.html',
  styleUrls: ['./otp.component.scss']
})
export class OtpComponent implements OnInit {

  config :Config = {
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
  constructor(
  ) { }
  ngOnInit() {
  }

}
