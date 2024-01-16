import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { ConfigTabbarComponent } from "./config-tabbar/config-tabbar.component";
import { LanguageIdComponent } from "./language-id/language-id.component";
import { MessageIdComponent } from "./message-id/message-id.component";
import { NumberRangeComponent } from "./number-range/number-range.component";
import { ScreenIdComponent } from "./screen-id/screen-id.component";
import { StatusIdComponent } from "./status-id/status-id.component";
import { TransactionIdComponent } from "./transaction-id/transaction-id.component";

const routes: Routes = [{
  path: '',
  component: ConfigTabbarComponent,
  children: [
    // { path: 'bar', component: ConfigTabbarComponent, pathMatch: 'full', data: { title: 'Bar' , module: 'Configuration' } },
    { path: 'language', component: LanguageIdComponent, pathMatch: 'full', data: { title: 'Language', module: 'Configuration' } },
    { path: 'screen', component: ScreenIdComponent, pathMatch: 'full', data: { title: 'Screen', module: 'Configuration' } },
    { path: 'status', component: StatusIdComponent, pathMatch: 'full', data: { title: 'Status', module: 'Configuration' } },
    { path: 'message', component: MessageIdComponent, pathMatch: 'full', data: { title: 'Message', module: 'Configuration' } },
    { path: 'transaction', component: TransactionIdComponent, pathMatch: 'full', data: { title: 'Transaction', module: 'Configuration' } },
    { path: 'numberrange', component: NumberRangeComponent, pathMatch: 'full', data: { title: 'Number Range', module: 'Configuration' } },
  ]
}
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ConfigurationRoutingModule { }
