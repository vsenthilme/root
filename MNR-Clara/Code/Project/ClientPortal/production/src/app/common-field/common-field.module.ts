import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { NgMultiSelectDropDownModule } from "ng-multiselect-dropdown";
import { SharedModule } from "../shared/shared.module";
import { DialogExampleComponent } from "./dialog-example/dialog-example.component";
import { CancelComponent } from "./dialog_modules/cancel/cancel.component";
import { DeleteComponent } from "./dialog_modules/delete/delete.component";
import { UploadComponent } from "./dialog_modules/upload/upload.component";
import { NgMultiselectDropdownComponent } from "./ng-multiselect-dropdown/ng-multiselect-dropdown.component";
import { NotesComponent } from "./notes/notes.component";
import { ScrollButtonComponent } from "./scroll-button/scroll-button.component";



@NgModule({
  declarations: [
    DialogExampleComponent, NotesComponent, CancelComponent, DeleteComponent, ScrollButtonComponent, NgMultiselectDropdownComponent
    , NgMultiselectDropdownComponent, UploadComponent],
  imports: [
    CommonModule, SharedModule, RouterModule, NgMultiSelectDropDownModule.forRoot()
  ],
  exports: [
    DialogExampleComponent, NotesComponent, CancelComponent, DeleteComponent, ScrollButtonComponent
    , NgMultiselectDropdownComponent]
})
export class CommonFieldModule { }
