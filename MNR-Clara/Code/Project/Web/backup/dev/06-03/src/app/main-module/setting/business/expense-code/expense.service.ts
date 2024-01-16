


import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


export interface ExpenseElement {
  classId: number;
  costPerItem: number;
  costType: string;
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  expenseCode: string;
  expenseCodeDescription: string;
  expenseCodeStatus: string;
  languageId: string;
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
  updatedBy: string;
  updatedOn: Date;
}
@Injectable({
  providedIn: 'root'
})
export class ExpenseService {



  constructor(private http: HttpClient) { }

  apiName = '/mnr-setup-service/';
  method = 'expenseCode'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }
  Create(obj: ExpenseElement) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: ExpenseElement, code: any) {
    return this.http.patch<any>(this.url + `?expenseCode=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + '/' + code);
  }
}
