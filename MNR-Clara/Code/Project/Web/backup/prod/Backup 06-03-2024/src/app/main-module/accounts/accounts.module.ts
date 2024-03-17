import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { SharedModule } from "src/app/shared/shared.module";
import { AccountsRoutingModule } from "./accounts-routing.module";
import { BillNewComponent } from "./bill/bill-new/bill-new.component";
import { BillComponent } from "./bill/bill.component";
import { InvoiceDetailsComponent } from "./bill/invoice-details/invoice-details.component";
import { NewbillListComponent } from "./bill/newbill-list/newbill-list.component";
import { PaymentPlanComponent } from "./payment-plan/payment-plan.component";
import { PaymentplanNewComponent } from "./payment-plan/paymentplan-new/paymentplan-new.component";
import { PrebillAaproveComponent } from "./prebill/prebill-aaprove/prebill-aaprove.component";
import { ExpenceDetailsComponent } from "./prebill/prebill-assignpartner/expence-details/expence-details.component";
import { PrebillAssignpartnerComponent } from "./prebill/prebill-assignpartner/prebill-assignpartner.component";
import { AssignPartnerComponent } from "./prebill/prebill-new/assign-partner/assign-partner.component";
import { PrebillNewComponent } from "./prebill/prebill-new/prebill-new.component";
import { TimetocketDetailsComponent } from "./prebill/prebill-new/timetocket-details/timetocket-details.component";
import { PrebillComponent } from "./prebill/prebill.component";
import { QuotationPdfComponent } from "./quotation/quotation-pdf/quotation-pdf.component";
import { QuotationsListComponent } from "./quotation/quotations-list/quotations-list.component";
import { QuotationsNewComponent } from "./quotation/quotations-list/quotations-new/quotations-new.component";
import { PrebillDisplayComponent } from './prebill/prebill-display/prebill-display.component';
import { ExpenseDetailsComponent } from './prebill/prebill-new/expense-details/expense-details.component';
import { TotalamountComponent } from './bill/newbill-list/totalamount/totalamount.component';
import { Client2Component } from './bill/newbill-list/client2/client2.component';
import { PaymentlinkComponent } from './bill/paymentlink/paymentlink.component';
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { PaymentLinkComponent } from './quotation/quotations-list/payment-link/payment-link.component';
import { MatterExpenseComponent } from './matter-expense/matter-expense.component';
import { AccountsexpenseNewComponent } from './matter-expense/accountsexpense-new/accountsexpense-new.component';
import { TimeticketDiaryComponent } from './timeticket-diary/timeticket-diary.component';
import { TimeticketSummaryUsersComponent } from './timeticket-summary-users/timeticket-summary-users.component';
import { PaymentCrudComponent } from './payment-crud/payment-crud.component';
import { PaymentCrudNewComponent } from './payment-crud/payment-crud-new/payment-crud-new.component';
import { TimeTicketNewComponent } from './prebill/prebill-aaprove/time-ticket-new/time-ticket-new.component';
import { SavedInvoiceComponent } from './bill/newbill-list/saved-invoice/saved-invoice.component';
import { TimeticketreportComponent } from './timeticketreport/timeticketreport.component';



@NgModule({
  declarations: [
    QuotationsListComponent,
    QuotationsNewComponent,
    PaymentPlanComponent,
    PaymentplanNewComponent,
    PrebillComponent,
    PrebillNewComponent,
    PrebillAaproveComponent,
    PrebillAssignpartnerComponent,
    AssignPartnerComponent,
    TimetocketDetailsComponent,
    BillComponent,
    BillNewComponent,
    NewbillListComponent,
    InvoiceDetailsComponent,
    QuotationPdfComponent,
    ExpenceDetailsComponent,
    PrebillDisplayComponent,
    ExpenseDetailsComponent,
    TotalamountComponent,
    Client2Component,
    PaymentlinkComponent,
    PaymentLinkComponent,
    MatterExpenseComponent,
    AccountsexpenseNewComponent,
    TimeticketDiaryComponent,
    TimeticketSummaryUsersComponent,
    PaymentCrudComponent,
    PaymentCrudNewComponent,
    TimeTicketNewComponent,
    SavedInvoiceComponent,
    TimeticketreportComponent,
  ],
  imports: [
    CommonModule,
    AccountsRoutingModule,
    SharedModule, CommonFieldModule,
    AngularMultiSelectModule,
  ],
  exports: [
    QuotationsListComponent,
    QuotationsNewComponent
  ]
})
export class AccountsModule { }
