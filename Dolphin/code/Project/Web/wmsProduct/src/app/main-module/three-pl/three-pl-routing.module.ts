import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InvoiceNewComponent } from './invoice/invoice-new/invoice-new.component';
import { InvoiceComponent } from './invoice/invoice.component';
import { ProformaNewComponent } from './proforma/proforma-new/proforma-new.component';
import { ProformaComponent } from './proforma/proforma.component';
import { ThreeplTransferComponent } from './threepl-transfer/threepl-transfer.component';

const routes: Routes = [ {
  path: 'profoma',
  component: ProformaComponent
},
{
  path: 'profoma-new',
  component: ProformaNewComponent
},
{
  path: 'invoice',
  component: InvoiceComponent
},
{
  path: 'invoice-new',
  component: InvoiceNewComponent
},

{
  path: 'threepltransfer',
  component: ThreeplTransferComponent
},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ThreePlRoutingModule { }
