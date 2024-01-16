
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
export class documentTemplateService {


  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `documentTemplate`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `documentTemplate/` + code);
  }
  Create(obj: documentElement) {
    return this.http.post<any>(this.apiName + `documentTemplate`, obj);
  }
  Update(obj: documentElement, code: any) {
    return this.http.patch<any>(this.apiName + `documentTemplate` + `?documentNumber=` + code, obj);
  }
  Delete(obj: any) {
    const options = {

      body: obj
    }
    return this.http.delete<any>(this.apiName + `documentTemplate`, options);
  }
  createChecklistDocument(obj: any[]) {
    return this.http.post<any>(this.apiName + `docchecklist`, obj);
  }
  getChecklistDocument() {
    return this.http.get<any>(this.apiName + `docchecklist`);
  }
  filterChecklistDocument(obj: {}) {
    return this.http.post<any>(this.apiName + `docchecklist/findDocCheckList`, obj);
  }

  Search(obj: any) {
    return this.http.post<any>(this.apiName + `docchecklist` + '/findDocCheckList', obj);
  }

  deleteChecklistDocument(obj: any) {
    return this.http.delete<any>(this.apiName + `docchecklist/${obj.checkListNo}?sequenceNo=${obj.sequenceNo}&caseCategoryId=${obj.caseCategoryId}&caseSubCategoryId=${obj.caseSubCategoryId}&classId=${obj.classId}&languageId=${obj.languageId}`);
  }

  deleteChecklist(obj: any) {
    return this.http.delete<any>(this.apiName + `docchecklist/${obj.checkListNo}?caseCategoryId=${obj.caseCategoryId}&caseSubCategoryId=${obj.caseSubCategoryId}&classId=${obj.classId}&languageId=${obj.languageId}`);
  }

  getfile1(fileName: any, clientId: any, matterNumber: any): Promise<File> {
    return this.http
      .get<any>(`/doc-storage/download?fileName=${fileName}&location=${"receipt"}/${clientId}/${matterNumber}`, {
        responseType: 'blob' as 'json',
      })
      .toPromise();
  }

  getClientChecklistFile(fileName: any): Promise<File> {
    return this.http
      .get<any>(`/doc-storage/download?fileName=${fileName}&location=${"document/checklist"}`, {
        responseType: 'blob' as 'json',
      })
      .toPromise();
  }

  download(fileName: any, clientId: any, matterNumber: any, location: any): Promise<File> {
    return this.http
      .get<any>(`/doc-storage/download?fileName=${fileName}&location=${location}/${clientId}/${matterNumber}`, {
        responseType: 'blob' as 'json',
      })
      .toPromise();
  }
}
