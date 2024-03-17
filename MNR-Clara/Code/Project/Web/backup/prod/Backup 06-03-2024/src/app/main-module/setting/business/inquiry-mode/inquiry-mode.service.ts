import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';


export interface InquiryModeElement {
  inquiryModeDescription: string;
  inquiryModeStatus: string;
  inquiryModeId: number;
  classId: number;
  clientId: number;
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  languageId: string;
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
  updatedBy: string;
  updatedOn: Date;
}
@Injectable({
  providedIn: 'root'
})
export class InquiryModeService {


  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `inquiryMode`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `inquiryMode/` + code);
  }
  Create(obj: InquiryModeElement) {
    return this.http.post<any>(this.apiName + `inquiryMode`, obj);
  }
  Update(obj: InquiryModeElement, code: any) {
    return this.http.patch<any>(this.apiName + `inquiryMode` + `?inquiryModeId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `inquiryMode/` + code);
  }
}
