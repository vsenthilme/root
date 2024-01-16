import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OwnershipComponent } from './ownership/ownership.component';
import { TransactionListComponent } from './transaction-list/transaction-list.component';
import { OwnershipNewComponent } from './ownership/ownership-new/ownership-new.component';
import { BrotherSisterTemplateComponent } from './brother-sister-template/brother-sister-template.component';
import { ValidationComponent } from './validation/validation.component';
import { ProposedComponent } from './proposed/proposed.component';
import { ApprovalComponent } from './approval/approval.component';
import { ProposalComponent } from './validation/proposal/proposal.component';
import { SummaryComponent } from './summary/summary.component';
import { StorePartnerListingComponent } from './store-partner-listing/store-partner-listing.component';
import { MergeComponent } from './store-partner-listing/merge/merge.component';
import { AdminApprovalComponent } from './admin-approval/admin-approval.component';
import { ApprovalDisplayComponent } from './admin-approval/approval-display/approval-display.component';
import { FamilyTemplateComponent } from './Template/family-template/family-template.component';


const routes: Routes = [ {
 path: '',
component: TransactionListComponent,
children:[
// { path:'ownership', component: OwnershipComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },
// { path:'validation', component: ValidationComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },
// { path:'proposed', component: ProposedComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },
// { path:'approval', component: ApprovalComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },

{ path:'ownership/:code', component: OwnershipComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },
{ path:'validation/:code', component: ValidationComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },
{ path:'proposed/:code', component: ProposedComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },
{ path:'approval/:code', component: ApprovalComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },
]},

{ path:'summary', component: SummaryComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },

{ path:'approvalAdmin', component: AdminApprovalComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },

{
  path: '',
 component: ApprovalDisplayComponent,
 children:[
 { path:'proposal1/:code', component: ProposalComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },
 { path:'brotherSisterRemplate1/:code', component: BrotherSisterTemplateComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },
 ]},


{ path:'merge/:code', component: MergeComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },
{ path:'storePartnerListing', component: StorePartnerListingComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },
{ path:'storePartnerListing/:code', component: StorePartnerListingComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },

{ path:'proposal/:code', component: ProposalComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } }, 
{ path:'ownershipNew/:code', component: OwnershipNewComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } }, 
{ path:'brotherSisterRemplate/:code', component: BrotherSisterTemplateComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },
{ path:'familytemplate/:code', component: FamilyTemplateComponent, pathMatch: 'full', data: { title: 'Client', module: 'Controlled Groups' } },
 // { path:'brotherSisterRemplate', component: BrotherSisterTemplateComponent, pathMatch: 'full', data: { title: 'Client', module: 'Brother Sister' } },
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TransactionRoutingModule { }