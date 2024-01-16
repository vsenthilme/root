import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { throwMatDuplicatedDrawerError } from '@angular/material/sidenav';
import { AuthService } from 'src/app/core/core';
export interface IntakeElementList {
  alternateEmailId: string;
  approvedBy: string;
  approvedOn: Date;
  classId: number;
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  email: string;
  inquiryNumber: number;
  intakeFormId: number;
  intakeFormId_des: number;
  feedbackStatus: string;
  intakeFormNumber: number;
  intakeNotesNumber: string;
  languageId: string;
  receivedOn: Date;
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
  resentOn: Date;
  sentOn: Date;
  statusId: number;
  statusId_des: string;
  transactionId: number;
  updatedBy: string;
  updatedOn: Date;
  pageflow: string;


}
@Injectable({
  providedIn: 'root'
})
export class IntakeService {
  constructor(private http: HttpClient, private auth: AuthService) { }

  // apiName = '/wms-crm-service/';
  apiName = '/mnr-crm-service/';
  methodeName = 'pcIntakeForm';
  url = this.apiName + this.methodeName;
  crm = '/mnr-crm-service/';
  Getall() {
    return this.http.get<any>(this.url);
  }
  // Get(code: string) {
  //   return this.http.get<any>(this.url + `/` + code);
  // }
  // Create(obj: any) {
  //   return this.http.post<any>(this.url, obj);
  // }
  // Update(obj: any, code: any) {
  //   return this.http.patch<any>(this.url + `?inquiryNumber=` + code, obj);
  // }

  // Assign(obj: any, code: any) {
  //   return this.http.patch<any>(this.url + '/' + code + `/assignInquiry`, obj);
  // }
  Delete(intakeFormNumber: string, inquiryNumber: any) {
    return this.http.delete<any>(this.url + `/` + intakeFormNumber + '/inquiryNumber/' + inquiryNumber);
  }

  Search(obj: any) {
    return this.http.post<any>(this.url + '/findPCIntakeForm', obj);
  }

    Update(obj: any, code: any) {
    return this.http.patch<any>('/mnr-crm-service/pcIntakeForm/' + code, obj);
  }

  Get(code: string) {
    return this.http.get<any>(this.apiName + 'inquiry' + `/` + code);
  }
  
  Getinquiry() {
    return this.http.get<any>(this.apiName + 'inquiry');
  }
 Getreferal(referralId:any){
  return this.http.get<any>(' /mnr-setup-service/referral/'+ referralId);
 }
}
