import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


export interface TimekeeperElement {
  classId: number;
  createdBy: string;
  createdOn: Date;
  defaultRate: number;
  deletionIndicator: number;
  languageId: string;
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
  timekeeperCode: string;
  timekeeperName: string;
  timekeeperStatus: string;
  updatedBy: string;
  updatedOn: Date;
  userTypeId: number;
  userTypeIddesc: string;
}
@Injectable({
  providedIn: 'root'
})
export class TimekeeperService {



  constructor(private http: HttpClient) { }

  apiName = '/mnr-setup-service/';
  method = 'timekeeperCode'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }
  Create(obj: TimekeeperElement) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.url + `?timekeeperCode=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + '/' + code);
  }



  GetUserName(code: string) {
    return this.http.get<any>(this.apiName + 'user/' + code);
  }


  getTimekeeper(code: string){
    return this.http.get<any>('/mnr-setup-service/userProfile/' + code);
  }

}
