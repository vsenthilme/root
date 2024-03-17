import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TranserrorRoutingModule } from './transerror-routing.module';
import { TranserrorComponent } from './transerror/transerror.component';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { TranserrorTabComponent } from './transerror-tab/transerror-tab.component';


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
