import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AgreementRenewComponent } from './agreement-renew/agreement-renew.component';
import { DocumentsComponent } from './documents/documents.component';
import { EmployeeEfficiencyComponent } from './employee-efficiency/employee-efficiency.component';
import { FillrateComponent } from './fillrate/fillrate.component';
import { InquiryStatusComponent } from './inquiry-status/inquiry-status.component';
import { LeadTimeComponent } from './lead-time/lead-time.component';
import { MasterDataComponent } from './master-data/master-data.component';
import { PaymentDueComponent } from './payment-due/payment-due.component';
import { QuotationStatusComponent } from './quotation-status/quotation-status.component';
import { ReportsListComponent } from './reports-list/reports-list.component';
import { ReportstabComponent } from './reportstab/reportstab.component';
import { SmsAlertComponent } from './sms-alert/sms-alert.component';
import { StorageUnitComponent } from './storage-unit/storage-unit.component';
import { WordOrderStatusComponent } from './word-order-status/word-order-status.component';
import { WordorderprocessingleadtimeComponent } from './wordorderprocessingleadtime/wordorderprocessingleadtime.component';

const routes: Routes = [
 
  { path: 'leadTime', component: LeadTimeComponent,  },
  { path: 'agreementRenew', component: AgreementRenewComponent,  },
  { path: 'document', component: DocumentsComponent,  },
  { path: 'employeeEfficiency', component: EmployeeEfficiencyComponent,  },
  { path: 'fillRate', component: FillrateComponent,  },
  { path: 'inquiryRate', component: InquiryStatusComponent,  },
  { path: 'masterData', component: MasterDataComponent,  },
  { path: 'paymentDue', component: PaymentDueComponent,  },
  { path: 'quotationstatus', component: QuotationStatusComponent,  },
  { path: 'smsAlert', component: SmsAlertComponent,  },
  { path: 'storageUnit', component: StorageUnitComponent,  },
  { path: 'workOrder', component: WordOrderStatusComponent,  },
  { path: 'report-list', component: ReportsListComponent,  },
  { path:'wordorderprocessingleadtime',component: WordorderprocessingleadtimeComponent, },
  {
    path: '',
    component: ReportsListComponent, },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportsRoutingModule { }
