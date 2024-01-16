import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { Form007Component } from "src/app/customerform/form007/form007.component";
import { Form009SpanishComponent } from "src/app/customerform/form009-spanish/form009-spanish.component";
import { Form010EnglishComponent } from "src/app/customerform/form010-english/form010-english.component";
import { Form011EnglishComponent } from "src/app/customerform/form011-english/form011-english.component";
import { Itform008SpanishComponent } from "src/app/customerform/itform008-spanish/itform008-spanish.component";
import { AccountingComponent } from "./accounting/accounting.component";
import { ClientdocumentComponent } from "./document -client/clientdocument/clientdocument.component";
import { DocumentClientportalComponent } from "./document -client/document-clientportal/document-clientportal.component";
import { ExpensesComponent } from "./expenses/expenses.component";
import { ExpirationDateComponent } from "./expiration-date/expiration-date.component";
import { FeessharingListComponent } from "./feessharing-list/feessharing-list.component";
import { GeneralListComponent } from "./General/general-list/general-list.component";
import { MatterEditComponent } from "./General/matter-edit/matter-edit.component";
import { MatterActiviyComponent } from "./matter-activiy/matter-activiy.component";
import { MatterDocumentComponent } from "./matter-document/matter-document.component";
import { MatterdocumentSendComponent } from "./matter-document/matterdocument-send/matterdocument-send.component";
import { MatterIntakeComponent } from "./matter-intake/matter-intake.component";
import { MattersNotesComponent } from "./matters-notes/matters-notes.component";
import { MattersTabComponent } from "./matters-tab/matters-tab.component";
import { RateListComponent } from "./rate-list/rate-list.component";
import { ReceiptNoComponent } from "./receipt-no/receipt-no.component";
import { TaskComponent } from "./task/task.component";
import { TimeTicketsComponent } from "./time-tickets/time-tickets.component";
import { TransferactivityComponent } from "./transferactivity/transferactivity.component";



const routes: Routes = [
  { path: 'general', component: GeneralListComponent, pathMatch: 'full', data: { title: 'General', module: 'Matter' } },

 { path: 'document_template/:code', component: MatterdocumentSendComponent, pathMatch: 'full', data: { title: 'Document', module: 'Matter' } },
 { path: 'document_template/:pageType/:code', component: MatterdocumentSendComponent, pathMatch: 'full', data: { title: 'Document', module: 'Matter' } },
  { path: 'matteractivity', component: MatterActiviyComponent, pathMatch: 'full', data: { title: 'Matteractivity', module: 'Matter' } },
  { path: 'transferactivity', component: TransferactivityComponent, pathMatch: 'full', data: { title: 'Transferactivity', module: 'Matter' } },  
  { path: 'form007/:code', component: Form007Component, pathMatch: 'full', data: { title: 'Form007', module: 'Matter' } },
  { path: 'form008/:code', component: Itform008SpanishComponent, pathMatch: 'full', data: { title: 'Form007', module: 'Matter' } },
  { path: 'form009/:code', component: Form009SpanishComponent, pathMatch: 'full', data: { title: 'Form009', module: 'Matter' } },
  { path: 'form010/:code', component: Form010EnglishComponent, pathMatch: 'full', data: { title: 'Form010', module: 'Matter' } },
  { path: 'form011/:code', component: Form011EnglishComponent, pathMatch: 'full', data: { title: 'Form011', module: 'Matter' } },

  {
    path: '',
    component: MattersTabComponent,
    children: [
      { path: 'matter/:code', component: MatterEditComponent, pathMatch: 'full', data: { title: 'Matter', module: 'Matter' } },
      { path: 'tasklist/:code', component: TaskComponent, pathMatch: 'full', data: { title: 'Task List', module: 'Matter' } },
      { path: 'notes/:code', component: MattersNotesComponent, pathMatch: 'full', data: { title: 'Notes', module: 'Matter' } },
      { path: 'timeticket/:code', component: TimeTicketsComponent, pathMatch: 'full', data: { title: ' Time Tickets', module: 'Matter' } },
      { path: 'document/:code', component: MatterDocumentComponent, pathMatch: 'full', data: { title: 'Document', module: 'Matter' } },
      { path: 'expirationdate/:code', component: ExpirationDateComponent, pathMatch: 'full', data: { title: 'Expiration Date', module: 'Matter' } },

//mugilan added 
{ path: 'documenttab/:code', component:  ClientdocumentComponent, pathMatch: 'full', data: { title: 'Document - Client Portal', module: 'Matter' } },
{ path: 'clientdocument/:code', component: DocumentClientportalComponent, pathMatch: 'full', data: { title: 'Document - Client Portal', module: 'Matter' } },



      { path: 'receiptno/:code', component: ReceiptNoComponent, pathMatch: 'full', data: { title: 'Receipt No', module: 'Matter' } },
      { path: 'accounting/:code', component: AccountingComponent, pathMatch: 'full', data: { title: 'Accounting', module: 'Matter' } },
      { path: 'intake/:code', component: MatterIntakeComponent, pathMatch: 'full', data: { title: 'Intake', module: 'Matter' } },
      { path: 'expenses/:code', component: ExpensesComponent, pathMatch: 'full', data: { title: 'Expenses', module: 'Matter' } },
      { path: 'rate/:code', component: RateListComponent, pathMatch: 'full', data: { title: 'Rate', module: 'Matter' } },
      { path: 'feessharing/:code', component: FeessharingListComponent, pathMatch: 'full', data: { title: 'Fees Sharing', module: 'Matter' } },


      // { path: 'status', component: StatusIdComponent, pathMatch: 'full', data: { title: 'Status' , module: 'Matter' } },
      // { path: 'message', component: MessageIdComponent, pathMatch: 'full', data: { title: 'Message' , module: 'Matter' } },
      // { path: 'transaction', component: TransactionIdComponent, pathMatch: 'full', data: { title: 'Transaction' , module: 'Matter' } },
      // { path: 'numberrange', component: NumberRangeComponent, pathMatch: 'full', data: { title: 'Number Range' , module: 'Matter' } },



    ]
  },
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CaseManagementRoutingModule { }