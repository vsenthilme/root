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
  referenceField27: string;
  referenceField26: string;
  referenceField23: string;
  referenceField28: string;
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
export class GeneralMatterService {


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
  Create(obj: GeneralMatterElement) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: GeneralMatterElement, code: any) {
    return this.http.patch<any>(this.url + `/` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + '/' + code);
  }
  Search(obj: any) {
    return this.http.post<any>(this.url + '/findMatterGenAccs', obj);
  }


  GetClient(code: string) {
    return this.http.get<any>(this.apiName + 'clientgeneral/' + code);
  }
  GetClientdetails() {
    return this.http.get<any>('/mnr-management-service/clientgeneral/');
  }
  GetTimeKeper(code: string) {
    return this.http.get<any>(this.apiName + 'matterassignment/' + code);
  }


  Updateaccounting(obj: any, code: any) {
    return this.http.patch<any>(this.url + `/` + code + '/accounting', obj);
  }

  getUpdateImmigration(matterNumber: any, matterId: any) {
    return this.http.get<any>(this.url + `/` + matterNumber + '/docketwise/' + matterId);
  }

  saveMatterCheckList(obj: any) {
    return this.http.post<any>(this.apiName + 'matterdoclist', obj);
  }
  getAllMatterDocList(matterNumber: any) {
    let obj: any = {};
    obj['matterNumber'] = [matterNumber];
    // return this.http.get<any>(this.apiName + 'matterdoclist/' + result.matterNumber + `?checkListNo=${result.checkListNo}&classId=${result.classId}&clientId=${result.clientId}&languageId=${result.languageId}`);
    return this.http.post<any>(this.apiName + 'matterdoclist/findMatterDocList', obj);
  }

  getAllMatterByPagination(pageNo, pageSize, classObj) {
    return this.http.get<any>(this.url + `/pagination?pageNo=${pageNo}&pageSize=${pageSize}&classId=${classObj}&sortBy=${'createdOn'}`);
  }

  getAllSearchDropDown() {
    return this.http.get<any>(this.url + `/dropdown`);
  }
  deleteMatterChecklistDocument(obj: any) {
    return this.http.delete<any>(this.apiName + `matterdoclist/${obj.matterNumber}?checkListNo=${obj.checkListNo}&classId=${obj.classId}&clientId=${obj.clientId}&languageId=${obj.languageId}`);     ///YETTOBEPROD
  }

  getMatterBillingActivityReport(obj: any) {
    return this.http.post<any>(`/mnr-accounting-service/invoiceheader/matterBillingActivityReport`, obj);
  }

  getTransferMatterBillingActivityReport(obj: any) {
    return this.http.get<any>(`/mnr-accounting-service/invoiceheader/transferBilling?fromDateRange=${obj.fromDateRange}&fromMatterNumber=${obj.fromMatterNumber}&toDateRange=${obj.toDateRange}&toMatterNumber=${obj.toMatterNumber}`);      
  }



  saveMatterCheckListHeader(obj: any) {   
    return this.http.post<any>(this.apiName + 'matterdoclistheader', obj);    ///09/02/23
  }

}
