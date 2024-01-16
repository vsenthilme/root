import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { AdminTabComponent } from './admin-tab/admin-tab.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { UserprofileNewComponent } from './user-profile/userprofile-new/userprofile-new.component';
import { SystemTypeComponent } from './system/system-type/system-type.component';
import { NumberingSeriesComponent } from './system/numbering-series/numbering-series.component';
import { PartnerCodeComponent } from './partner/partner-code/partner-code.component';
import { PartnerNumberingComponent } from './partner/partner-numbering/partner-numbering.component';
import { PartnerCountryComponent } from './partner/partner-country/partner-country.component';
import { SystemTypeNewComponent } from './system/system-type/system-type-new/system-type-new.component';
import { SystemnumberingNewComponent } from './system/numbering-series/systemnumbering-new/systemnumbering-new.component';
import { PartnercodeNewComponent } from './partner/partner-code/partnercode-new/partnercode-new.component';
import { PartnernumberingNewComponent } from './partner/partner-numbering/partnernumbering-new/partnernumbering-new.component';
import { CountryNewComponent } from './partner/partner-country/country-new/country-new.component';
import { UserCreationComponent } from './user-creation/user-creation.component';
import { UserCreationNewComponent } from './user-creation/user-creation-new/user-creation-new.component';
import { ClientAssignmentComponent } from './client-assignment/client-assignment.component';
import { ClientAssignmentNewComponent } from './client-assignment/client-assignment-new/client-assignment-new.component';


@NgModule({
  declarations: [
    UserProfileComponent,
    AdminTabComponent,
    UserprofileNewComponent,
    SystemTypeComponent,
    NumberingSeriesComponent,
    PartnerCodeComponent,
    PartnerNumberingComponent,
    PartnerCountryComponent,
    SystemTypeNewComponent,
    SystemnumberingNewComponent,
    PartnercodeNewComponent,
    PartnernumberingNewComponent,
    CountryNewComponent,
    UserCreationComponent,
    UserCreationNewComponent,
    ClientAssignmentComponent,
    ClientAssignmentNewComponent,
  ],
  imports: [
    CommonModule,
    SharedModule,
    AdminRoutingModule,
  ]
})
export class AdminModule { }
