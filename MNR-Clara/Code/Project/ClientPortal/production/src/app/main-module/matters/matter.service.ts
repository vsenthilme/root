import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

export interface GeneralMatterElement {

  administrativeCost: number;
  approvalDate: Date;
  arAccountNumber: string;
  billingFormatId: string;
  billingFrequencyId: string;
  billingModeId: string;
  billingRemarks: string;
  caseCategoryId: number;
  caseClosedDate: Date;
  caseFileNumber: number;
  caseFiledDate: Date;
  caseInformationNo: string;
  caseOpenedDate: Date;
  caseSubCategoryId: number;
  classId: number;
  clientId: string;
  contigencyFeeAmount: number;
  courtDate: Date;
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  directPhoneNumber: string;
  expirationDate: Date;
  fileNumber: string;
  flatFeeAmount: number;
  languageId: string;
  matterDescription: string;
  matterNumber: string;
  priorityDate: Date;
  rateUnit: string;
  receiptDate: Date;
  receiptNoticeNo: string;
  referenceField1: string;
  referenceField10: string;
  referenceField11: string;
  referenceField12: string;
  referenceField13: string;
  referenceField14: string;
  referenceField15: string;
  referenceField16: string;
  referenceField17: string;
  referenceField18: string;
  referenceField19: string;
  referenceField2: string;
  referenceField20: string;
  referenceField3: string;
  referenceField4: string;
  referenceField5: string;
  referenceField6: string;
  referenceField7: string;
  referenceField8: string;
  referenceField9: string;
  statusId: number;
  transactionId: number;
  trustDepositNo: string;
  updatedBy: string;
  updatedOn: Date;
}
@Injectable({
  providedIn: 'root'
})
export class MatterService {

  constructor(private http: HttpClient) { }

  apiName = '/mnr-management-service/';
  method = 'mattergenacc'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }

  Getstatus() {
    return this.http.get<any>('/mnr-setup-service/status');
  }

  filterMatter(obj: any) {
    return this.http.post<any>(this.url + '/findMatterGenAccs', obj);
  }
  SearchNew(obj: any) {
    return this.http.post<any>(this.url + '/findMatterGeneralNew', obj);
  }

}
