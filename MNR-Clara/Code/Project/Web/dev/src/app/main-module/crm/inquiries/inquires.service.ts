
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AuthService } from 'src/app/core/core';
export interface inquiryElement {

  assignedUserId: string;
  classId: number;
  classIddes: number;
  contactNumber: string;
  createdBy: string;
  screatedOn: Date;
  createdOn: Date;
  deletionIndicator: number;
  email: string;
  firstName: string;
  inquiryDate: Date;
  sinquiryDate: Date;
  inquiryModeId: number;
  inquiryNotesNumber: string;
  inquiryNumber: number;
  intakeFormId: number;
  intakeNotesNumber: string;
  languageId: string;
  lastName: string;
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
  transactionId: number;
  updatedBy: string;
  updatedOn: Date;

  statusIddesc: string;

}
@Injectable({
  providedIn: 'root'
})
export class InquiresService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  // apiName = '/wms-crm-service/';
  apiName = '/mnr-crm-service/';
  methodeName = 'inquiry';
  url = this.apiName + this.methodeName;
  methodeName_2 = 'intake';

  urlintake = this.apiName + this.methodeName + '/' + this.methodeName_2;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + `/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.url + `?inquiryNumber=` + code, obj);
  }

  Assign(obj: any, code: any) {
    return this.http.patch<any>(this.url + '/' + code + `/assignInquiry`, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + `/` + code);
  }
  updateValiationStatus(obj: any, code: any, status: any) {

    return this.http.patch<any>(this.url + '/' + code + `/updateValiationStatus?status=` + status, obj);

  }

  updateStatus_Intake(obj: any, code: any) {

    return this.http.patch<any>(this.urlintake + '/updateStatus?inquiryNumber=' + code, obj);

  }

  updateInquiryIntake(obj: any, code: any) {

    return this.http.patch<any>(this.url + '/' + code + '/updateInquiryIntake', obj);

  }
  sendmail(obj: any) {
    return this.http.post<any>(this.urlintake + '/sendFormThroEmail', obj);


  }

  Search(obj: any) {
    return this.http.post<any>(this.url + '/findInquiry', obj);
  }



  getfile(url: any, code: any): Promise<File> {
    return this.http
      .get<any>('/doc-storage/download?fileName=' + url + '&location=' + code, {
        responseType: 'blob' as 'json',
      })
      .toPromise();
  }
  
}