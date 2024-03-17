import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private http: HttpClient) { }

  apiName = '/mnr-accounting-service/';
  method = 'dashboard'
  url = this.apiName + this.method;

  getBillableUnBillableTimeData(classId: any, period: any) {
    return this.http.get<any>(this.url + `/billableUnbillableTime?classId=${classId}&period=${period}`);
  }
  getClientReferralData(classId: any, period: any) {
    return this.http.get<any>(this.url + `/clientReferral?classId=${classId}&period=${period}`);
  }
  getPracticeBreakdown(classId: any, period: any) {
    return this.http.get<any>(this.url + `/practiceBreakDown?classId=${classId}&period=${period}`);
  }
  getTopClients(period: any) {
    return this.http.get<any>(this.url + `/topClients?period=${period}`);
  }
  getCaseAssignment(classId: any, timeKeeper: any) {
    return this.http.get<any>(this.url + `/caseAssignment?classId=${classId}&timeKeepers=${timeKeeper}`);
  }
  getBilledIncome(classId: any, period: any) {
    return this.http.get<any>(this.url + `/billedIncome?classId=${classId}&period=${period}`);
  }
  getLeadConversion(classId: any, period: any) {
    return this.http.get<any>(this.url + `/leadConversion?classId=${classId}&period=${period}`);
  }
}
