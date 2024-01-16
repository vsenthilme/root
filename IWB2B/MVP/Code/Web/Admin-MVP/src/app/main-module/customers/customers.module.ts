import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CustomersRoutingModule } from './customers-routing.module';
import { CustomerProfileComponent } from './customer-profile/customer-profile.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { ProfileNewComponent } from './customer-profile/profile-new/profile-new.component';

@NgModule({
  declarations: [
    CustomerProfileComponent,
    ProfileNewComponent
  ],
  imports: [
    CommonModule,
    CustomersRoutingModule,
    SharedModule,
  ],
  exports: [
    CustomerProfileComponent,
    ProfileNewComponent
  ]
})
export class CustomersModule { }
