import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ControlgroupRoutingModule } from './controlgroup-routing.module';
import { SharedModule } from "src/app/shared/shared.module";
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { CommonFieldModule } from "src/app/common-field/common-field.module";

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    ControlgroupRoutingModule,
    SharedModule, CommonFieldModule, AngularMultiSelectModule,
  ]
})
export class ControlgroupModule { }
