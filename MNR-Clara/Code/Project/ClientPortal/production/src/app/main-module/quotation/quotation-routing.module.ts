import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { QuotationPdfComponent } from './quotation-pdf/quotation-pdf.component';
import { QuotationsComponent } from './quotations/quotations.component';

const routes: Routes = [
  { path: 'quotations', component: QuotationsComponent,  },
 // { path: 'quotationpdf', component: QuotationPdfComponent,  },
  { path: 'quotationpdf/:code', component: QuotationPdfComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class QuotationRoutingModule { }
