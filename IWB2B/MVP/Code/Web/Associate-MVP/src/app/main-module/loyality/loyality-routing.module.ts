import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoyaltyCategoryComponent } from './loyalty-category/loyalty-category.component';
import { LoyaltySetupComponent } from './loyalty-setup/loyalty-setup.component';

const routes: Routes = [
  { path: 'loyaltyCategory', component: LoyaltyCategoryComponent, },
  { path: 'loyaltySetup', component: LoyaltySetupComponent, },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LoyalityRoutingModule { }
