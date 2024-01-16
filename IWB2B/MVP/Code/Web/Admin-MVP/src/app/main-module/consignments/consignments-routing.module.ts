import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { ConsignmentHistoryComponent } from "./consignment-history/consignment-history.component";
import { TrackHistoryComponent } from "./consignment-history/track-history/track-history.component";
import { ConsignmentNewPopupComponent } from "./consignment-new/consignment-new-popup/consignment-new-popup.component";
import { ConsignmentNewComponent } from "./consignment-new/consignment-new.component";


const routes: Routes = [
  { path: 'consignmentNew', component: ConsignmentNewComponent, },
  { path: 'consignmentCreate', component: ConsignmentNewPopupComponent, },
  { path: 'consignmentHistory', component: ConsignmentHistoryComponent, },
  { path: 'tracking', component: TrackHistoryComponent, },

  {
    path: 'consignmentCreate/:code',
    component: ConsignmentNewPopupComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ConsignmentsRoutingModule { }
