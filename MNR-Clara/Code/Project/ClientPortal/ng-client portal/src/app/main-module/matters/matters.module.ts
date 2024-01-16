import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MattersRoutingModule } from './matters-routing.module';
import { MattersComponent } from './matters/matters.component';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { MatterPopupComponent } from './matter-popup/matter-popup.component';


@NgModule({
  declarations: [
    MattersComponent,
    MatterPopupComponent
  ],
  imports: [
    CommonModule,
    MattersRoutingModule,
    SharedModule,
    CommonFieldModule
  ]
})
export class MattersModule { }
