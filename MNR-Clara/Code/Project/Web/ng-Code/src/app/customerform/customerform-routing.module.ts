import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { CustomerThanksComponent } from "./customer-thanks/customer-thanks.component";
import { CustomerformReceivedComponent } from "./customerform-received/customerform-received.component";
import { DacaFormComponent } from "./daca-form/daca-form.component";
import { EnglishN400Component } from "./english-n400/english-n400.component";
import { EnglishComponent } from "./english/english.component";
import { Form007Component } from "./form007/form007.component";
import { Form009SpanishComponent } from "./form009-spanish/form009-spanish.component";
import { Form010EnglishComponent } from "./form010-english/form010-english.component";
import { Form011EnglishComponent } from "./form011-english/form011-english.component";
import { ImmigrationFormComponent } from "./immigration-form/immigration-form.component";
import { Itform008SpanishComponent } from "./itform008-spanish/itform008-spanish.component";
import { LAndEComponent } from "./l-and-e/l-and-e.component";
import { SpanishComponent } from "./spanish/spanish.component";
import { EnglishFeedbackComponent } from "./feedback/english-feedback/english-feedback.component";
import { SpanishFeedbackComponent } from "./feedback/spanish-feedback/spanish-feedback.component";
import { FeedbackThanksComponent } from "./feedback-thanks/feedback-thanks.component";


const routes: Routes = [
  { path: 'immigrationform/:code', component: ImmigrationFormComponent },
  { path: 'spanishform/:code', component: SpanishComponent },
  { path: 'englishform/:code', component: EnglishComponent },
  { path: 'english400form/:code', component: EnglishN400Component },
  { path: 'dacaform/:code', component: DacaFormComponent },
  { path: 'landeform/:code', component: LAndEComponent },
  { path: 'thanks', component: CustomerThanksComponent },
  { path: 'received', component: CustomerformReceivedComponent },



  { path: 'form007/:code', component: Form007Component },
  { path: 'form008/:code', component: Itform008SpanishComponent },
  { path: 'form009/:code', component: Form009SpanishComponent },
  { path: 'form010/:code', component: Form010EnglishComponent },
  { path: 'form011/:code', component: Form011EnglishComponent },

  { path: 'feedback/:code', component: EnglishFeedbackComponent},
  { path: 'spnfeedback/:code', component: SpanishFeedbackComponent},
  { path: 'feedbackThanks', component: FeedbackThanksComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomerformRoutingModule { }
