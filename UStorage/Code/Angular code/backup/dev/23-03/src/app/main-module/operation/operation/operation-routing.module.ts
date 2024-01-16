import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AgreementComponent } from './agreement/agreement.component';
import { InvoiceComponent } from './invoice/invoice.component';
import { PaymentComponent } from './payment/payment.component';
import { WorkorderComponent } from './workorder/workorder.component';

const routes: Routes = [
  {
    path: 'agreement',
    component: AgreementComponent
  },

  {
    path: 'invoice',
    component: InvoiceComponent
  },
  {
    path: 'payment',
    component: PaymentComponent
  },
  {
    path: 'workorder',
    component: WorkorderComponent
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OperationRoutingModule { }
