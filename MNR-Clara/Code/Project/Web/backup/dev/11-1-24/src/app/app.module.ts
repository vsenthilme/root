import { DatePipe, DecimalPipe, HashLocationStrategy, LocationStrategy, PathLocationStrategy } from "@angular/common";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { BrowserModule, Title } from "@angular/platform-browser";
import { RouterModule } from "@angular/router";
import { NgOtpInputModule } from 'ng-otp-input';
import { NgxSpinnerModule } from "ngx-spinner";
import { ToastrModule } from "ngx-toastr";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { AuthGuard } from "./core/Auth/auth.guard";
import { CommonInterceptor } from "./core/common.interceptors";
import { AuthService } from "./core/core";
import { ErrorInterceptor } from "./core/error.interceptor";
import { SharedModule } from "./shared/shared.module";
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from "@angular/material/core";
import { NgxEditorModule } from "ngx-editor";
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from "@angular/material/form-field";
import { NgNumbersOnlyInputDirectiveModule } from "ng-numbers-only-input-directive";
import { HighchartsChartModule } from 'highcharts-angular';
import { MatInputModule } from "@angular/material/input";
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { AppDateAdapter, APP_DATE_FORMATS } from "./shared/material/date-format";
import { UserIdleModule } from 'angular-user-idle';
import { WebSocketAPIService } from "./WebSocketAPIService";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
// import {
//   MAT_DATE_FORMATS,
//   MAT_DATE_LOCALE,
// } from '@angular/material';
// import {
//   MomentDateModule,
//   MomentDateAdapter,
// } from 'npm i @angular/material-moment-adapter';

// create our cost var with the information about the format that we want
export const MY_FORMATS = {
  parse: {
    dateInput: 'MM-dd-yyyy',
  },
  display: {
    dateInput: 'MM-dd-yyyy',
    monthYearLabel: 'MM yyyy',
    dateA11yLabel: 'MM-dd-yyyy',
    monthYearA11yLabel: 'MM yyyy',
  },

};
@NgModule({
  declarations: [
    AppComponent,
    //tep

  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    SharedModule,
    AppRoutingModule,
    RouterModule,
    NgOtpInputModule,
    NgNumbersOnlyInputDirectiveModule,
    HighchartsChartModule,
    MatInputModule,
    HttpClientModule,
    MatInputModule,
    AngularMultiSelectModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    SharedModule,
    // CommonFieldModule,
    // ToastrModule added
    NgxSpinnerModule,
    NgxEditorModule,
    BrowserModule,
    ReactiveFormsModule,
    // MatDialogModule,
    // AppRoutingModule,
    // BrowserAnimationsModule,
    UserIdleModule.forRoot({idle: 300, timeout: 10, ping: 120}),
    ToastrModule.forRoot({
      timeOut: 5000,
      positionClass: 'toast-bottom-right',
      preventDuplicates: true,
      progressBar: true
    }),
    NgbModule,
  ],

  providers: [AuthGuard,
    AuthService,
    DatePipe,
    DecimalPipe,
    WebSocketAPIService,
    {
      provide: MAT_DATE_FORMATS,
      useValue: MY_FORMATS,
    },
    {
      provide: MAT_DATE_LOCALE,
      useValue: MY_FORMATS,
    },
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    { provide: HTTP_INTERCEPTORS, useClass: CommonInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'fill' } },
    Title,
    { provide: DateAdapter, useClass: AppDateAdapter },
		{ provide: MAT_DATE_FORMATS, useValue: APP_DATE_FORMATS },
		// { provide: MAT_DATE_LOCALE, useValue: 'en-GB' },
		{ provide: LocationStrategy, useClass: HashLocationStrategy }
    
  ],

  bootstrap: [AppComponent]

})
export class AppModule { }
