import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})


export class SpanishService {
  constructor(private http: HttpClient) { }

  // apiName = '/wms-crm-service/';
  apiName = '/mnr-crm-service/itform/';
  methodeName = 'itform003';
  url = this.apiName + this.methodeName;

  // Get(classID: any, inquiryNo: any, itFormID: any, itFormNo: any, itlanguage: any) {


  //   return this.http.get<any>(this.apiName + 'itform000/id?classID=' + classID + '&inquiryNo=' + inquiryNo + '&itFormID=' + itFormID + '&itFormNo=' + itFormNo + '&language=' + itlanguage);
  // }

  Get(classID: any, inquiryNo: any, itFormID: any, itFormNo: any, itlanguage: any) {


    return this.http.get<any>(this.url + '/id?classID=' + classID + '&inquiryNo=' + inquiryNo + '&itFormID=' + itFormID + '&itFormNo=' + itFormNo + '&language=' + itlanguage);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any) {
    return this.http.patch<any>(this.url, obj);
  }
  getreferral() {
    return this.http.get<any>('/mnr-setup-service/referral');
  }
}
