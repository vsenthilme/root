import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DocumentsRoutingModule } from './documents-routing.module';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { DocumentsComponent } from './documents/documents.component';
import { DocumentsUploadComponent } from './documents-upload/documents-upload.component';
import { TabComponent } from './tab/tab.component';
import { DocumentsfromMRComponent } from './documentsfrom-mr/documentsfrom-mr.component';
import { DocumentuploadComponent } from './documentupload/documentupload.component';
import { ngfModule } from 'angular-file';
import { UploadmatterComponent } from './documentsfrom-mr/uploadmatter/uploadmatter.component';
import { DocumentViewComponent } from './documentsfrom-mr/document-view/document-view.component';
import { DocumentComponent } from './documentsfrom-mr/document/document.component';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import { DownloadConfirmComponent } from './download-confirm/download-confirm.component';

@NgModule({
  declarations: [
    DocumentsComponent,
    DocumentsUploadComponent,
    TabComponent,
    DocumentsfromMRComponent,
    DocumentuploadComponent,
    UploadmatterComponent,
    DocumentViewComponent,
    DocumentComponent,
    DownloadConfirmComponent,
  ],
  imports: [
    CommonModule,
    DocumentsRoutingModule,
    SharedModule,
    CommonFieldModule,
    ngfModule,
    PdfViewerModule, ngfModule,
  ]
})
export class DocumentsModule { }
