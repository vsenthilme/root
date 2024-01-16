import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ngfModule } from "angular-file";
import { PdfViewerModule } from "ng2-pdf-viewer";
import { NgxEditorModule } from "ngx-editor";
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { SharedModule } from "src/app/shared/shared.module";
import { AccountingComponent } from "./accounting/accounting.component";
import { CaseManagementRoutingModule } from "./case-management-routing.module";
import { ClientdocumentComponent } from "./document -client/clientdocument/clientdocument.component";
import { DocumentClientportalComponent } from "./document -client/document-clientportal/document-clientportal.component";
import { DocumentPopupComponent } from "./document -client/document-clientportal/document-popup/document-popup.component";
import { ExpensesNewComponent } from "./expenses/expenses-new/expenses-new.component";
import { ExpensesComponent } from "./expenses/expenses.component";
import { ExpirationDateComponent } from "./expiration-date/expiration-date.component";
import { ExpirationNewComponent } from "./expiration-date/expiration-new/expiration-new.component";
import { FeessharingListComponent } from "./feessharing-list/feessharing-list.component";
import { FeessharingNewComponent } from "./feessharing-list/feessharing-new/feessharing-new.component";
import { GeneralListComponent } from "./General/general-list/general-list.component";
import { MatterEditComponent } from "./General/matter-edit/matter-edit.component";
import { DocumentNEWComponent } from "./matter-document/document-new/document-new.component";
import { MatterDocumentComponent } from "./matter-document/matter-document.component";
import { MatterdocumentSendComponent } from "./matter-document/matterdocument-send/matterdocument-send.component";
import { IntakeSelectionComponent } from "./matter-intake/intake-selection/intake-selection.component";
import { MatterIntakeComponent } from "./matter-intake/matter-intake.component";
import { EmailNEWComponent } from "./matters-email/email-new/email-new.component";
import { MattersEmailComponent } from "./matters-email/matters-email.component";
import { MatternotesNewComponent } from "./matters-notes/matternotes-new/matternotes-new.component";
import { MattersNotesComponent } from "./matters-notes/matters-notes.component";
import { MattersTabComponent } from "./matters-tab/matters-tab.component";
import { RateListComponent } from "./rate-list/rate-list.component";
import { RateNewComponent } from "./rate-list/rate-new/rate-new.component";
import { ReceiptNewComponent } from "./receipt-no/receipt-new/receipt-new.component";
import { ReceiptNoComponent } from "./receipt-no/receipt-no.component";
import { TaskNewComponent } from "./task/task-new/task-new.component";
import { TaskComponent } from "./task/task.component";
import { TimeNewComponent } from "./time-tickets/time-new/time-new.component";
import { TimeTicketsComponent } from "./time-tickets/time-tickets.component";
import { ChecklistPopupComponent } from './document -client/clientdocument/checklist-popup/checklist-popup.component';
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { MatterActiviyComponent } from './matter-activiy/matter-activiy.component';
import { SharedPopupComponent } from './document -client/document-clientportal/shared-popup/shared-popup.component';
import { TransferactivityComponent } from './transferactivity/transferactivity.component';
import { DocumentChecklistComponent } from './document -client/clientdocument/document-checklist/document-checklist.component';
import { ExpensesChequeComponent } from './expenses/expenses-cheque/expenses-cheque.component';
import { EmailComponent } from './expenses/email/email.component';
import { TextMaskModule } from "angular2-text-mask";



@NgModule({
  declarations: [
    GeneralListComponent,
    MattersTabComponent,
    AccountingComponent,
    RateListComponent,
    RateNewComponent,
    FeessharingListComponent,
    ExpensesComponent,
    TaskComponent,
    TimeTicketsComponent,
    FeessharingNewComponent,
    ExpensesNewComponent,
    TaskNewComponent,
    TimeNewComponent,
    MattersNotesComponent,
    MatternotesNewComponent,
    MattersEmailComponent,
    MatterDocumentComponent,
    EmailNEWComponent,
    DocumentNEWComponent,
    MatterdocumentSendComponent,
    MatterEditComponent,
    MatterIntakeComponent,
    IntakeSelectionComponent,
    ExpirationDateComponent,
    ExpirationNewComponent,
    ReceiptNoComponent,
    ReceiptNewComponent,
    DocumentClientportalComponent,
    ClientdocumentComponent,
    DocumentPopupComponent,
    ChecklistPopupComponent,
    MatterActiviyComponent,
    SharedPopupComponent,
    TransferactivityComponent,
    DocumentChecklistComponent,
    ExpensesChequeComponent,
    EmailComponent,
  ],
  imports: [
    CommonModule,
    CaseManagementRoutingModule,
    SharedModule, CommonFieldModule, NgxEditorModule, PdfViewerModule, ngfModule,AngularMultiSelectModule, TextMaskModule,
  ],
  exports: [
    GeneralListComponent,
    MattersTabComponent,
    AccountingComponent,
    RateListComponent,
    RateNewComponent,
    FeessharingListComponent,
    ExpensesComponent,
    TaskComponent,
    TimeTicketsComponent,
    MattersNotesComponent,
    MatternotesNewComponent,
    MattersEmailComponent,
    MatterDocumentComponent,
    EmailNEWComponent,
    DocumentNEWComponent,
    MatterdocumentSendComponent,
    MatterEditComponent,
    MatterIntakeComponent,
    IntakeSelectionComponent,
    DocumentClientportalComponent,
    ClientdocumentComponent,
  ]
})
export class CaseManagementModule { }
