import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';
import { StatusEditComponent } from '../../configuration/status-id/status-edit/status-edit.component';


export interface CaseCategoryElement {
  categoryDescription: string;
  caseCategory: string;
  categoryStatus: string;
  caseCategoryId: number;
  classId: number;
  taxType: string;
  clientId: number;
  createdBy: string;
  createdOn: Date;
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
  updatedBy: string;
  updatedOn: Date;
}
@Injectable({
  providedIn: 'root'
})
export class CaseCategoryService {


  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `caseCategory`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `caseCategory/` + code);
  }
  Create(obj: CaseCategoryElement) {
    return this.http.post<any>(this.apiName + `caseCategory`, obj);
  }
  Update(obj: CaseCategoryElement, code: any) {
    return this.http.patch<any>(this.apiName + `caseCategory` + `?caseCategoryId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `caseCategory/` + code);
  }
}
