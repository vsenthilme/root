import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ClientDocumentService {

  constructor(private http: HttpClient) { }
  apiNamesetup = '/mnr-setup-service/';
  // apiName = '/wms-crm-service/';
  apiName = '/mnr-management-service/';
  methodeName = 'clientdocument';
  methodeName1 = 'clientgeneral'

  url = this.apiName + this.methodeName;
  url1 = this.apiName + this.methodeName1;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + `/` + code);
  }

  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.url + `/` + code, obj);
  }

  Delete(code: any) {
    return this.http.delete(this.url + `/` + code, { responseType: 'text' });
  }



  Get_documentTemplate(code: string) {
    return this.http.get<any>(this.apiNamesetup + `documentTemplate/` + code);
  }


  get_matterdetails(code: string) {
    return this.http.get<any>(this.apiName + `mattergenacc/` + code);
  }

  getfile(code: any, url: any): Promise<File> {
    return this.http
      .get<any>('/doc-storage/download?fileName=' + url + '&location=' + code, {
        responseType: 'blob' as 'json',
      })
      .toPromise();
  }

  getfile1(code: any, url: any, classId: any): Promise<File> {
    return this.http
      .get<any>('/doc-storage/download?fileName=' + url + '&location=' + classId + '&classId=' + code, {
        responseType: 'blob' as 'json',
      })
      .toPromise();
  }


  send_document(classId: any, clientId: any, documentNumber: any) {
    return this.http.post<any>(this.url + '/docusign?classId=' + classId + '&clientId=' + clientId + '&documentNumber=' + documentNumber
      , {});
  }


  Search(obj: any) {
    return this.http.post<any>(this.url + '/findClientDocument', obj);
}


  // docusignstatus(code: string) {
  //   return this.http.get<any>(this.apiName + 'clientdocument/' + code + 'agreement/docusign/envelope/status');
  // }
  docuSignDownload(clientId: string, classId: any, documentNumber: string) {

    return this.http.get(this.apiName + 'clientdocument/' + clientId + '/docusign/download?classId=' + classId + '&documentNumber=' + documentNumber, { responseType: 'text' });


  }
  GetClient(code: string) {
    return this.http.get<any>(this.url1 + `/` + code);
  }

  getAllClientList() {
    return this.http.get<any>(this.apiNamesetup + 'clientUser');
  }

  sendMailClient(obj) {
    return this.http.post<any>(this.apiNamesetup + 'clientUser/sendToClient', obj);
  }

  searchClientList(obj: any) {
    return this.http.post<any>(this.apiNamesetup + 'clientUser/findClientUser', obj);
  }

  patchClientUser(obj: any, code: any) {
    return this.http.patch<any>(this.apiNamesetup + `clientUser/` + code, obj);
  }

}
