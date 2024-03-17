import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminTabComponent } from './admin-tab/admin-tab.component';
import { ClientAssignmentComponent } from './client-assignment/client-assignment.component';
import { PartnerCodeComponent } from './partner/partner-code/partner-code.component';
import { PartnerCountryComponent } from './partner/partner-country/partner-country.component';
import { PartnerNumberingComponent } from './partner/partner-numbering/partner-numbering.component';
import { NumberingSeriesComponent } from './system/numbering-series/numbering-series.component';
import { SystemTypeComponent } from './system/system-type/system-type.component';
import { UserCreationComponent } from './user-creation/user-creation.component';
import { UserProfileComponent } from './user-profile/user-profile.component';

const routes: Routes = [
  {
    path: 'admintab',
    component: AdminTabComponent
  },
  {
    path: 'userprofile',
    component: UserProfileComponent
  },
  //
  {
    path: 'system-type',
    component: SystemTypeComponent
  },
  {
    path: 'system-numbering',
    component: NumberingSeriesComponent
  },
// PARTNER
  {
    path: 'partnercode',
    component: PartnerCodeComponent
  },
  {
    path: 'partnerCountry',
    component: PartnerCountryComponent
  },
  {
    path: 'partnernumbering',
    component: PartnerNumberingComponent
  },

  //USER CREATION

  {
    path: 'userCreation',
    component: UserCreationComponent
  },

  // CLIENT CREATION

  {
    path: 'clientAssignment',
    component: ClientAssignmentComponent
  },

  {
    path: '',
    redirectTo: 'userprofile'
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
