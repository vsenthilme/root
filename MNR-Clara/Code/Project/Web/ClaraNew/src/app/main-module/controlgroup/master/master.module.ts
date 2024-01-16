import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from "src/app/shared/shared.module";
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { MasterRoutingModule } from './master-routing.module';
import { ClientComponent } from './client/client.component';
import { ClientNewComponent } from './client/client-new/client-new.component';
import { ControlgroupComponent } from './controlgroup/controlgroup.component';
import { ControlgroupNewComponent } from './controlgroup/controlgroup-new/controlgroup-new.component';
import { ClientcontrolgroupComponent } from './clientcontrolgroup/clientcontrolgroup.component';
import { ClientcontrolgroupNewComponent } from './clientcontrolgroup/clientcontrolgroup-new/clientcontrolgroup-new.component';
import { CliententityassignmentComponent } from './cliententityassignment/cliententityassignment.component';
import { CliententityassignmentNewComponent } from './cliententityassignment/cliententityassignment-new/cliententityassignment-new.component';
import { ClientpopupComponent } from './client/client-new/clientpopup/clientpopup.component';



@NgModule({
  declarations: [
    ClientComponent,
    ClientNewComponent,
    ControlgroupComponent,
    ControlgroupNewComponent,
    ClientcontrolgroupComponent,
    ClientcontrolgroupNewComponent,
    CliententityassignmentComponent,
    CliententityassignmentNewComponent,
    ClientpopupComponent,
  ],
  imports: [
    CommonModule,
    MasterRoutingModule,
    SharedModule, CommonFieldModule, AngularMultiSelectModule,
  ]
})
export class MasterModule { }
