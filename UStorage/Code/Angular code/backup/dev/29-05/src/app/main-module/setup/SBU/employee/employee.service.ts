import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';


export interface employeeElement {
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  employeeCode: string;
  employeeEmail: string;
  employeeName: string;
  employeePhone: string;
  employeeSbu: string;
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
export class EmployeeService {

  constructor(private http: HttpClient, private auth: AuthService) { }
  
  apiName = '/us-master-service/';
  methodName = 'crm/';
  url = this.apiName + this.methodName;
  Getall() {
    return this.http.get<any>(this.apiName + `employee`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `employee/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `employee`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.apiName + `employee/` + code, obj);
  }
  Delete(employeeId: any,) {
    return this.http.delete<any>(this.apiName + `employee/` + employeeId  );
  }
 
}

