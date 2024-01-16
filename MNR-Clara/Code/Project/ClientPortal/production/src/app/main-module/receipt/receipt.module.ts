import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReceiptRoutingModule } from './receipt-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { ReceiptListComponent } from './receipt-list/receipt-list.component';


@NgModule({
  declarations: [
    ReceiptListComponent
  ],
  imports: [
    CommonModule,
    ReceiptRoutingModule,
    SharedModule,
    CommonFieldModule
  ]
})
export class ReceiptModule { }
