import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { SharedModule } from "../shared/shared.module";
import { CustomerformRoutingModule } from "./customerform-routing.module";
import { DacaFormComponent } from "./daca-form/daca-form.component";
import { EnglishN400Component } from "./english-n400/english-n400.component";
import { EnglishComponent } from "./english/english.component";
import { ImmigrationFormComponent } from "./immigration-form/immigration-form.component";
import { LAndEComponent } from "./l-and-e/l-and-e.component";
import { SpanishComponent } from "./spanish/spanish.component";
import { CustomerThanksComponent } from './customer-thanks/customer-thanks.component';
import { CustomerformReceivedComponent } from './customerform-received/customerform-received.component';
import { CommonFieldModule } from "../common-field/common-field.module";
import { Form007Component } from './form007/form007.component';
import { Itform008SpanishComponent } from './itform008-spanish/itform008-spanish.component';
import { Form011EnglishComponent } from './form011-english/form011-english.component';
import { Form009SpanishComponent } from './form009-spanish/form009-spanish.component';
import { Form010EnglishComponent } from './form010-english/form010-english.component';
import { ImmigirationFrom007Component } from './form007/immigiration-from007/immigiration-from007.component';
import { HistoryResidenceForm007Component } from './form007/history-residence-form007/history-residence-form007.component';
import { EmploymentHistoryForm007Component } from './form007/employment-history-form007/employment-history-form007.component';
import { AdditionalInformationForm007Component } from './form007/additional-information-form007/additional-information-form007.component';
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { TextMaskModule } from 'angular2-text-mask';
import { EnglishFeedbackComponent } from './feedback/english-feedback/english-feedback.component';
import { SpanishFeedbackComponent } from './feedback/spanish-feedback/spanish-feedback.component';
import { FeedbackThanksComponent } from './feedback-thanks/feedback-thanks.component';                             //10/02/23

@NgModule({
  declarations: [
    ImmigrationFormComponent,
    SpanishComponent,
    EnglishComponent,
    EnglishN400Component,
    DacaFormComponent,
    LAndEComponent,
    CustomerThanksComponent,
    CustomerformReceivedComponent,
    Form007Component,
    Itform008SpanishComponent,
    Form011EnglishComponent,
    Form009SpanishComponent,
    Form010EnglishComponent,

    ImmigirationFrom007Component,
      HistoryResidenceForm007Component,
      EmploymentHistoryForm007Component,
      AdditionalInformationForm007Component,
      EnglishFeedbackComponent,
      SpanishFeedbackComponent,
      FeedbackThanksComponent,

  ],
  imports: [
    CommonModule,
    CustomerformRoutingModule,
    SharedModule, CommonFieldModule, AngularMultiSelectModule,TextMaskModule                                     //10/02/23
  ],
  exports: [

    Form011EnglishComponent
  ]
})
export class CustomerformModule { }
