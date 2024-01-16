import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface MatterRateElement {
  assignedRatePerHour: number;
  caseCategoryId: number;
  caseSubCategoryId: number;
  classId: number;
  clientId: string;
  createdBy: string;
  createdOn: Date;
  defaultRatePerHour: number;
  deletionIndicator: number;
  languageId: string;
  matterNumber: string;
  rateUnit: string;
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
  statusId: number;
  timeKeeperCode: string;
  updatedBy: string;
  statusIddes: string;
  updatedOn: Date;
}

@Injectable({
  providedIn: 'root'
})
export class MatterRateService {
  constructor(private http: HttpClient) { }

  apiName = '/mnr-management-service/';
  method = 'matterrate'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string, matterNumber: any) {
    return this.http.get<any>(this.url + '/' + matterNumber + '?timeKeeperCode=' + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, code: any, matterNumber: any) {
    return this.http.patch<any>(this.url + `/` + matterNumber + '?timeKeeperCode=' + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + '/' + code);
  }
  Search(obj: any) {
    return this.http.post<any>(this.url + '/findMatterRate', obj);
  }
  getMatterRateByMatterNumber(matterNumber: any) {
    return this.http.get<any>(this.url + '/'+ matterNumber + '/matterNumber');
  }
}
