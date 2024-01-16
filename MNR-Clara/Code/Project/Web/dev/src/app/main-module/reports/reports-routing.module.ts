import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccountingListComponent } from './accounting/accounting-list/accounting-list.component';
import { ArAgingComponent } from './accounting/ar-aging/ar-aging.component';
import { ArReportComponent } from './accounting/ar-report/ar-report.component';
import { BilledHrspaidComponent } from './accounting/billed-hrspaid/billed-hrspaid.component';
import { BilledPaidFeesComponent } from './accounting/billed-paid-fees/billed-paid-fees.component';
import { BilledUnbilledComponent } from './accounting/billed-unbilled/billed-unbilled.component';
import { BillingComponent } from './accounting/billing/billing.component';
import { ClientCashReciptComponent } from './accounting/client-cash-recipt/client-cash-recipt.component';
import { ClientIncomeSummaryComponent } from './accounting/client-income-summary/client-income-summary.component';
import { WriteOffComponent } from './accounting/write-off/write-off.component';
import { ClientlistComponent } from './clients/clientlist/clientlist.component';
import { ImmigrationComponent } from './clients/immigration/immigration.component';
import { LeReportComponent } from './clients/le-report/le-report.component';
import { CrmlistComponent } from './crm/crmlist/crmlist.component';
import { LeadconversionComponent } from './crm/leadconversion/leadconversion.component';
import { ProspectiveClientComponent } from './crm/prospective-client/prospective-client.component';
import { ReferralComponent } from './crm/referral/referral.component';
import { DocketwiseSyncComponent } from './docketwise-sync/docketwise-sync.component';
import { ClientMatterComponent } from './matter/client-matter/client-matter.component';
import { ExpirationReportComponent } from './matter/expiration-report/expiration-report.component';
import { ImmigrationMatterComponent } from './matter/immigration-matter/immigration-matter.component';
import { LandeMatterComponent } from './matter/lande-matter/lande-matter.component';
import { MatterRatesComponent } from './matter/matter-rates/matter-rates.component';
import { MatterlistComponent } from './matter/matterlist/matterlist.component';
import { TaskReportComponent } from './matter/task-report/task-report.component';
import { WipAgedPBComponent } from './matter/wip-aged/wip-aged.component';
import { QbsyncListComponent } from './Qb-sync/qbsync-list/qbsync-list.component';
import { AttorneyProductivityComponent } from './productivity/attorney-productivity/attorney-productivity.component';
import { MatterPlComponent } from './productivity/matter-pl/matter-pl.component';
import { LandebillinghrsreportsComponent } from './accounting/landebillinghrsreports/landebillinghrsreports.component';
import { PartnerbillingComponent } from './accounting/partnerbilling/partnerbilling.component';
import { AcademyReportComponent } from './matter/academy-report/academy-report.component';


