import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TranserrorRoutingModule } from './transerror-routing.module';
import { TranserrorComponent } from './transerror/transerror.component';
import { TranserrorTabComponent } from './transerror-tab/transerror-tab.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';


@NgModule({
  declarations: [
    TranserrorComponent,
    TranserrorTabComponent
  ],
  imports: [
    CommonModule,
    TranserrorRoutingModule,
    SharedModule,
    CommonFieldModule,
  ]
})
export class TranserrorModule { }
