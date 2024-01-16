import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserprofileRoutingModule } from './userprofile-routing.module';
import { UserprofileComponent } from './userprofile/userprofile.component';
import { UserprofileNewComponent } from './userprofile/userprofile-new/userprofile-new.component';
import { MatIconModule } from '@angular/material/icon';
import { SharedModule } from 'src/app/shared/shared.module';


@NgModule({
  declarations: [
    UserprofileComponent,
    UserprofileNewComponent
  ],
  imports: [
    CommonModule,
    UserprofileRoutingModule,
    SharedModule,

  ]
})
export class UserprofileModule { }
