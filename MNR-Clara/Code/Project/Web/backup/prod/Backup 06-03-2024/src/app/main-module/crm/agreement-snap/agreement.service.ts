import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { AuthService } from "src/app/core/core";


export interface AgreementElement {
  agreementCode: string;
  agreementURL: string;
  agreementURLVersion: string;
  approvedOn: Date;
  caseCategoryId: number;
  classId: number;
  clientId: string;
  createdBy: string;
  deletionIndicator: number;
  emailId: string;
  inquiryNumber: string;
  languageId: string;
  potentialClientId: string;
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
  transactionId: number;
  updatedBy: string;
  validatedOn: Date;//check with raj
}
@Injectable({
  providedIn: 'root'
})
export class AgreementService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  // apiName = '/wms-crm-service/';
  apiName = '/mnr-crm-service/';
  methodeName = 'agreement';
  url = this.apiName + this.methodeName;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Resend(code: string) {
    return this.http.post<any>(this.url + `/` + code, null);
  }
  Search(obj: any) {
    return this.http.post<any>(this.url + `/findAgreement`, obj);
  }
  // Update(obj: any, code: any) {
  //   return this.http.patch<any>(this.url + `?inquiryNumber=` + code, obj);
  // }
  // Delete(code: any) {
  //   return this.http.delete<any>(this.url + `/` + code);
  // }

}