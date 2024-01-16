import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';



export interface TransactionElement {

  classId: number;
  createdBy: string;
  deletionIndicator: number;
  languageId: string;
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
  transactionDescription: string;
  transactionId: number;
  updatedBy: string;
  createdOn: string;
}


@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `transaction`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `transaction/` + code);
  }
  Create(obj: TransactionElement) {
    return this.http.post<any>(this.apiName + `transaction`, obj);
  }
  Update(obj: TransactionElement, code: any) {
    return this.http.patch<any>(this.apiName + `transaction` + `?transactionId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `transaction/` + code);
  }
}
