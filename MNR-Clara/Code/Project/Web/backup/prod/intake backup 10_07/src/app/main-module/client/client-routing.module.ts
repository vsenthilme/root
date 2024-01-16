import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientDocumentComponent } from './client-document/client-document.component';
import { ClientportalListComponent } from './client-document/client-portal/clientportal-list/clientportal-list.component';
import { DocumentSendComponent } from './client-document/document-send/document-send.component';
import { ClientEmailComponent } from './client-email/client-email.component';
import { ClientListComponent } from './client-general/client-list/client-list.component';
import { ClientNewComponent } from './client-general/client-new/client-new.component';
import { ClientMattersComponent } from './client-matters/client-matters.component';
import { NotesListComponent } from './client-notes/notes-list/notes-list.component';
import { ClientTabbarComponent } from './client-tabbar/client-tabbar.component';



const routes: Routes = [
  { path: 'clientlist', component: ClientListComponent, pathMatch: 'full', data: { title: 'Client List', module: 'Client' } },
  { path: 'clientNew/:code', component: ClientNewComponent, pathMatch: 'full', data: { title: 'Client New', module: 'Client' } },
  { path: 'document/:code', component: DocumentSendComponent, pathMatch: 'full', data: { title: 'Document', module: 'Client' } },
  { path: 'clientportal', component: ClientportalListComponent, pathMatch: 'full', data: { title: 'Document Template', module: 'Client' } },
  {
    path: '',
    component: ClientTabbarComponent,
    children: [
      { path: 'matters/:code', component: ClientMattersComponent, pathMatch: 'full', data: { title: 'Matters', module: 'Matter' } },

      { path: 'clientupdate/:code', component: ClientNewComponent, pathMatch: 'full', data: { title: 'Client ', module: 'Client' } },
      { path: 'notes/:code', component: NotesListComponent, pathMatch: 'full', data: { title: 'Notes', module: 'Client' } },
      { path: 'email/:code', component: ClientEmailComponent, pathMatch: 'full', data: { title: 'Email', module: 'Client' } },
      { path: 'documents/:code', component: ClientDocumentComponent, pathMatch: 'full', data: { title: 'Document Template', module: 'Client' } },

     
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientRoutingModule { }
