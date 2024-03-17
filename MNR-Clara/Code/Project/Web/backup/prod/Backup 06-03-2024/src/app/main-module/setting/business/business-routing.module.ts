import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { ActivityCodeComponent } from "./activity-code/activity-code.component";
import { AdminCostComponent } from "./admin-cost/admin-cost.component";
import { AgreeementTemplateComponent } from "./agreeement-template/agreeement-template.component";
import { AgreementTemplateViewComponent } from "./agreeement-template/agreement-template-view/agreement-template-view.component";
import { BillingFormatComponent } from "./billing-format/billing-format.component";
import { BillingFrequencyComponent } from "./billing-frequency/billing-frequency.component";
import { BillingModeComponent } from "./billing-mode/billing-mode.component";
import { BusinessTabbarComponent } from "./business-tabbar/business-tabbar.component";
import { CaseCategoryElementComponent } from "./case-category/case-category.component";
import { CasesubCategoryComponent } from "./casesub-category/casesub-category.component";
import { ChartOfAccountsComponent } from "./chart-of-accounts/chart-of-accounts.component";
import { DeadlineCalculatorComponent } from "./deadline-calculator/deadline-calculator.component";
import { DocumentChecklistComponent } from "./document-checklist/document-checklist.component";
import { documentTemplateComponent } from "./document-template/document-template.component";
import { ExpenseCodeComponent } from "./expense-code/expense-code.component";
import { ExpirationDocumentComponent } from "./expiration-document/expiration-document.component";
import { GlMappingComponent } from "./gl-mapping/gl-mapping.component";
import { InquiryModeElementComponent } from "./inquiry-mode/inquiry-mode.component";
import { NoteTypeComponent } from "./note-type/note-type.component";
import { ReferralComponent } from "./referral/referral.component";
import { TaskTypeComponent } from "./task-type/task-type.component";
import { TaskbasedCodeComponent } from "./taskbased-code/taskbased-code.component";
import { TimekeeperComponent } from "./timekeeper/timekeeper.component";
const routes: Routes = [
  { path: 'agreementtemplateview/:code', component: AgreementTemplateViewComponent, pathMatch: 'full', data: { title: 'Template view', module: 'Business' } },
  {
    path: '',
    component: BusinessTabbarComponent,
    children: [

      { path: 'casecategory', component: CaseCategoryElementComponent, pathMatch: 'full', data: { title: 'Case Category', module: 'Business' } },
      { path: 'casesubcategory', component: CasesubCategoryComponent, pathMatch: 'full', data: { title: 'Case Sub Category', module: 'Business' } },
      { path: 'agreementtemplate', component: AgreeementTemplateComponent, pathMatch: 'full', data: { title: 'Agreement Template', module: 'Business' } },
      { path: 'inquirymode', component: InquiryModeElementComponent, pathMatch: 'full', data: { title: 'Inquiry Mode', module: 'Business' } },
      { path: 'referral', component: ReferralComponent, pathMatch: 'full', data: { title: 'Referral', module: 'Business' } },
      { path: 'notetype', component: NoteTypeComponent, pathMatch: 'full', data: { title: 'Note Type', module: 'Business' } },


      { path: 'timekeeper', component: TimekeeperComponent, pathMatch: 'full', data: { title: 'Time Keeper', module: 'Business' } },
      { path: 'activity', component: ActivityCodeComponent, pathMatch: 'full', data: { title: 'Activity', module: 'Business' } },
      { path: 'taskbased', component: TaskbasedCodeComponent, pathMatch: 'full', data: { title: 'Task Based', module: 'Business' } },
      { path: 'expense', component: ExpenseCodeComponent, pathMatch: 'full', data: { title: 'Expense', module: 'Business' } },
      { path: 'tasktype', component: TaskTypeComponent, pathMatch: 'full', data: { title: 'Task Type', module: 'Business' } },
      { path: 'deadlinecalculator', component: DeadlineCalculatorComponent, pathMatch: 'full', data: { title: 'Deadline Calculator', module: 'Business' } },
      { path: 'documenttemplate', component: documentTemplateComponent, pathMatch: 'full', data: { title: 'Document Template', module: 'Business' } },

      { path: 'documentchecklist', component: DocumentChecklistComponent, pathMatch: 'full', data: { title: 'Document Checklist', module: 'Business' } },

      { path: 'coc', component: ChartOfAccountsComponent, pathMatch: 'full', data: { title: 'Chart Of Accounts', module: 'Business' } },
      { path: 'admincost', component: AdminCostComponent, pathMatch: 'full', data: { title: 'Admin Cost', module: 'Business' } },
      { path: 'billingmode', component: BillingModeComponent, pathMatch: 'full', data: { title: 'Billing Mode', module: 'Business' } },
      { path: 'billingfrequency', component: BillingFrequencyComponent, pathMatch: 'full', data: { title: 'Billing Frequency', module: 'Business' } },
      { path: 'billingformat', component: BillingFormatComponent, pathMatch: 'full', data: { title: 'Billing Format', module: 'Business' } },

      //added new
      { path: 'expirationdocument', component: ExpirationDocumentComponent, pathMatch: 'full', data: { title: 'Expiration Document', module: 'Business' } },
     // { path: 'glmapping', component: GlMappingComponent, pathMatch: 'full', data: { title: 'GL Mapping', module: 'Business' } },
    ]
  }
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BusinessRoutingModule { }
