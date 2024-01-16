import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionRoutingModule } from './transaction-routing.module';
import { OwnershipComponent } from './ownership/ownership.component';
import { OwnershipNewComponent } from './ownership/ownership-new/ownership-new.component';
import { SharedModule } from "src/app/shared/shared.module";
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { TransactionListComponent } from './transaction-list/transaction-list.component';
import { OwnershippopComponent } from './ownership/ownership-new/ownershippop/ownershippop.component';
import { BrotherSisterTemplateComponent } from './brother-sister-template/brother-sister-template.component';
import { NgCircleProgressModule } from 'ng-circle-progress';
import { SuccessTemplateComponent } from './brother-sister-template/success-template/success-template.component';
import { ValidationComponent } from './validation/validation.component';
import { ProposalComponent } from './validation/proposal/proposal.component';
import { ProposedComponent } from './proposed/proposed.component';
import { ApprovalComponent } from './approval/approval.component';
import { Ownershipop2Component } from './ownership/ownership-new/ownershipop2/ownershipop2.component';
import { SummaryComponent } from './summary/summary.component';
import { StorePartnerListingComponent } from './store-partner-listing/store-partner-listing.component';
import { EmailRequestComponent } from './summary/email-request/email-request.component';
import { MergeComponent } from './store-partner-listing/merge/merge.component';
import { AdminApprovalComponent } from './admin-approval/admin-approval.component';
import { ApprovalDisplayComponent } from './admin-approval/approval-display/approval-display.component';
import { FamilyTemplateComponent } from './Template/family-template/family-template.component';
import { SucesstmplteComponent } from './Template/family-template/sucesstmplte/sucesstmplte.component';
import { SendEmailApprovalComponent } from './proposed/send-email-approval/send-email-approval.component';
import { NgxEditorModule } from "ngx-editor";
import { UploadFilesComponent } from './ownership/ownership-new/upload-files/upload-files.component';

@NgModule({
  declarations: [
    OwnershipComponent,
    OwnershipNewComponent,
    TransactionListComponent,
    OwnershippopComponent,
    BrotherSisterTemplateComponent,
    SuccessTemplateComponent,
    ValidationComponent,
    ProposalComponent,
    ProposedComponent,
    ApprovalComponent,
    Ownershipop2Component,
    SummaryComponent,
    StorePartnerListingComponent,
    EmailRequestComponent,
    MergeComponent,
    AdminApprovalComponent,
    ApprovalDisplayComponent,
    FamilyTemplateComponent,
    SucesstmplteComponent,
    SendEmailApprovalComponent,
    UploadFilesComponent,
  ],
  imports: [
    CommonModule,
    TransactionRoutingModule,
    SharedModule, CommonFieldModule, AngularMultiSelectModule, NgxEditorModule,

    NgCircleProgressModule.forRoot({
      // set defaults here
      radius: 100,
      outerStrokeWidth: 16,
      innerStrokeWidth: 8,
      outerStrokeColor: "#78C000",
      innerStrokeColor: "#C7E596",
      animationDuration: 300,
    }),

  ]
})
export class TransactionModule { }