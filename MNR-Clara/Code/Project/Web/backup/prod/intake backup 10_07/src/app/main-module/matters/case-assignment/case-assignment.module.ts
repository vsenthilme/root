import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { SharedModule } from "src/app/shared/shared.module";
import { CaseAssignmentRoutingModule } from "./case-assignment-routing.module";
import { CaseassignmentListComponent } from "./caseassignment-list/caseassignment-list.component";
import { ResouceAssignmentComponent } from "./resouce-assignment/resouce-assignment.component";

@NgModule({
  declarations: [CaseassignmentListComponent,
    ResouceAssignmentComponent],
  imports: [
    CommonModule, SharedModule, CommonFieldModule,
    CaseAssignmentRoutingModule,AngularMultiSelectModule

  ]
})
export class CaseAssignmentModule { }
