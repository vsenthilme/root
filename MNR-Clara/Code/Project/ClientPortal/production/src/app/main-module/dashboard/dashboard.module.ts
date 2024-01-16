import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { SharedModule } from "src/app/shared/shared.module";
import { InvoiceModule } from "../invoice/invoice.module";
import { ProfileModule } from "../profile/profile.module";
import { DashboardRoutingModule } from "./dashboard-routing.module";
import { DashboardComponent } from "./dashboard/dashboard.component";




@NgModule({
  declarations: [
    DashboardComponent,


  ],
  imports: [

    CommonModule,
    SharedModule,
    DashboardRoutingModule,
    CommonFieldModule,



    ProfileModule,
    InvoiceModule,
    // AdminModule,
    // ConfigurationModule,
    // BusinessModule,

    // ClientModule,
    //   CaseInfoModule,
    // // CaseManagementModule,

  ],
  exports: [
  ]
})
export class DashboardModule { }
