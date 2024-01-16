import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class EnglishService {
  constructor(private http: HttpClient) { }

  // apiName = '/wms-crm-service/';
  apiName = '/mnr-crm-service/itform/';
  methodeName = 'itform002';
  methodeName1 = 'itform002/attorney';
  url = this.apiName + this.methodeName;
  url1 = this.apiName + this.methodeName1;
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
  GetAttorney(classID: any, inquiryNo: any, itFormID: any, itFormNo: any, itlanguage: any) {
    return this.http.get<any>(this.url1 + '?classID=' + classID + '&inquiryNo=' + inquiryNo + '&itFormID=' + itFormID + '&itFormNo=' + itFormNo + '&language=' + itlanguage);
  }
  CreateAttorney(obj: any) {
    return this.http.post<any>(this.url1, obj);
  }
}
