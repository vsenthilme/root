import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MatterIntakeService {
  constructor(private http: HttpClient) { }

  apiName = '/mnr-management-service/';
  methodeName = 'matteritform'
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
  Approve(intakeFormNumber: string, obj: any) {
    return this.http.patch<any>(this.url + '/' + intakeFormNumber + '/approve', obj);
  }
}
