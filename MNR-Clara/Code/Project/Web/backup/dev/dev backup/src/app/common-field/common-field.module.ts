import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { NgMultiSelectDropDownModule } from "ng-multiselect-dropdown";
import { SharedModule } from "../shared/shared.module";
import { DialogExampleComponent } from "./dialog-example/dialog-example.component";
import { AddStringArrayPopupComponent } from "./dialog_modules/add-string-array-popup/add-string-array-popup.component";
import { CancelComponent } from "./dialog_modules/cancel/cancel.component";
import { DeleteComponent } from "./dialog_modules/delete/delete.component";
import { ShowStringPopupComponent } from "./dialog_modules/show-string-popup/show-string-popup.component";
import { NgMultiselectDropdownComponent } from "./ng-multiselect-dropdown/ng-multiselect-dropdown.component";
import { NotesComponent } from "./notes/notes.component";
import { ScrollButtonComponent } from "./scroll-button/scroll-button.component";
import { TimerComponent } from './timer/timer.component';
import { LogoutPopupComponent } from './dialog_modules/logout-popup/logout-popup.component';


@NgModule({

  imports: [
    CommonModule, SharedModule, NgMultiSelectDropDownModule.forRoot()
  ],
  declarations: [
    DialogExampleComponent, NotesComponent, CancelComponent, DeleteComponent, ScrollButtonComponent, NgMultiselectDropdownComponent,AddStringArrayPopupComponent,ShowStringPopupComponent, TimerComponent, LogoutPopupComponent
  ],
  exports: [
    DialogExampleComponent, NotesComponent, CancelComponent, DeleteComponent, ScrollButtonComponent
    , NgMultiselectDropdownComponent,AddStringArrayPopupComponent,ShowStringPopupComponent,TimerComponent]
})
export class CommonFieldModule { }
