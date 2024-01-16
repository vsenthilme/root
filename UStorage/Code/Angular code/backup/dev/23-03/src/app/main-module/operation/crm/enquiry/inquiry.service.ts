import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';


export interface inquiryElement{
  createdBy: string;
  createdOn: Date;
  customerGroup: string;
  deletionIndicator: number;
  email: string;
  enquiryId: string;
  enquiryMobileNumber: string;
  enquiryName: string;
  enquiryRemarks: string;
  enquiryStatus: string;
  enquiryStoreSize: string;
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
  requirementDetail: string;
  requirementType: string;
  sbu: string;
  updatedBy: string;
  updatedOn: Date;
}

@Injectable({
  providedIn: 'root'
})
export class InquiryService {

  constructor(private http: HttpClient, private auth: AuthService) { }


  apiName = '/us-trans-service/';
  methodName = 'crm/';
  url = this.apiName + this.methodName;
  Getall() {
    return this.http.get<any>(this.url + `enquiry`);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + `enquiry/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url + `enquiry`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.url + `enquiry/` + code, obj);
  }
  Delete(enquiry: any,) {
    return this.http.delete<any>(this.url + `enquiry/` + enquiry  );
  }
  search(obj: any) {
    return this.http.post<any>(this.url + 'enquiry/find', obj);
  }
 
}
