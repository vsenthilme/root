import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { QuotationRoutingModule } from './quotation-routing.module';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { QuotationsComponent } from './quotations/quotations.component';
import { QuotationPdfComponent } from './quotation-pdf/quotation-pdf.component';


@NgModule({
  declarations: [
    QuotationsComponent,
    QuotationPdfComponent
  ],
  imports: [
    CommonModule,
    QuotationRoutingModule,
    SharedModule,
    CommonFieldModule
  ]
})
export class QuotationModule { }
