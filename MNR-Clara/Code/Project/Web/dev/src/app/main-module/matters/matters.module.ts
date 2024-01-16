import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MattersRoutingModule } from './matters-routing.module';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { CustomerformModule } from 'src/app/customerform/customerform.module';


@NgModule({
  declarations: [
  ],
  imports: [
    CommonModule,
    MattersRoutingModule, CustomerformModule,
    SharedModule, CommonFieldModule
  ]
})
export class MattersModule { }
