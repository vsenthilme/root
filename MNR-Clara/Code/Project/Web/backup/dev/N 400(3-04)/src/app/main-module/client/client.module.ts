import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClientRoutingModule } from './client-routing.module';
import { ClientTabbarComponent } from './client-tabbar/client-tabbar.component';
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { SharedModule } from "src/app/shared/shared.module";
import { ClientTabComponent } from './client-tab/client-tab.component';
import { ClientListComponent } from './client-general/client-list/client-list.component';
import { ClientNewComponent } from './client-general/client-new/client-new.component';
import { NotesListComponent } from './client-notes/notes-list/notes-list.component';
import { NotesNewComponent } from './client-notes/notes-list/notes-new/notes-new.component';
import { ClientMattersComponent } from './client-matters/client-matters.component';
import { ClientEmailComponent } from './client-email/client-email.component';
import { EmailNewComponent } from './client-email/email-new/email-new.component';
import { ClientDocumentComponent } from './client-document/client-document.component';
import { DocumentNewComponent } from './client-document/document-new/document-new.component';
import { DocumentSendComponent } from './client-document/document-send/document-send.component';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import { NgxEditorModule } from 'ngx-editor';
import { ngfModule } from 'angular-file';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { ClientportalListComponent } from './client-document/client-portal/clientportal-list/clientportal-list.component';
import { ClientportalDisplayComponent } from './client-document/client-portal/clientportal-list/clientportal-display/clientportal-display.component';
import { SendIntakeComponent } from './client-document/send-intake-popup/send-intake.component';
import { TextMaskModule } from 'angular2-text-mask';  
@NgModule({
  declarations: [
    ClientTabbarComponent,
    ClientTabComponent,
    ClientListComponent,
    ClientNewComponent,
    NotesListComponent,
    NotesNewComponent,
    ClientMattersComponent,
    ClientEmailComponent,
    EmailNewComponent,
    ClientDocumentComponent,
    DocumentNewComponent,
    DocumentSendComponent,
    ClientportalListComponent,
    ClientportalDisplayComponent,
    SendIntakeComponent
  ],
  imports: [
    CommonModule, SharedModule, CommonFieldModule, NgxEditorModule, PdfViewerModule, ngfModule,
    ClientRoutingModule, AngularMultiSelectModule,TextMaskModule

  ],
  exports: [
    ClientTabbarComponent,
    ClientTabComponent,
    ClientListComponent,
    ClientNewComponent,
    NotesListComponent,
    NotesNewComponent,
    ClientMattersComponent,
    ClientEmailComponent,
    EmailNewComponent,
    ClientDocumentComponent,
    DocumentNewComponent,
    DocumentSendComponent,
    SendIntakeComponent
  ]
})
export class ClientModule { }
