import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AgreementComponent } from './agreement/agreement.component';
import { BillingComponent } from './billing/billing.component';
import { CbminboundComponent } from './cbminbound/cbminbound.component';
import { InquiryComponent } from './inquiry/inquiry.component';
import { InvoiceheaderComponent } from './invoiceheader/invoiceheader.component';
import { InvoicelinesComponent } from './invoicelines/invoicelines.component';
import { PricelistComponent } from './pricelist/pricelist.component';
import { PricelistassignmentComponent } from './pricelistassignment/pricelistassignment.component';
import { ProformainvoiceheaderComponent } from './proformainvoiceheader/proformainvoiceheader.component';
import { ProformainvoicelineComponent } from './proformainvoiceline/proformainvoiceline.component';
import { QuotationheaderComponent } from './quotationheader/quotationheader.component';
import { QuotationlineComponent } from './quotationline/quotationline.component';
import { BillingNewComponent } from './billing/billing-new/billing-new.component';
import { PricelistNewComponent } from './pricelist/pricelist-new/pricelist-new.component';
import { PricelistassignmentNewComponent } from './pricelistassignment/pricelistassignment-new/pricelistassignment-new.component';
import { CbminboundNewComponent } from './cbminbound/cbminbound-new/cbminbound-new.component';

const routes: Routes = [
  {
    path: 'pricelist',
    component: PricelistComponent
  },
  {
    path: 'billing',
    component: BillingComponent
  },
  {
    path: 'pricelistassign',
    component: PricelistassignmentComponent

  },
  {
    path: 'cbm',
    component: CbminboundComponent

  },
  {
    path: 'proformainvoiceheader',
    component: ProformainvoiceheaderComponent

  },
  {
    path: 'proformainvoiceline',
    component: ProformainvoicelineComponent

  },
  {
    path: 'invoiceheader',
    component: InvoiceheaderComponent

  },
  {
    path: 'invoiceline',
    component: InvoicelinesComponent

  },
  {
    path: 'inquiry',
    component: InquiryComponent

  },
  {
    path: 'quotationheader',
    component: QuotationheaderComponent

  },
  {
    path: 'quotationline',
    component: QuotationlineComponent

  },
  {
    path: 'agreement',
    component: AgreementComponent

  },
  {
    path: 'billingNew/:code',
    component: BillingNewComponent
  },
  {
    path: 'pricelistNew/:code',
    component: PricelistNewComponent
  },
  {
    path: 'pricelistassignNew/:code',
    component: PricelistassignmentNewComponent
  },
  {
    path: 'cbmNew/:code',
    component: CbminboundNewComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ThreeplRoutingModule { }
