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
import { NgxIntlTelInputModule } from "ngx-intl-tel-input";
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from "@angular/material/form-field";
import { NgNumbersOnlyInputDirectiveModule } from "ng-numbers-only-input-directive";

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

    HttpClientModule,
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
    NgxIntlTelInputModule,
    ToastrModule.forRoot({
      timeOut: 5000,
      positionClass: 'toast-bottom-right',
      preventDuplicates: true,
      progressBar: true
    }),
  ],

  providers: [AuthGuard,
    AuthService,    DecimalPipe,
    DatePipe,
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
  ],

  bootstrap: [AppComponent]

})
export class AppModule { }
