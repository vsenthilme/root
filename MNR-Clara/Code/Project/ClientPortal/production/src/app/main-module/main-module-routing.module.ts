import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardModule } from './dashboard/dashboard.module';
import { DocumentsModule } from './documents/documents.module';
import { HeaderComponent } from './header/header.component';
import { InvoiceModule } from './invoice/invoice.module';
import { MattersModule } from './matters/matters.module';
import { PaymentPlanModule } from './payment-plan/payment-plan.module';
import { ProfileModule } from './profile/profile.module';
import { QuotationModule } from './quotation/quotation.module';
import { ReceiptModule } from './receipt/receipt.module';


const routes: Routes = [{
  path: '',
  component: HeaderComponent,
  children: [
    {
      path: 'dashboard',
      loadChildren: () => import('./dashboard/dashboard.module').then(m => DashboardModule)

    },
    {
      path: 'profile',
      loadChildren: () => import('./profile/profile.module').then(m => ProfileModule)

    },
    {
      path: 'invoice',
      loadChildren: () => import('./invoice/invoice.module').then(m => InvoiceModule)

    },
    {
      path: 'matters',
      loadChildren: () => import('./matters/matters.module').then(m => MattersModule)

    },
    {
      path: 'quotations',
      loadChildren: () => import('./quotation/quotation.module').then(m => QuotationModule)

    },
    {
      path: 'documents',
      loadChildren: () => import('./documents/documents.module').then(m => DocumentsModule)

    },
    {
      path: 'payment',
      loadChildren: () => import('./payment-plan/payment-plan.module').then(m => PaymentPlanModule)

    },
    {
      path: 'receipt',
      loadChildren: () => import('./receipt/receipt.module').then(m => ReceiptModule)

    },
  ]
}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MainModuleRoutingModule { }
