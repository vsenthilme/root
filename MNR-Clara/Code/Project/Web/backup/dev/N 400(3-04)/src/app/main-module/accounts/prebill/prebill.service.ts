import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PrebillService {

  constructor(private http: HttpClient) { }

  apiName = '/mnr-accounting-service/';
  method = 'prebilldetails'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url + '/preBillNumber');
  }
  Get(code: string, matterNumber: any, preBillBatchNumber: any, preBillDate: any) {
    return this.http.get<any>(this.url + '/' + code + '?matterNumber=' + matterNumber + '&preBillBatchNumber=' + preBillBatchNumber + '&preBillDate=' + preBillDate);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, code: any, preBillBatchNumber: any) {
    return this.http.patch<any>(this.url + '/' + code + `?preBillBatchNumber=` + preBillBatchNumber, obj);
  }
  Delete(code: any, preBillBatchNumber: any) {
    return this.http.delete<any>(this.url + '/' + code + `?preBillBatchNumber=` + preBillBatchNumber);
  }

  Search(obj: any) {
    return this.http.post<any>(this.url + '/findPreBillDetails', obj);
  }

  excute(obj: any, isByIndividual: boolean) {
    return this.http.post<any>(this.url + '/executeBill?isByIndividual=' + isByIndividual, obj);
  }


  getexpensepreBillNo(preBillNo: any) {
    return this.http.get<any>('/mnr-management-service/matterexpense/' + preBillNo + '/preBillApprove');
  }
  gettimeticketpreBillNo(preBillNo: any) {
    return this.http.get<any>('/mnr-management-service/mattertimeticket/' + preBillNo + '/preBillApprove');
  }

  Approve(obj: any) {
    return this.http.post<any>(this.url + '/approve', obj);
  }
  save(obj: any) {
    return this.http.post<any>(this.url + '/save', obj);
  }
  GetClientdetails() {
    return this.http.get<any>('/mnr-management-service/clientgeneral/');
  }
  getmatter() {
    return this.http.get<any>('/mnr-management-service/mattergenacc');
  }
  getclassdetails() {
    return this.http.get<any>('/mnr-setup-service/class');
  }

  getbillmodedetails() {
    return this.http.get<any>('/mnr-setup-service/billingMode/');
  }
  getcasecatdetails() {
    return this.http.get<any>('/mnr-setup-service/caseCategory');
  }
  getcasesubdetails() {
    return this.http.get<any>('/mnr-setup-service/caseSubcategory');
  }
  getresptimedetails() {
    return this.http.get<any>('/mnr-setup-service/timekeeperCode');
  }
  getassigntimedetails() {
    return this.http.get<any>('/mnr-setup-service/timekeeperCode');
  }
  getReferralDetails() {
    return this.http.get<any>('/mnr-setup-service/referral');
  }
  getReferralReport(obj: any) {
    return this.http.post<any>('/mnr-crm-service/potentialClient/referralReport', obj);
  }
  getPotentialClientReport(obj: any) {
    return this.http.post<any>('/mnr-crm-service/potentialClient/report', obj);
  }
  getLeadConversionReport(obj: any) {
    return this.http.post<any>('/mnr-crm-service/pcIntakeForm/leadConversionReport', obj);
  }
  // getClientImmigrationReport(obj: any) {
  //   return this.http.post<any>('/mnr-management-service/clientgeneral/immigrationReport', obj);
  // }

  getClientImmigrationReport(obj: any) {
    return this.http.post<any>('/mnr-management-service/clientgeneral/immigrationReport', obj);
  }


  // getClientImmigrationReport(classId: string, clientCategoryId: string, clientId: string, fromCreatedOn: string,fromDateClosed: string, referralId: string[],statusId: string[],toCreatedOn: string,toDateClosed: string, docketwiseId: string) {
  //   return this.http.post<any>('/mnr-management-service/clientgeneral/immigrationReport'+ '?classId=' + classId +'&statusId=' + statusId + '&clientId=' + clientId +  '&clientCategoryId=' + clientCategoryId + +
  //   '&referralId=' + referralId  + '&docketwiseId=' + docketwiseId, null);

  getMatterLnEReport(obj: any) {
    return this.http.post<any>('/mnr-management-service/mattergenacc/lneReport', obj);
  }
  getMatterImmReport(obj: any) {
    return this.http.post<any>('/mnr-management-service/mattergenacc/immigrationReport', obj);
  }

  getPreBillPdfData(matterNumber, preBillNumber) {
    return this.http.get<any>(this.apiName + 'outputforms/preBill' + `?matterNumber=${matterNumber}&preBillNumber=${preBillNumber}`);
  }


  getMatterExpiration(obj: any) {
    return this.http.post<any>('/mnr-management-service/expirationdate/findExpirationDate', obj);
  }

  getNotificationsDBData(userId: string) {
    return this.http.get<any>("/mnr-crm-service/notification-message" + '?userId=' + userId);
  }
  markNotificationsAsRead() {
    return this.http.get<any>("/mnr-crm-service/notification-message/mark-read-all");
  }
  readOnenotification(id: any) {
    return this.http.get<any>("/mnr-crm-service/notification-message/mark-read/" + id);
  }
}
