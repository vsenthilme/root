import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';
export interface CompanyElement {
  companyAddress1: string;
  companyAddress2: string;
  companyId: string;
  companyName: string;
  contactName: string;
  contactNumber: string;
  createdBy: string;
  createdOn: Date;
  //deletionIndicator: number;
  languageId: string;
  // referenceField1: string;
  // referenceField10: string;
  // referenceField2: string;
  // referenceField3: string;
  // referenceField4: string;
  // referenceField5: string;
  // referenceField6: string;
  // referenceField7: string;
  // referenceField8: string;
  // referenceField9: string;
  updatedBy: string;
  updatedOn: Date;
}
@Injectable({
  providedIn: 'root'
})
export class CompanyService {


  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `company`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `company/` + code);
  }
  Create(obj: CompanyElement) {
    return this.http.post<any>(this.apiName + `company`, obj);
  }
  Update(obj: CompanyElement, code: any) {
    return this.http.patch<any>(this.apiName + `company` + `?companyId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `company/` + code);
  }
}