const routes: Routes = [
  // { path: 'leadinput', component: LeadInputComponent, pathMatch: 'full', data: { title: 'Lead Input', module: 'Reports' } },
  // { path: 'leadoutput', component: LeadOutputComponent, pathMatch: 'full', data: { title: 'Lead Output', module: 'Reports' } },

  // { path: 'prospectiveinput', component: ProspectiveInputComponent, pathMatch: 'full', data: { title: 'Prospective Input', module: 'Reports' } },
  // { path: 'prospectiveoutput', component: ProspectiveOutputComponent, pathMatch: 'full', data: { title: 'Prospective Output', module: 'Reports' } },

  // { path: 'referralinput', component: ReferralInputComponent, pathMatch: 'full', data: { title: 'Referral Input', module: 'Reports' } },
  // { path: 'referraloutput', component: ReferralOutputComponent, pathMatch: 'full', data: { title: 'Referral Output', module: 'Reports' } },

  { path: 'crmlist', component: CrmlistComponent, pathMatch: 'full', data: { title: 'CRM Reports', module: 'Reports' } },
  { path: 'prospectiveclient', component: ProspectiveClientComponent, pathMatch: 'full', data: { title: 'Prospective Client Report', module: 'Reports' } },
  { path: 'leadconversion', component: LeadconversionComponent, pathMatch: 'full', data: { title: 'Lead Conversion', module: 'Reports' } },
  { path: 'referral', component: ReferralComponent, pathMatch: 'full', data: { title: 'Referral', module: 'Reports' } },
  { path: 'crmlist', component: CrmlistComponent, pathMatch: 'full', data: { title: 'CRM Reports', module: 'Reports' } },

  { path: 'clientlist', component: ClientlistComponent, pathMatch: 'full', data: { title: 'Prospective Client Report', module: 'Reports' } },
  { path: 'immigration', component: ImmigrationComponent, pathMatch: 'full', data: { title: 'Lead Conversion', module: 'Reports' } },
  { path: 'lande', component: LeReportComponent, pathMatch: 'full', data: { title: 'Referral', module: 'Reports' } },

  { path: 'matterlist', component: MatterlistComponent, pathMatch: 'full', data: { title: 'Prospective Client Report', module: 'Reports' } },
  { path: 'landematter', component: LandeMatterComponent, pathMatch: 'full', data: { title: 'Lead Conversion', module: 'Reports' } },
  { path: 'immigrationmatter', component: ImmigrationMatterComponent, pathMatch: 'full', data: { title: 'Referral', module: 'Reports' } },
  { path: 'wipagedpb', component: WipAgedPBComponent, pathMatch: 'full', data: { title: 'WIP Agend PB', module: 'Reports' } },
  { path: 'clientmatter', component: ClientMatterComponent, pathMatch: 'full', data: { title: 'Client and Matter Report', module: 'Reports' } },
  { path: 'matterRates', component: MatterRatesComponent, pathMatch: 'full', data: { title: 'Matter Rates Report', module: 'Reports' } },
  { path: 'taskReport', component: TaskReportComponent, pathMatch: 'full', data: { title: 'Task', module: 'Reports' } },


  { path: 'accountlist', component: AccountingListComponent, pathMatch: 'full', data: { title: 'Prospective Client Report', module: 'Reports' } },
  { path: 'araging', component: ArAgingComponent, pathMatch: 'full', data: { title: 'AR Aging Report', module: 'Reports' } },
  { path: 'billing', component: BillingComponent, pathMatch: 'full', data: { title: 'Billing Report', module: 'Reports' } },
  { path: 'accountlist', component: AccountingListComponent, pathMatch: 'full', data: { title: 'Prospective Client Report', module: 'Reports' } },
  { path: 'expirarion', component: ExpirationReportComponent, pathMatch: 'full', data: { title: 'Expirarion Report', module: 'Reports' } },
  { path: 'clientcash', component: ClientCashReciptComponent, pathMatch: 'full', data: { title: 'Client Cash Receipt Report', module: 'Reports' } },
  { path: 'clientIncomeSummary', component: ClientIncomeSummaryComponent, pathMatch: 'full', data: { title: 'Client Income Summary Report', module: 'Reports' } },
  { path: 'ArReportComponent', component: ArReportComponent, pathMatch: 'full', data: { title: 'AR Report', module: 'Reports' } },
  { path: 'BilledUnbilledComponent', component: BilledUnbilledComponent, pathMatch: 'full', data: { title: 'Billed & Unbilled', module: 'Reports' } },
  { path: 'billedHoursPaid', component: BilledHrspaidComponent, pathMatch: 'full', data: { title: 'Billed Hours Paid', module: 'Reports' } },
  { path: 'writeoff', component: WriteOffComponent, pathMatch: 'full', data: { title: 'Write Off', module: 'Reports' } },
  { path: 'billedPaid', component: BilledPaidFeesComponent, pathMatch: 'full', data: { title: 'Billed Fees Paid', module: 'Reports' } },
  
  { path: 'attorneyProductivity', component: AttorneyProductivityComponent, pathMatch: 'full', data: { title: 'Attorney Productivity Report', module: 'Reports' } },
  { path: 'matterPL', component: MatterPlComponent, pathMatch: 'full', data: { title: 'Matter P&L', module: 'Reports' } },
  
  { path: 'qbsync', component: QbsyncListComponent, pathMatch: 'full', data: { title: 'QB Sync', module: 'Reports' } },
  { path: 'docketwisesync', component: DocketwiseSyncComponent, pathMatch: 'full', data: { title: 'Docusign Sync', module: 'Reports' } },

  { path: 'landebilledhr', component: LandebillinghrsreportsComponent, pathMatch: 'full', data: { title: 'L&E Billing', module: 'Reports' } },
  { path: 'partnerbilling', component: PartnerbillingComponent, pathMatch: 'full', data: { title: 'Partner Billing Report', module: 'Reports' } },
  { path: 'academy', component: AcademyReportComponent, pathMatch: 'full', data: { title: 'Academy Report', module: 'Reports' } },
]
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportsRoutingModule { }
