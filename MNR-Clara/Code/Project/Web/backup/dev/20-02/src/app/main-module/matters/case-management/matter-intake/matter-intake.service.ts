import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
export interface MatterIntakeElement {

  approvedOn: Date;
  classId: number;
  clientId: string;
  clientUserId: string;
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  intakeFormId: number;
  intakeFormNumber: string;
  languageId: string;
  matterNumber: string;
  notesNumber: string;
  receivedOn: Date;
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
  resentOn: Date;
  sentOn: Date;
  statusId: number;
  updatedBy: string;
  updatedOn: Date;
  statusIddes: string;
}
@Injectable({
  providedIn: 'root'
})
export class MatterIntakeService {
  constructor(private http: HttpClient) { }

  apiName = '/mnr-management-service/';
  method = 'matteritform'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.url + `/` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + '/' + code);
  }
  Search(obj: any) {
    return this.http.post<any>(this.url + '/findMatterITForm', obj);
  }

}
