import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InquirySearchComponent } from '../crm/inquiries/inquiry-search/inquiry-search.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TreeComponent } from './tree/tree.component';

const routes: Routes = [
  {
  path: '',
  component: DashboardComponent,
  pathMatch: 'full', data: { title: 'Home', module: 'Home' } },
 //{ path: '', component: InquirySearchComponent, pathMatch: 'full', data:  { title: 'Inquiry Validation', module: 'CRM' } },
 { path: 'tree', component: TreeComponent, pathMatch: 'full', data:  { title: 'Inquiry Validation', module: 'CRM' } },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule { }
