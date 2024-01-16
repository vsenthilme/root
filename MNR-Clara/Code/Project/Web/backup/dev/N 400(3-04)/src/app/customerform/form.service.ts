import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FormService {

  constructor(private http: HttpClient) { }

  // apiName = '/wms-crm-service/';
  apiName = '/mnr-crm-service/';
  methodeName = 'pcIntakeForm';
  url = this.apiName + this.methodeName;

  Get(intakeFormNumber: string) {
    return this.http.get<any>(this.url + '/' + intakeFormNumber);
  }
  Create(inquiryNumber: string, obj: any) {
    return this.http.post<any>(this.url + '?inquiryNumber=' + inquiryNumber, obj);
  }

  Update(intakeFormNumber: string, obj: any) {
    return this.http.patch<any>(this.url + '/' + intakeFormNumber, obj);
  }


  Receive(inquiryNumber: string, intakeFormNumber: string) {
    return this.http.patch<any>(this.url + '/' + intakeFormNumber + '/receive?inquiryNumber=' + inquiryNumber, {});
  }
  Approve(intakeFormNumber: string, statusId: any, inquiryNumber: string, obj: any) {
    return this.http.patch<any>(this.url + '/' + intakeFormNumber + '/approve?inquiryNumber=' + inquiryNumber + '&statusId=' + statusId, obj);
  }
  otherstatus(intakeFormNumber: string, statusId: any, inquiryNumber: any, obj: any) {
    return this.http.patch<any>(this.url + '/' + intakeFormNumber + '/approve?inquiryNumber=' + inquiryNumber + '&statusId=' + statusId, obj);
  }

  update(intakeFormNumber: string, inquiryNumber: string, intakeFormId: string, obj: any) {
    return this.http.patch<any>(this.url + '/' + intakeFormNumber + '/status?inquiryNumber=' + inquiryNumber + '&intakeFormId=' + intakeFormId, obj);
  }

  potentialClient_create(obj: any) {
    return this.http.patch<any>(this.apiName + 'potentialClient', obj);
  }
}
