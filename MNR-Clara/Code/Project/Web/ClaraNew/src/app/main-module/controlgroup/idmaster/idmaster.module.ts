import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IdmasterRoutingModule } from './idmaster-routing.module';
import { LanguageComponent } from './language/language.component';
import { LanguageNewComponent } from './language/language-new/language-new.component';
import { CompanyComponent } from './company/company.component';
import { CompanyNewComponent } from './company/company-new/company-new.component';
import { IdmasterlistComponent } from './idmasterlist/idmasterlist.component';
import { SharedModule } from "src/app/shared/shared.module";
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { CountryComponent } from './country/country.component';
import { CountryNewComponent } from './country/country-new/country-new.component';
import { StateComponent } from './state/state.component';
import { StateNewComponent } from './state/state-new/state-new.component';
import { CityComponent } from './city/city.component';
import { CityNewComponent } from './city/city-new/city-new.component';
import { StoreComponent } from './store/store.component';
import { StoreNewComponent } from './store/store-new/store-new.component';
import { ControlgrouptypeComponent } from './controlgrouptype/controlgrouptype.component';
import { ControlgrouptypeNewComponent } from './controlgrouptype/controlgrouptype-new/controlgrouptype-new.component';
import { SubgroupComponent } from './subgroup/subgroup.component';
import { SubgroupNewComponent } from './subgroup/subgroup-new/subgroup-new.component';
import { RelationshipComponent } from './relationship/relationship.component';
import { RelationshipNewComponent } from './relationship/relationship-new/relationship-new.component';
import { StatusComponent } from './status/status.component';
import { StatusNewComponent } from './status/status-new/status-new.component';
import { NumberrangecodeComponent } from './numberrangecode/numberrangecode.component';
import { NumberrangecodeNewComponent } from './numberrangecode/numberrangecode-new/numberrangecode-new.component';
import { EntityComponent } from './entity/entity.component';
import { EntityNewComponent } from './entity/entity-new/entity-new.component';


@NgModule({
  declarations: [
    LanguageComponent,
    LanguageNewComponent,
    CompanyComponent,
    CompanyNewComponent,
    IdmasterlistComponent,
    CountryComponent,
    CountryNewComponent,
    StateComponent,
    StateNewComponent,
    CityComponent,
    CityNewComponent,
    StoreComponent,
    StoreNewComponent,
    ControlgrouptypeComponent,
    ControlgrouptypeNewComponent,
    SubgroupComponent,
    SubgroupNewComponent,
    RelationshipComponent,
    RelationshipNewComponent,
    StatusComponent,
    StatusNewComponent,
    NumberrangecodeComponent,
    NumberrangecodeNewComponent,
    EntityComponent,
    EntityNewComponent
  ],
  imports: [
    CommonModule,
    IdmasterRoutingModule,
    SharedModule, CommonFieldModule, AngularMultiSelectModule,
  ]
})
export class IdmasterModule { }
