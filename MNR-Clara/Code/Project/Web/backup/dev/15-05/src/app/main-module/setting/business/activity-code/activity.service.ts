
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


export interface ActivityElement {
  activityCodeId: any;
  activityCode: string;
  activityCodeDescription: string;
  activityCodeStatus: string;
  classId: number;
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  languageId: string;
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
export class ActivityService {



  constructor(private http: HttpClient) { }

  apiName = '/mnr-setup-service/';
  method = 'activityCode'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }
  Create(obj: ActivityElement) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: ActivityElement, code: any) {
    return this.http.patch<any>(this.url + `?activityCodeId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + '/' + code);
  }
}
