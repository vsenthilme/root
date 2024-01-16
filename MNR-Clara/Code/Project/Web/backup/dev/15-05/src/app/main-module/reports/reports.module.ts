import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportsRoutingModule } from './reports-routing.module';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { LeadInputComponent } from './lead-conversion/lead-input/lead-input.component';
import { LeadOutputComponent } from './lead-conversion/lead-output/lead-output.component';
import { ProspectiveInputComponent } from './proscpective-client/prospective-input/prospective-input.component';
import { ProspectiveOutputComponent } from './proscpective-client/prospective-output/prospective-output.component';
import { ReferralInputComponent } from './referral-report/referral-input/referral-input.component';
import { ReferralOutputComponent } from './referral-report/referral-output/referral-output.component';
import { CrmlistComponent } from './crm/crmlist/crmlist.component';
import { ProspectiveClientComponent } from './crm/prospective-client/prospective-client.component';
import { LeadconversionComponent } from './crm/leadconversion/leadconversion.component';
import { ReferralComponent } from './crm/referral/referral.component';
import { ClientlistComponent } from './clients/clientlist/clientlist.component';
import { LeReportComponent } from './clients/le-report/le-report.component';
import { ImmigrationComponent } from './clients/immigration/immigration.component';
import { ImmigrationMatterComponent } from './matter/immigration-matter/immigration-matter.component';
import { LandeMatterComponent } from './matter/lande-matter/lande-matter.component';
import { MatterlistComponent } from './matter/matterlist/matterlist.component';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { AccountingListComponent } from './accounting/accounting-list/accounting-list.component';
import { ArAgingComponent } from './accounting/ar-aging/ar-aging.component';
import { BillingComponent } from './accounting/billing/billing.component';
import { WipAgedPBComponent } from './matter/wip-aged/wip-aged.component';
import { QbsyncListComponent } from './Qb-sync/qbsync-list/qbsync-list.component';
import { UpdateQbComponent } from './Qb-sync/qbsync-list/update-qb/update-qb.component';
import { ExpirationReportComponent } from './matter/expiration-report/expiration-report.component';
import { DocketwiseSyncComponent } from './docketwise-sync/docketwise-sync.component';
import { ClientMatterComponent } from './matter/client-matter/client-matter.component';
import { ClientCashReciptComponent } from './accounting/client-cash-recipt/client-cash-recipt.component';
import { ClientIncomeSummaryComponent } from './accounting/client-income-summary/client-income-summary.component';
import { ArReportComponent } from './accounting/ar-report/ar-report.component';
import { BilledUnbilledComponent } from './accounting/billed-unbilled/billed-unbilled.component';
import { MatterRatesComponent } from './matter/matter-rates/matter-rates.component';
import { BilledHrspaidComponent } from './accounting/billed-hrspaid/billed-hrspaid.component';
import { WriteOffComponent } from './accounting/write-off/write-off.component';
import { BilledPaidFeesComponent } from './accounting/billed-paid-fees/billed-paid-fees.component';
import { TaskReportComponent } from './matter/task-report/task-report.component';
import { AttorneyProductivityComponent } from './productivity/attorney-productivity/attorney-productivity.component';
import { FeesSharingComponent } from './productivity/attorney-productivity/fees-sharing/fees-sharing.component';
import { MatterPLComponent } from './productivity/matter-pl/matter-pl.component';
import { ImmigrationPaymentComponent } from './accounting/immigration-payment/immigration-payment.component';
import { InvoiceComponent } from './accounting/immigration-payment/invoice/invoice.component';


@NgModule({
  declarations: [
    LeadInputComponent,
    LeadOutputComponent,
    ProspectiveInputComponent,
    ProspectiveOutputComponent, ReferralInputComponent,
    ReferralOutputComponent,
    CrmlistComponent,
    ProspectiveClientComponent,
    LeadconversionComponent,
    ReferralComponent,
    ClientlistComponent,
    LeReportComponent,
    ImmigrationComponent,
    ImmigrationMatterComponent,
    LandeMatterComponent,
    MatterlistComponent,
    AccountingListComponent,
    ArAgingComponent,
    BillingComponent,
    WipAgedPBComponent,
    QbsyncListComponent,
    UpdateQbComponent,
    ExpirationReportComponent,
    DocketwiseSyncComponent,
    ClientMatterComponent,
    ClientCashReciptComponent,
    ClientIncomeSummaryComponent,
    ArReportComponent,
    BilledUnbilledComponent,
    MatterRatesComponent,
    BilledHrspaidComponent,
    WriteOffComponent,
    BilledPaidFeesComponent,
    TaskReportComponent,
    AttorneyProductivityComponent,
    FeesSharingComponent,
    MatterPLComponent,
    ImmigrationPaymentComponent,
    InvoiceComponent,
  ],
  imports: [
    CommonModule,
    ReportsRoutingModule,
    SharedModule, CommonFieldModule,
    AngularMultiSelectModule
  ]
})
export class ReportsModule { }
