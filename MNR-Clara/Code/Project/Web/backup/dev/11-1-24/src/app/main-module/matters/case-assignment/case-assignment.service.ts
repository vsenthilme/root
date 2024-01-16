import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


export interface CaseAssignmentElement {
  assignedTimeKeeper: string;
  caseCategoryId: number;
  caseInformationNo: string;
  caseOpenedDate: Date;
  caseSubCategoryId: number;
  classId: number;
  clientId: string;
  createdBy: string;
  createdOn: Date;
  clientName:string;
  deletionIndicator: number;
  languageId: string;
  legalAssistant: string;
  matterNumber: string;
  originatingTimeKeeper: string;
  partner: string;
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
  responsibleTimeKeeper: string;
  statusId: number;
  updatedBy: string;
  updatedOn: Date;
  statusIddes: string;
  name: string;
}
@Injectable({
  providedIn: 'root'
})
export class CaseAssignmentService {

  constructor(private http: HttpClient) { }

  apiName = '/mnr-management-service/';
  method = 'matterassignment'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }
  Create(obj: CaseAssignmentElement) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, code: CaseAssignmentElement) {
    return this.http.patch<any>(this.url + `/` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + '/' + code);
  }
  Search(obj: any) {
    return this.http.post<any>(this.url + '/findMatterAssignment', obj);
  }
}