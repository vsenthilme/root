import { HttpClient, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ReceiptNoService {

  constructor(private http: HttpClient) { }

  apiName = '/mnr-management-service/';
  method = 'receiptappnotice'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(matterNumber: any, classId: any, receiptNo: any, languageId: any) {
    return this.http.get<any>(this.url + '/' + receiptNo + '?classId=' + classId + '&languageId=' + languageId + '&matterNumber=' + matterNumber);
  }
  Create(obj: any) {


    return this.http.post<any>(this.url, obj);
  }
  // Create(nvpdata: any, myFormData: any) {

  //   const config = new HttpRequest('POST', this.url + '?' + nvpdata, myFormData, {
  //     reportProgress: true
  //   })
  //   return this.http.request(config);

  //   return this.http.post<any>(this.url, obj);
  // }
  // Update(nvpdata: any, myFormData: any) {

  //   const config = new HttpRequest('PATCH', this.url + '?' + nvpdata, myFormData, {
  //     reportProgress: true
  //   })
  //   return this.http.request(config);

  //   // return this.http.post<any>(this.url, obj);
  // }
  Update(obj: any, receiptNo: any, classId: any, matterNumber: any, documentType: any, languageId: any) {
    return this.http.patch<any>(this.url + `/` + '?receiptNo=' + receiptNo + '&classId=' + classId + '&matterNumber=' + matterNumber + '&documentType=' + documentType + '&languageId=' + languageId, obj);
  }
  Delete(code: any, classId: any, matterNumber: any, languageId: any) {
    return this.http.delete<any>(this.url + '/' + code + '?classId=' + classId + '&matterNumber=' + matterNumber +  '&languageId=' + languageId);
  }
  // Search(obj: any) {
  //   return this.http.post<any>(this.url + '/findMatterExpenses', obj);
  // }

}
