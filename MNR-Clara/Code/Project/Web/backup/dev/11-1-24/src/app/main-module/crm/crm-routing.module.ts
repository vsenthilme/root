import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { DacaFormComponent } from "src/app/customerform/daca-form/daca-form.component";
import { EnglishN400Component } from "src/app/customerform/english-n400/english-n400.component";
import { EnglishComponent } from "src/app/customerform/english/english.component";
import { ImmigrationFormComponent } from "src/app/customerform/immigration-form/immigration-form.component";
import { LAndEComponent } from "src/app/customerform/l-and-e/l-and-e.component";
import { SpanishComponent } from "src/app/customerform/spanish/spanish.component";
import { AgreementSnapComponent } from "./agreement-snap/agreement-snap.component";
import { ConflickCheckMainComponent } from "./conflict-check/conflick-check-main/conflick-check-main.component";
import { ConflictListComponent } from "./conflict-check/conflict-list/conflict-list.component";
import { InquirySearchComponent } from "./inquiries/inquiry-search/inquiry-search.component";
import { IntakeSnapMainComponent } from "./intake-snap-main/intake-snap-main.component";
import { AgreementDocumentComponent } from "./potential/agreement-document/agreement-document.component";
import { PotentialSnalComponent } from "./potential/potential-snal/potential-snal.component";
import { Spanish003Component } from "./intake-pdf/spanish003/spanish003.component";


const crmroutes: Routes = [
  { path: 'inquirynew', component: InquirySearchComponent, pathMatch: 'full', data: { title: 'Inquiry New', module: 'CRM' } },
  { path: 'inquiryassign', component: InquirySearchComponent, pathMatch: 'full', data: { title: 'Inquiry Assign', module: 'CRM' } },
  { path: 'inquiryvalidate', component: InquirySearchComponent, pathMatch: 'full', data: { title: 'Inquiry Validation', module: 'CRM' } },
  { path: 'inquiryform', component: IntakeSnapMainComponent, pathMatch: 'full', data: { title: 'Intake Form', module: 'CRM' } },
  { path: 'potential', component: PotentialSnalComponent, pathMatch: 'full', data: { title: 'Prospective Client', module: 'CRM' } },
  { path: 'agreementdocument/:code', component: AgreementDocumentComponent, pathMatch: 'full', data: { title: 'Agreement Document', module: 'CRM' } },
  { path: 'agreementlist', component: AgreementSnapComponent, pathMatch: 'full', data: { title: 'Agreement List', module: 'CRM' } },
  { path: 'conflictcheck', component: ConflickCheckMainComponent, pathMatch: 'full', data: { title: 'Conflict Check', module: 'CRM' } },
  { path: 'conflictlist/:code', component: ConflictListComponent, pathMatch: 'full', data: { title: 'Conflict List', module: 'CRM' } },



  { path: 'immigrationform/:code', component: ImmigrationFormComponent, data: { title: 'Immigration' } },
  { path: 'spanishform/:code', component: SpanishComponent, data: { title: 'Spanish' } },
  { path: 'englishform/:code', component: EnglishComponent, data: { title: 'English' } },
  { path: 'english400form/:code', component: EnglishN400Component, data: { title: 'EnglishN400' } },
  { path: 'english400form', component: EnglishN400Component, data: { title: 'EnglishN400' } },
  { path: 'dacaform/:code', component: DacaFormComponent, data: { title: 'DACA' } },
  { path: 'landeform/:code', component: LAndEComponent, data: { title: 'L & E' } },


  
  { path: 'pdfForm', component: Spanish003Component, data: { title: 'L & E' } },
  
];

@NgModule({
  imports: [RouterModule.forChild(crmroutes)],
  exports: [RouterModule]
})
export class CrmRoutingModule { }