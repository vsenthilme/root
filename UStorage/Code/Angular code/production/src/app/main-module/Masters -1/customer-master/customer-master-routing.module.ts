import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerListComponent } from './customer-list/customer-list.component';
import { CustomermaterNewComponent } from './customermater-new/customermater-new.component';

const routes: Routes = [

  {
    path: 'customerlist',
    component: CustomerListComponent
  },
  {
    path: 'customermaster-new/:code',
    component: CustomermaterNewComponent
  },
  {
    path: 'customermaster-new',
    component: CustomermaterNewComponent
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomerMasterRoutingModule { }
