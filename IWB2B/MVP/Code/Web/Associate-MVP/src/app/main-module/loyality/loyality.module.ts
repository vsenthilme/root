import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LoyalityRoutingModule } from './loyality-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { LoyaltyCategoryComponent } from './loyalty-category/loyalty-category.component';
import { LoyaltySetupComponent } from './loyalty-setup/loyalty-setup.component';
import { CategoryNewComponent } from './loyalty-category/category-new/category-new.component';
import { SetupNewComponent } from './loyalty-setup/setup-new/setup-new.component';


@NgModule({
  declarations: [
    LoyaltyCategoryComponent,
    LoyaltySetupComponent,
    CategoryNewComponent,
    SetupNewComponent
  ],
  imports: [
    CommonModule,
    LoyalityRoutingModule,
    SharedModule,
  ]
})
export class LoyalityModule { }
