import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EnquiryComponent } from './enquiry/enquiry.component';
import { QuoteComponent } from './quote/quote.component';

const routes: Routes = [

  {
    path: 'enquiry',
    component: EnquiryComponent
  },

  {
    path: 'quote',
    component: QuoteComponent
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CrmRoutingModule { }
