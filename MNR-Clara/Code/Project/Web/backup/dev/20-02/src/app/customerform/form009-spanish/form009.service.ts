import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class Form009Service {

  constructor(private http: HttpClient) { }

  // apiName = '/wms-crm-service/';
  apiName = '/mnr-crm-service/itform/';
  methodeName = 'itform009';
  url = this.apiName + this.methodeName;

  // Get(classID: any, inquiryNo: any, itFormID: any, itFormNo: any, itlanguage: any) {


  //   return this.http.get<any>(this.apiName + 'itform000/id?classID=' + classID + '&inquiryNo=' + inquiryNo + '&itFormID=' + itFormID + '&itFormNo=' + itFormNo + '&language=' + itlanguage);
  // }

  Get(classID: any, matterNumber: any, itFormID: any, itFormNo: any, itlanguage: any, clientId: any) {


    return this.http.get<any>(this.url + '/id?classID=' + classID + '&matterNumber=' + matterNumber + '&itFormID=' + itFormID + '&itFormNo=' + itFormNo + '&language=' + itlanguage + '&clientId=' + clientId);
  }
  // Create(obj: any) {
  //   return this.http.post<any>(this.url, obj);
  // }
  Update(obj: any) {
    return this.http.patch<any>(this.url, obj);
  }

}