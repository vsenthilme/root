import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DocumentsUploadComponent } from './documents-upload/documents-upload.component';
import { DocumentsComponent } from './documents/documents.component';
import { DocumentViewComponent } from './documentsfrom-mr/document-view/document-view.component';
import { DocumentComponent } from './documentsfrom-mr/document/document.component';
import { DocumentsfromMRComponent } from './documentsfrom-mr/documentsfrom-mr.component';
import { DocumentuploadComponent } from './documentupload/documentupload.component';
import { TabComponent } from './tab/tab.component';

const routes: Routes = [

{ path: 'document-upload/:matterNumber/:clientId/:checkListNo/:matterHeaderId', component: DocumentsUploadComponent,  },
{ path: 'documents', component: DocumentsComponent,  },
{ path: 'documentupload', component: DocumentuploadComponent,  },
{ path: 'document-MR', component: DocumentsfromMRComponent, },
{ path: 'document-view', component: DocumentViewComponent, },
{ path: 'documentpdf', component: DocumentComponent, },
{ path: 'documentpdf/:code', component: DocumentComponent, },
{ path: 'document-view/:code', component: DocumentViewComponent,  pathMatch: 'full', }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DocumentsRoutingModule { }
