import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PreoutboundRoutingModule } from './preoutbound-routing.module';
import { PreoutboundMainComponent } from './preoutbound-main/preoutbound-main.component';
import { PreoutboundCreateComponent } from './preoutbound-create/preoutbound-create.component';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { InventoryQtyComponent } from './preoutbound-create/inventory-qty/inventory-qty.component';
import { OrderManagementModule } from '../order-management/order-management.module';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { PreoutboundNewComponent } from './preoutbound-new/preoutbound-new.component';
import { PopupComponent } from './preoutbound-new/popup/popup.component';
import { InterwarehousecreateComponent } from './interwarehousecreate/interwarehousecreate.component';
import { InterwarehouselinesComponent } from './interwarehousecreate/interwarehouselines/interwarehouselines.component';
import { SalesinvoiceComponent } from './salesinvoice/salesinvoice.component';



@NgModule({
  declarations: [
    PreoutboundMainComponent,
    PreoutboundCreateComponent,
    InventoryQtyComponent,
    PreoutboundNewComponent,
    PopupComponent,
    InterwarehousecreateComponent,
    InterwarehouselinesComponent,
    SalesinvoiceComponent
  ],
  imports: [
    CommonModule,
    PreoutboundRoutingModule,
    SharedModule,
    CommonFieldModule,
    OrderManagementModule,
    NgMultiSelectDropDownModule.forRoot(),
    AngularMultiSelectModule,
  ],
  exports: [
    PreoutboundMainComponent,
    PreoutboundCreateComponent,
    InventoryQtyComponent
  ]
})
export class PreoutboundModule { }
