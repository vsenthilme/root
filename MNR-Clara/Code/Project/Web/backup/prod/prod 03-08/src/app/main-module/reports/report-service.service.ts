import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ReportServiceService {

  constructor(private http: HttpClient) { }

  apiName = '/mnr-management-service/';
  method = 'mattergenacc'
  url = this.apiName + this.method;

  // getClientLandEReport(obj: any) {
  //   return this.http.post<any>('/mnr-management-service/clientgeneral/lneReport', obj);
  // }

  getClientLandEReport(obj: any) {
    return this.http.post<any>('/mnr-management-service/clientgeneral/lneReport', obj);
  }


  // getClientLandEReport(classId: string, clientCategoryId: string[], clientId: string[], fromCreatedOn: string,fromDateClosed: string, referralId: string[],statusId: string[],toCreatedOn: string,toDateClosed: string) {
  //   return this.http.post<any>('/mnr-management-service/clientgeneral/lneReport'+ '?classId=' + classId +'&statusId=' + statusId + '&clientId=' + clientId +  '&clientCategoryId=' + clientCategoryId + '&fromCreatedOn=' + fromCreatedOn +
  //   '&fromDateClosed=' + fromDateClosed + '&referralId=' + referralId  + '&toCreatedOn=' + toCreatedOn  + '&toDateClosed=' + toDateClosed, null);
  // }

  getMatterWipAgedPBReport(obj: any) {
    return this.http.post<any>('/mnr-management-service/mattergenacc/wipAgedPBReport', obj);
  }

  getARAgingReport(obj: any) {
    return this.http.post<any>('/mnr-accounting-service/invoiceheader/arAgingReport', obj);
  }

  getBillingReport(obj: any) {
    return this.http.post<any>('/mnr-accounting-service/invoiceheader/billingReport', obj);
  }

  getWipReportDetails( obj:any, pageNumber, pageSize) {
    return this.http.post<any>('/mnr-management-service/mattergenacc/wipAgedPBReport/pagination' + '?pageNo=' + pageNumber + '&pageSize=' + pageSize, obj);
  }


  getAllGbSync(obj: any) {
    return this.http.post<any>('/soapservice/qbsync/findQbSync', obj);
  }

  reRunClient(clientId: any, obj: any) {
    return this.http.post<any>('/soapservice/clientgeneral/rerun' + '?clientId=' + clientId, obj);
  }

  reRunMatter(matterNumber: any, obj: any) {
    return this.http.post<any>('/soapservice/mattergeneral/rerun' + '?matterNumber=' + matterNumber, obj);
  }

  reRunInvoice(invoiceNumber: any, obj: any) {
    return this.http.post<any>('/soapservice/invoice/rerun' + '?invoiceNumber=' + invoiceNumber, obj);
  }

  docketwise(matterNumber: any) {
    return this.http.get<any>('/mnr-management-service/mattergenacc/' + matterNumber + '/push2Docketwise');
  }


  getClientCash(obj: any) {
    return this.http.post<any>('/mnr-accounting-service/dashboard/client-cash-receipt-report', obj);
  }
  getClientIncome(obj: any) {
    return this.http.post<any>('/mnr-accounting-service/dashboard/client-income-summary-report', obj);
  }
  getAR(obj: any) {
    return this.http.post<any>('/mnr-accounting-service/dashboard/ar-report', obj);
  }
  getBilledandNonBilled(obj: any) {
    return this.http.post<any>('/mnr-accounting-service/dashboard/billed-unbilled-hours', obj);
  }

  getMatterListing(obj: any) {
    return this.http.post<any>('/mnr-accounting-service/dashboard/matter-listing', obj);
  }
  getMatterRates(obj: any) {
    return this.http.post<any>('/mnr-accounting-service/dashboard/matter-rates-listing', obj);
  }

  getBilledPaidHours(obj: any) {
    return this.http.post<any>('/mnr-accounting-service/dashboard/billed-hours-paid-report', obj);
  }

  getWriteOff(obj: any) {
    return this.http.post<any>('/mnr-accounting-service/dashboard/write-off-report', obj);
  }

  getBilledFeesPaid(obj: any) {
    return this.http.post<any>('/mnr-accounting-service/dashboard/billed-paid-fees-report', obj);
  }

  
  getExpiration(obj: any) {
    return this.http.post<any>('/mnr-accounting-service/dashboard/expiration-date', obj);
  }

  getAttorneyProduction(obj: any) {
    return this.http.post<any>('/mnr-management-service/mattergenacc/attorneyProductivityReport', obj);
  }
  getMatterPL(obj: any) {
    return this.http.post<any>('/mnr-accounting-service/invoiceheader/matterPLReport', obj);
  }

  getlandebillinghr(obj: any) {
    return this.http.post<any>('/mnr-management-service/mattertimeticket/findTimeKeeperBillingReport', obj);
  }
}

