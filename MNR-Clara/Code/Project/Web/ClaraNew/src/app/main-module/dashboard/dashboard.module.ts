import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { HighchartsChartModule } from "highcharts-angular";
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { SharedModule } from "src/app/shared/shared.module";
import { CardComponent } from "./card/card.component";
import { DashboardRoutingModule } from "./dashboard-routing.module";
import { BarChartComponent } from "./dashboard/bar-chart/bar-chart.component";
import { DashboardComponent } from "./dashboard/dashboard.component";
import { LineChartComponent } from "./dashboard/line-chart/line-chart.component";
import { PiechartComponent } from "./dashboard/piechart/piechart.component";
import { BasicLineComponent } from "./dashboard/basic-line/basic-line.component";
import { StackChartComponent } from "./dashboard/stack-chart/stack-chart.component";
import { TopClientsComponent } from "./dashboard/top-clients/top-clients.component";
import { LeadConversionComponent } from "./dashboard/lead-conversion/lead-conversion.component";
import { TreeComponent } from './tree/tree.component';




@NgModule({
  declarations: [
    DashboardComponent,
    CardComponent,
    PiechartComponent,
    LineChartComponent,
    StackChartComponent,
    BasicLineComponent,
    BarChartComponent,
    TopClientsComponent,
    LeadConversionComponent,
    TreeComponent

  ],
  imports: [

    CommonModule,
    SharedModule,
    DashboardRoutingModule,
    CommonFieldModule,
    HighchartsChartModule,
    // CaseInfoModule,
    // CaseManagementModule,
    // CustomerformModule,




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
