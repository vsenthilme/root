import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { NgMultiSelectDropDownModule } from "ng-multiselect-dropdown";
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { SharedModule } from "src/app/shared/shared.module";
import { ReportsListComponent } from "./reports-list/reports-list.component";
import { ReportsRoutingModule } from "./reports-routing.module";
import { PaymentDueComponent } from './payment-due/payment-due.component';
import { InquiryStatusComponent } from './inquiry-status/inquiry-status.component';
import { StorageUnitComponent } from './storage-unit/storage-unit.component';
import { MasterDataComponent } from './master-data/master-data.component';
import { DocumentsComponent } from './documents/documents.component';
import { QuotationStatusComponent } from './quotation-status/quotation-status.component';
import { WordOrderStatusComponent } from './word-order-status/word-order-status.component';
import { FillrateComponent } from './fillrate/fillrate.component';
import { SmsAlertComponent } from './sms-alert/sms-alert.component';
import { AgreementRenewComponent } from './agreement-renew/agreement-renew.component';
import { EmployeeEfficiencyComponent } from './employee-efficiency/employee-efficiency.component';
import { LeadTimeComponent } from './lead-time/lead-time.component';
import { WordorderprocessingleadtimeComponent } from './wordorderprocessingleadtime/wordorderprocessingleadtime.component';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
@NgModule({
  declarations: [
    ReportsListComponent,
    PaymentDueComponent,
    InquiryStatusComponent,
    StorageUnitComponent,
    MasterDataComponent,
    DocumentsComponent,
    QuotationStatusComponent,
    WordOrderStatusComponent,
    FillrateComponent,
    SmsAlertComponent,
    AgreementRenewComponent,
    EmployeeEfficiencyComponent,
    LeadTimeComponent,
    WordorderprocessingleadtimeComponent,
  ],
  imports: [
    CommonModule,
    ReportsRoutingModule,
    CommonModule,
    SharedModule,
    CommonFieldModule,
    NgMultiSelectDropDownModule.forRoot(),
    AngularMultiSelectModule,
    OwlDateTimeModule, 
    OwlNativeDateTimeModule,
  ],
  exports:[
  ]
})
export class ReportsModule { }
