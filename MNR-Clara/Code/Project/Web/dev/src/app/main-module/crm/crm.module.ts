import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ngfModule } from "angular-file";
import { PdfViewerModule } from "ng2-pdf-viewer";
import { NgxEditorModule } from "ngx-editor";
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { CustomerformModule } from "src/app/customerform/customerform.module";
import { SharedModule } from "src/app/shared/shared.module";
import { AgreementSnapComponent } from "./agreement-snap/agreement-snap.component";
import { ConflickCheckMainComponent } from "./conflict-check/conflick-check-main/conflick-check-main.component";
import { ConflictListComponent } from "./conflict-check/conflict-list/conflict-list.component";
import { CrmRoutingModule } from "./crm-routing.module";
import { InquirySearchComponent } from "./inquiries/inquiry-search/inquiry-search.component";
import { InquiryUpdate3Component } from "./inquiries/inquiry-update3/inquiry-update3.component";
import { IntakePopupComponent } from "./inquiries/intake-popup/intake-popup.component";
import { SendIntakeComponent } from "./inquiries/send-intake/send-intake.component";
import { IntakeSnapMainComponent } from "./intake-snap-main/intake-snap-main.component";
import { AgreementDocumentComponent } from "./potential/agreement-document/agreement-document.component";
import { AgreementPopupNewComponent } from "./potential/agreement-popup-new/agreement-popup-new.component";
import { DisplayComponent } from "./potential/display/display.component";
import { PotentialSnalComponent } from "./potential/potential-snal/potential-snal.component";
import { TextMaskModule } from 'angular2-text-mask';
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { MatInputModule } from "@angular/material/input";
import { DetailspopupComponent } from './feedback-pdf/detailspopup/detailspopup.component';
import { EnglishFeedbackpdfComponent } from './feedback-pdf/english-feedbackpdf/english-feedbackpdf.component';
import { SpanishFeedbackpdfComponent } from './feedback-pdf/spanish-feedbackpdf/spanish-feedbackpdf.component';
import { EnglishintakeComponent } from './intake-pdf/englishintake/englishintake.component';
import { N400intakeComponent } from './intake-pdf/n400intake/n400intake.component';
@NgModule({
  declarations: [
    InquirySearchComponent,

    InquiryUpdate3Component,
    IntakePopupComponent,
    SendIntakeComponent,
    IntakeSnapMainComponent,
    PotentialSnalComponent,
    DisplayComponent,
    AgreementPopupNewComponent,
    AgreementDocumentComponent,
    AgreementSnapComponent,
    ConflictListComponent,
    ConflickCheckMainComponent,
    DetailspopupComponent,
    EnglishFeedbackpdfComponent,
    SpanishFeedbackpdfComponent,
    EnglishintakeComponent,
    N400intakeComponent


  ],
  imports: [
    CommonModule, SharedModule, CommonFieldModule,
    CrmRoutingModule,
    CustomerformModule, NgxEditorModule, ngfModule, PdfViewerModule,TextMaskModule,  AngularMultiSelectModule,MatInputModule
  ],
  exports: [ConflickCheckMainComponent, InquiryUpdate3Component]
})
export class CrmModule { }