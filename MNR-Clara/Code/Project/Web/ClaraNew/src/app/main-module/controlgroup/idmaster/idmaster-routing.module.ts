import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LanguageComponent } from './language/language.component';
import { CompanyComponent } from './company/company.component';
import { IdmasterlistComponent } from './idmasterlist/idmasterlist.component';
import { CountryComponent } from './country/country.component';
import { StateComponent } from './state/state.component';
import { CityComponent } from './city/city.component';
import { StoreComponent } from './store/store.component';
import { ControlgrouptypeComponent } from './controlgrouptype/controlgrouptype.component';
import { SubgroupComponent } from './subgroup/subgroup.component';
import { RelationshipComponent } from './relationship/relationship.component';
import { StatusComponent } from './status/status.component';
import { NumberrangecodeComponent } from './numberrangecode/numberrangecode.component';
import { EntityComponent } from './entity/entity.component';

const routes: Routes = [{
  path: '',
  component: IdmasterlistComponent,
  children:[
  // { path: 'language', component: LanguageComponent, pathMatch: 'full', data: { title: 'Language', module: 'Language' } },
  // { path: 'company', component: CompanyComponent, pathMatch: 'full', data: { title: 'Company', module: 'Company' } },
  { path: 'country', component: CountryComponent, pathMatch: 'full', data: { title: 'Country', module: 'Country' } },
  { path: 'state', component: StateComponent, pathMatch: 'full', data: { title: 'State', module: 'State' } },
  { path: 'city', component: CityComponent, pathMatch: 'full', data: { title: 'City', module: 'City' } },
  { path: 'status', component: StatusComponent, pathMatch: 'full', data: { title: 'Status', module: 'Status' } },
  { path: 'store', component: StoreComponent, pathMatch: 'full', data: { title: 'Store', module: 'Store' } },
  { path: 'controlgrouptype', component: ControlgrouptypeComponent, pathMatch: 'full', data: { title: 'Controlled Groups Type', module: 'Controlled Groups Type' } },
 // { path: 'subgroup', component: SubgroupComponent, pathMatch: 'full', data: { title: 'Sub Group', module: 'Sub Group' } },
  { path: 'relationship', component: RelationshipComponent, pathMatch: 'full', data: { title: 'Relationship', module: 'Relationship' } },
  { path: 'numberrange', component: NumberrangecodeComponent, pathMatch: 'full', data: { title: 'Number Range Code', module: 'Number Range Code' } },
  { path: 'entity', component: EntityComponent, pathMatch: 'full', data: { title: 'Entity', module: 'Entity' } },
]} 
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class IdmasterRoutingModule { }
