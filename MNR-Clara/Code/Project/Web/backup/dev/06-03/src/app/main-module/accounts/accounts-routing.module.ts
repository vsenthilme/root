import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BillComponent } from './bill/bill.component';
import { InvoiceDetailsComponent } from './bill/invoice-details/invoice-details.component';
import { NewbillListComponent } from './bill/newbill-list/newbill-list.component';
import { MatterExpenseComponent } from './matter-expense/matter-expense.component';
import { PaymentPlanComponent } from './payment-plan/payment-plan.component';
import { PaymentplanNewComponent } from './payment-plan/paymentplan-new/paymentplan-new.component';
import { PrebillAaproveComponent } from './prebill/prebill-aaprove/prebill-aaprove.component';
import { PrebillAssignpartnerComponent } from './prebill/prebill-assignpartner/prebill-assignpartner.component';
import { PrebillDisplayComponent } from './prebill/prebill-display/prebill-display.component';
import { PrebillNewComponent } from './prebill/prebill-new/prebill-new.component';
import { PrebillComponent } from './prebill/prebill.component';
import { QuotationPdfComponent } from './quotation/quotation-pdf/quotation-pdf.component';
import { QuotationsListComponent } from './quotation/quotations-list/quotations-list.component';
import { QuotationsNewComponent } from './quotation/quotations-list/quotations-new/quotations-new.component';
import { TimeticketDiaryComponent } from './timeticket-diary/timeticket-diary.component';
import { TimeticketSummaryUsersComponent } from './timeticket-summary-users/timeticket-summary-users.component';
const routes: Routes = [
  { path: 'quotationslist', component: QuotationsListComponent, data: { title: 'Quotations', module: 'Billing' } },
  { path: 'quotationsnew/:code', component: QuotationsNewComponent, data: { title: 'Quotations', module: 'Billing' } },
  //added new
  { path: 'quotationpdf/:code', component: QuotationPdfComponent, data: { title: 'Billing', module: 'Billing' } },

  { path: 'paymentplanlist', component: PaymentPlanComponent, data: { title: 'PaymentPlan', module: 'Billing' } },
  { path: 'paymentplannew/:code', component: PaymentplanNewComponent, data: { title: 'PaymentPlan', module: 'Billing' } },

  { path: 'prebilllist', component: PrebillComponent, data: { title: 'Billing', module: 'Billing' } },
  { path: 'prebill-new/:code', component: PrebillNewComponent, data: { title: 'Billing', module: 'Billing' } },
  { path: 'prebill-approve/:code', component: PrebillAaproveComponent, data: { title: 'Billing', module: 'Billing' } },
  { path: 'prebilllist/:code', component: PrebillComponent, data: { title: 'Billing', module: 'Billing' } },
  { path: 'billlist', component: BillComponent, data: { title: 'Billing', module: 'Billing' } },
  { path: 'newbilllist', component: NewbillListComponent, data: { title: 'Billing', module: 'Billing' } },
  { path: 'billingdetails/:code', component: InvoiceDetailsComponent, data: { title: 'Billing', module: 'Billing' } },
  //prebill display
  { path: 'prebill-display/:code', component: PrebillDisplayComponent, data: { title: 'Billing', module: 'Billing' } },

  { path: 'expensesaccounts/:code', component: MatterExpenseComponent, pathMatch: 'full', data: { title: 'Expenses', module: 'Matter' } },
  { path: 'expensesaccounts', component: MatterExpenseComponent, pathMatch: 'full', data: { title: 'Expenses', module: 'Matter' } },

 // { path: 'expensesaccounts/:code', component: MatterExpenseComponent, pathMatch: 'full', data: { title: 'Expenses', module: 'Matter' } },
  { path: 'timeticket', component: TimeticketDiaryComponent, pathMatch: 'full', data: { title: 'Timeticket', module: 'Matter' } },

  
  { path: 'timeticket-users', component: TimeticketSummaryUsersComponent, pathMatch: 'full', data: { title: 'Timeticket', module: 'Matter' } },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AccountsRoutingModule { }
