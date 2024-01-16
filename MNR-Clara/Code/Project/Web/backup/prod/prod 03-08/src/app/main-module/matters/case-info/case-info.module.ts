import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { SharedModule } from "src/app/shared/shared.module";
import { CaseInfoRoutingModule } from "./case-info-routing.module";
import { ImmigrationListComponent } from "./Immigiration/immigration-list/immigration-list.component";
import { ImmigrationNewComponent } from "./Immigiration/immigration-new/immigration-new.component";
import { LandEListComponent } from "./L&E/land-e-list/land-e-list.component";
import { LandENewComponent } from "./L&E/land-e-new/land-e-new.component";

@NgModule({
  declarations: [
    LandEListComponent,
    LandENewComponent,
    ImmigrationListComponent,
    ImmigrationNewComponent,

  ],
  imports: [
    CommonModule,
    CaseInfoRoutingModule,
    SharedModule, CommonFieldModule,AngularMultiSelectModule,
  ],
  exports: [

  ]
})
export class CaseInfoModule { }
