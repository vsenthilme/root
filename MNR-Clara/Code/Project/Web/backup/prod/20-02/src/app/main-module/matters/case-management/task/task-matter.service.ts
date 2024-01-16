import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

export interface TaskMatterElement {
  caseCategoryId: number;
  caseSubCategoryId: number;
  classId: number;
  clientId: string;
  courtDate: Date;
  deadlineCalculationDays: number;
  deadlineDate: Date;
  deletionIndicator: number;
  languageId: string;
  matterNumber: string;
  priority: string;
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
  reminderCalculationDays: number;
  reminderDate: Date;
  statusId: number;
  statusIddes: string;
  taskAssignedTo: string;
  taskCompletedBy: string;
  taskCompletedOn: Date;
  taskDescription: string;
  taskEmailId: string;
  taskName: string;
  taskNumber: string;
  taskTypeCode: string;
  timeEstimate: Date;
}
@Injectable({
  providedIn: 'root'
})
export class TaskMatterService {


  constructor(private http: HttpClient) { }

  apiName = '/mnr-management-service/';
  method = 'mattertask'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }
  Create(obj: TaskMatterElement) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: TaskMatterElement, code: any) {
    return this.http.patch<any>(this.url + `/` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + '/' + code);
  }
  Search(obj: any) {
    return this.http.post<any>(this.url + '/findMatterTasks', obj);
  }

  GetUser(code: string) {
    return this.http.get<any>('/mnr-setup-service/userProfile/' + code);
  }


  GetMatterDetails(code: string) {
    return this.http.get<any>(this.apiName + 'mattergenacc/' + code);
  }

  GetdeadlineCalculationDays(code: string) {
    return this.http.get<any>('/mnr-setup-service/deadlineCalculator');
  }
}
