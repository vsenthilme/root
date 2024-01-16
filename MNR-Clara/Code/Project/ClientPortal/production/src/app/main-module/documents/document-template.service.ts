
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';
export interface documentElement {
  classIddes: string;
  caseCategoryIddes: string;
  caseCategoryId: number;
  classId: number;
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  documentFileDescription: string;
  documentNumber: string;
  documentStatus: string;
  documentUrl: string;
  languageId: string;
  mailMerge: boolean;
  referenceField1: string;
  referenceField10: string;
  referenceField2: string;
  referenceField3: string;
  referenceField4: string;
  referenceField5: string;
  referenceField6: string;
  referenceField7: string;
  referenceField8: string;
  referenceField9: string;
  updatedBy: string;
  updatedOn: Date;
}
@Injectable({
  providedIn: 'root'
})
export class DocumentTemplateService {


  constructor(private http: HttpClient, private auth: AuthService) { }

  setUpServiceapiName = '/mnr-setup-service/';
  managementServiceApiName = '/mnr-management-service/';

  method = 'mattergenacc'
  method1 = 'matterdocument'
  method2 = 'documentTemplate'
  url = this.managementServiceApiName + this.method;
  url1 = this.managementServiceApiName + this.method1;
  url2 = this.setUpServiceapiName + this.method2;

  createChecklistDocument(obj: any[]) {
    return this.http.post<any>(this.setUpServiceapiName + `docchecklist`, obj);
  }
  getChecklistDocument() {
    return this.http.get<any>(this.setUpServiceapiName + `docchecklist`);
  }
  filterChecklistDocument(obj: {}) {
    return this.http.post<any>(this.setUpServiceapiName + `docchecklist/findDocCheckList`, obj);
  }
  getAllMatterDocList() {
    return this.http.get<any>(this.managementServiceApiName + 'matterdoclist');
  }

  getMatterDetails(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }

  downloadChecklistFile(location: any, url: any): Promise<File> {
    return this.http
      .get<any>('/doc-storage/download?fileName=' + url + '&location=' + location, {
        responseType: 'blob' as 'json',
      })
      .toPromise();
  }

  createClientPortalChecklistDocument(obj: any) {
    return this.http.get<any>(this.managementServiceApiName + `matterdoclist/` + obj.matterNumber + `/clientPortal/docCheckList?checkListNo=${obj.checkListNo}&clientId=${obj.clientId}&matterHeaderId=${obj.matterHeaderId}`);
  }


  getMatterDocument() {
    return this.http.get<any>(this.url1);
  }


  GetallMatter() {
    return this.http.get<any>(this.url);
  }

  GetallDocName() {
    return this.http.get<any>(this.url2);
  }


  Getstatus(code: string) {
    return this.http.get<any>('//mnr-setup-service/status');
  }

  createClinetPortalDocument(obj: any[]) {
    return this.http.post<any>(this.setUpServiceapiName + `docchecklist`, obj);
  }

  createClinetPortalmatterDocument(obj: any[]) {
    return this.http.post<any>(this.url1 + `/clientPortal/docsUpload`, obj);
  }

  GetmatterDocument(code: string) {
    return this.http.get<any>(this.url1 + `/` + code);
  }



  Get_documentTemplate(code: string) {
    return this.http.get<any>(this.setUpServiceapiName + `documentTemplate/` + code);
  }


  getfile1(code: any, url: any, classId: any): Promise<File> {
    return this.http
      .get<any>('/doc-storage/download?fileName=' + url + '&location=' + classId + '&classId=' + code, {
        responseType: 'blob' as 'json',
      })
      .toPromise();
  }

  // getChecklistno(obj: string){
  //   return this.http.get<any>('/mnr-setup-service/docchecklist/findDocCheckList' + code);
  // }

  Searchchecklistno(obj: any) {
    return this.http.post<any>('/mnr-setup-service/docchecklist/findDocCheckList', obj);
  }


  getfile(code: any, url: any): Promise<File> {
    return this.http
      .get<any>('/doc-storage/download?fileName=' + url + '&location=' + code, {
        responseType: 'blob' as 'json',
      })
      .toPromise();
  }
  searchMatterDocList(obj: any) {
    return this.http.post<any>(this.managementServiceApiName + 'matterdoclist/findMatterDocList', obj);
  }

  getClientCheckListDocument(fileName: any, location: any): Promise<File> {
    return this.http
      .get<any>(`/doc-storage/download?fileName=${fileName}&location=${location}`, {
        responseType: 'blob' as 'json',
      })
      .toPromise();
  }

  searchMatterDocListHeader(obj: any) {
    return this.http.post<any>(this.managementServiceApiName + 'matterdoclistheader/find', obj);
  }

  findMatterDocument(obj: any) {
    return this.http.post<any>(this.managementServiceApiName + 'matterdocument/findMatterDocument', obj);
  }

  // updateMatterDocList(obj: any) {
  //   return this.http.patch<any>(`/mnr-management-service/matterdoclistheader/${obj.matterNumber}?checkListNo=${obj.checkListNo}&classId=${obj.classId}&clientId=${obj.clientId}&languageId=${obj.languageId}`);     ///YETTOBEPROD
  // }
  Update(obj: any, code: any, checkListNo: any, classId: any, clientId: any, languageId: any) {
    return this.http.patch<any>(`/mnr-management-service/matterdoclistheader/` + code + `?checkListNo=` + checkListNo + '&classId=' + classId + '&clientId=' + clientId + '&languageId=' + languageId, obj);
  }
  UpdateNew(obj: any, code: any) {
    return this.http.patch<any>(`/mnr-management-service/matterdoclistheader/new/` + code, obj);
  }
}
