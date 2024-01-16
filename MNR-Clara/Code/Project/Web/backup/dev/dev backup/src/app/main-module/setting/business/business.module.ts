import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ngfModule } from "angular-file";
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { SharedModule } from "src/app/shared/shared.module";
import { ActivityCodeComponent } from "./activity-code/activity-code.component";
import { ActivitycodeDisplayComponent } from "./activity-code/activitycode-display/activitycode-display.component";
import { AdminCostComponent } from "./admin-cost/admin-cost.component";
import { AdmincostDisplayComponent } from "./admin-cost/admincost-display/admincost-display.component";
import { AgreeementTemplateComponent } from "./agreeement-template/agreeement-template.component";
import { AgreementtemplateDisplayComponent } from "./agreeement-template/agreementtemplate-display/agreementtemplate-display.component";
import { BillingFormatComponent } from "./billing-format/billing-format.component";
import { BillingformatDisplayComponent } from "./billing-format/billingformat-display/billingformat-display.component";
import { BillingFrequencyComponent } from "./billing-frequency/billing-frequency.component";
import { BillingfreqDisplayComponent } from "./billing-frequency/billingfreq-display/billingfreq-display.component";
import { BillingDisplayComponent } from "./billing-mode/billing-display/billing-display.component";
import { BillingModeComponent } from "./billing-mode/billing-mode.component";
import { BusinessRoutingModule } from "./business-routing.module";
import { BusinessTabbarComponent } from "./business-tabbar/business-tabbar.component";
import { CaseCategoryElementComponent } from "./case-category/case-category.component";
import { CasecategoryDisplayComponent } from "./case-category/casecategory-display/casecategory-display.component";
import { CasesubCategoryComponent } from "./casesub-category/casesub-category.component";
import { CasesubDisplayComponent } from "./casesub-category/casesub-display/casesub-display.component";
import { ChartDisplayComponent } from "./chart-of-accounts/chart-display/chart-display.component";
import { ChartOfAccountsComponent } from "./chart-of-accounts/chart-of-accounts.component";
import { DeadlineCalculatorComponent } from "./deadline-calculator/deadline-calculator.component";
import { DeadlineDisplayComponent } from "./deadline-calculator/deadline-display/deadline-display.component";
import { documentTemplateComponent } from "./document-template/document-template.component";
import { documenttemplateDisplayComponent } from "./document-template/documenttemplate-display/documenttemplate-display.component";
import { ExpenseCodeComponent } from "./expense-code/expense-code.component";
import { ExpenseDisplayComponent } from "./expense-code/expense-display/expense-display.component";
import { InquiryModeElementComponent } from "./inquiry-mode/inquiry-mode.component";
import { InquirymodeDisplayComponent } from "./inquiry-mode/inquirymode-display/inquirymode-display.component";
import { NoteTypeComponent } from "./note-type/note-type.component";
import { NotetypeDisplayComponent } from "./note-type/notetype-display/notetype-display.component";
import { ReferralDisplayComponent } from "./referral/referral-display/referral-display.component";
import { ReferralComponent } from "./referral/referral.component";
import { TaskTypeComponent } from "./task-type/task-type.component";
import { TasktypeDisplayComponent } from "./task-type/tasktype-display/tasktype-display.component";
import { TaskbasedCodeComponent } from "./taskbased-code/taskbased-code.component";
import { TaskcodeDisplayComponent } from "./taskbased-code/taskcode-display/taskcode-display.component";
import { TimekeeperDisplayComponent } from "./timekeeper/timekeeper-display/timekeeper-display.component";
import { TimekeeperComponent } from "./timekeeper/timekeeper.component";
import { AgreementTemplateViewComponent } from './agreeement-template/agreement-template-view/agreement-template-view.component';
import { CasecatSearchComponent } from './case-category/casecat-search/casecat-search.component';
import { ExpirationDocumentComponent } from './expiration-document/expiration-document.component';
import { ExpirationdocumentNewComponent } from './expiration-document/expirationdocument-new/expirationdocument-new.component';
import { GlMappingComponent } from './gl-mapping/gl-mapping.component';
import { GlmappingNewComponent } from './gl-mapping/glmapping-new/glmapping-new.component';
import { DocumentChecklistComponent } from './document-checklist/document-checklist.component';
import { ChecklistNewComponent } from './document-checklist/checklist-new/checklist-new.component';
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { PdfViewerModule } from "ng2-pdf-viewer";
import { DownloadComponent } from './document-checklist/download/download.component';



@NgModule({
  declarations: [
    TimekeeperComponent,
    TimekeeperDisplayComponent,
    BusinessTabbarComponent,
    CaseCategoryElementComponent,
    CasecategoryDisplayComponent,
    CasesubCategoryComponent,
    CasesubDisplayComponent,
    ActivityCodeComponent,
    ActivitycodeDisplayComponent,
    TaskbasedCodeComponent,
    TaskcodeDisplayComponent,
    ChartOfAccountsComponent,
    ChartDisplayComponent,
    ExpenseCodeComponent,
    ExpenseDisplayComponent,
    TaskTypeComponent,
    TasktypeDisplayComponent,
    AdminCostComponent,
    AdmincostDisplayComponent,
    BillingModeComponent,
    BillingDisplayComponent,
    BillingFormatComponent,
    BillingformatDisplayComponent,
    DeadlineCalculatorComponent,
    DeadlineDisplayComponent,
    AgreeementTemplateComponent,
    AgreementtemplateDisplayComponent,
    documentTemplateComponent,
    documenttemplateDisplayComponent,
    InquiryModeElementComponent,
    InquirymodeDisplayComponent,
    ReferralComponent,
    ReferralDisplayComponent,
    NoteTypeComponent,
    NotetypeDisplayComponent,
    BillingFrequencyComponent,
    BillingfreqDisplayComponent,
    AgreementTemplateViewComponent,
    CasecatSearchComponent,
    ExpirationDocumentComponent,
    ExpirationdocumentNewComponent,
    GlMappingComponent,
    GlmappingNewComponent,
    DocumentChecklistComponent,
    ChecklistNewComponent,
    DownloadComponent,
  ],
  imports: [
    CommonModule,
    BusinessRoutingModule,
    SharedModule,
    CommonFieldModule, ngfModule, AngularMultiSelectModule,PdfViewerModule
  ],
  exports: [

  ],
})
export class BusinessModule { }
