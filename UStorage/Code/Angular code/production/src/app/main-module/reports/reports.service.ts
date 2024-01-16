import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class ReportsService {

  
  constructor(private http: HttpClient, private auth: AuthService) { }


  getWorkOrderLeadTime(obj: any) {
    return this.http.post<any>('/us-trans-service/operations/workorder/find', obj);
  }
  getEmployeeEfficiencyTime(obj: any) {
    return this.http.post<any>('/us-trans-service/reports/efficiencyRecordReport', obj);
  }
  getWorkOrderStatusTime(obj: any) {
    return this.http.post<any>('/us-trans-service/reports/workOrderStatusReport', obj);
  }
  getenquiryIdTime(obj: any) {
    return this.http.post<any>('/us-trans-service/reports/enquiryStatusReport', obj);
  }
  getquoteIdTime(obj: any) {
    return this.http.post<any>('/us-trans-service/reports/quotationStatusReport', obj);
  }
  getFillrateTime(obj: any) {
      return this.http.post<any>('/us-trans-service/reports/fillrateStatusReport', obj);
      }
  getStorageunitTime(obj:any) {
    return this.http.post<any>('/us-trans-service/master/storageunit/find', obj);
  }
  getMasterdataTime(obj:any) {
    return this.http.post<any>('/us-trans-service/master/consumables/find', obj);
  }
  getAgreementTime(obj:any) {
    return this.http.post<any>('/us-trans-service/reports/documentStatusReport', obj);
  }
  getAgreementrenewTime(obj:any) {
    return this.http.post<any>('/us-trans-service/reports/contractRenewalStatusReport', obj);
  }
  getpaymentdue(obj:any) {
    return this.http.post<any>('/us-trans-service/reports/paymentDueStatusReport', obj);
  }
}
