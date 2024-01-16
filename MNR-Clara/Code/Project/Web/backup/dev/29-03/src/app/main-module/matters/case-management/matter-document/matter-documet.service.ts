import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MatterDocumetService {

  constructor(private http: HttpClient) { }
  apiNamesetup = '/mnr-setup-service/';
  // apiName = '/wms-crm-service/';
  apiName = '/mnr-management-service/';
  methodeName = 'matterdocument';
  url = this.apiName + this.methodeName;
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
      .get<any>('/doc-storage/download?fileName=' + url + '&location=' + classId  + '&classId=' + code, {
        responseType: 'blob' as 'json',
      })
      .toPromise();
  }



  send_document(classId: any, documentNumber: any, matterNumber: any) {
    return this.http.post<any>(this.url + '/docusign?classId=' + classId + '&documentNumber=' + documentNumber + '&matterNumber=' + matterNumber
      , {});
  }


  Search(obj: any) {
    return this.http.post<any>(this.url + '/findMatterDocument', obj);

  }

  // docusignstatus(code: string) {
  //   return this.http.get<any>(this.apiName + 'agreement/docusign/envelope/status?potentialClientId=' + code);
  // }

  docuSignDownload(matterNumber: string, classId: any, documentNumber: string) {

    return this.http.get(this.apiName + 'matterdocument/' + matterNumber + '/docusign/download?classId=' + classId + '&documentNumber=' + documentNumber, { responseType: 'text' });
  }

  createClientPortalDocs(obj: any) {
    return this.http.post<any>(this.url + '/clientPortal', obj);
  }

  createClinetPortalmatterDocument(obj: any[]) {
    return this.http.post<any>(`/mnr-management-service/matterdocument/clientPortal/docsUpload`, obj);
  }
}
