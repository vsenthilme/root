import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface MatterExpensesElement {
  billType: string;
  caseCategoryId: number;
  caseInformationNo: string;
  caseSubCategoryId: number;
  classId: number;
  clientId: string;
  costPerItem: number;
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  expenseAccountNumber: string;
  expenseAmount: number;
  expenseCode: string;
  expenseDescription: string;
  expenseType: string;
  languageId: string;
  matterNumber: string;
  numberofItems: number;
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
  updatedBy: string;
  updatedOn: Date;
  writeOff: boolean;
  statusIddes: string;
  matterExpenseId: string;
}
@Injectable({
  providedIn: 'root'
})
export class MatterExpensesService {

  constructor(private http: HttpClient) { }

  apiName = '/mnr-management-service/';
  method = 'matterexpense'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.url + `/` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + '/' + code);
  }
  Search(obj: any) {
    return this.http.post<any>(this.url + '/findMatterExpenses', obj);
  }

  multipleExpenseupdate(obj: any) {
    return this.http.patch<any>(this.url + '/status', obj);
  }

}
