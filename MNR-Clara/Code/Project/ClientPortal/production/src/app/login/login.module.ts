import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgOtpInputModule } from  'ng-otp-input';
import { LoginRoutingModule } from './login-routing.module';
import { LoginComponent } from './login/login.component';
import { SharedModule } from '../shared/shared.module';
import { OtpComponent } from './otp/otp.component';
import { TextMaskModule } from 'angular2-text-mask';


@NgModule({
  declarations: [
    LoginComponent,
    OtpComponent
  ],
  imports: [
    CommonModule,
    LoginRoutingModule,
    SharedModule,
    NgOtpInputModule,
    TextMaskModule
  ]
})
export class LoginModule { }
