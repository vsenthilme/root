import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MakeandchangeRoutingModule } from './makeandchange-routing.module';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { InhouseTransferModule } from './inhouse-transfer/inhouse-transfer.module';
import { WarehouseTransferModule } from './warehouse-transfer/warehouse-transfer.module';


@NgModule({
  declarations: [
  
  ],
  imports: [
    CommonModule,
    MakeandchangeRoutingModule,
     NgMultiSelectDropDownModule.forRoot(),
     AngularMultiSelectModule,
       InhouseTransferModule,
    WarehouseTransferModule
  ]
})
export class MakeandchangeModule { }
