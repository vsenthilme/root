
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

export interface TimeTicketsElement {
  activityCode: string;
  defaultRate1: any,
  approvedBillableAmount: number;
  approvedBillableTimeInHours: number;
  assignedRatePerHour: number;
  assignedRate: any;
  approvedOn: Date;
  assignedOn: Date;
  assignedPartner: string;
  timeKeeperCodeName: string;
  timeKeeperCodeShort: any,
  billType: string;
  caseCategoryId: number;
  caseSubCategoryId: number;
  classId: number;
  clientId: string;
  createdBy: string;
  createdOn: Date;
  screatedOn: Date;
  description: string
  defaultRate: number;
  deletionIndicator: number;
  languageId: string;
  matterNumber: string;
  rateUnit: string;
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
  statusIddes: string;
  taskCode: string;
  timeKeeperCode: string;
  timeTicketAmount: number;
  timeTicketDate: Date;
  stimeTicketDate: Date;
  timeTicketDescription: string;
  timeTicketHours: number;
  timeTicketNumber: string;
  updatedBy: string;
  updatedOn: Date;
}
@Injectable({
  providedIn: 'root'
})
export class TimeTicketsService {


  constructor(private http: HttpClient) { }

  apiName = '/mnr-management-service/';
  method = 'mattertimeticket'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }
  Create(obj: TimeTicketsElement) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: TimeTicketsElement, code: any) {
    return this.http.patch<any>(this.url + `/` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + '/' + code);
  }
  Search(obj: any) {
    return this.http.post<any>(this.url + '/findMatterTimeTicket', obj);
  }
 SearchTimeTicket(obj:any){
  return this.http.post<any>( '/mnr-management-service/timeticketnotification/find', obj);
 }


}
