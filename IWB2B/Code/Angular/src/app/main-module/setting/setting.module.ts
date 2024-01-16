import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SettingRoutingModule } from './setting-routing.module';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { SettingTabComponent } from './setting-tab/setting-tab.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { UserProfileNewComponent } from './user-profile/user-profile-new/user-profile-new.component';


@NgModule({
  declarations: [
    UserProfileComponent,
    SettingTabComponent,
    UserProfileNewComponent
  ],
  imports: [
    CommonModule,
    SettingRoutingModule,
    SharedModule,
  ]
})
export class SettingModule { }
