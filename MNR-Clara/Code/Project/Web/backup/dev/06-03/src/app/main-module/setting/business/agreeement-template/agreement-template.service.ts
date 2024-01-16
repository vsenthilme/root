
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';
export interface AgreementElement {
  agreementCode: string;
  agreementUrl: string;
  agreementFileDescription: string;
  agreementStatus: string;
  caseCategoryId: number;
  classId: number;
  clientId: number;
  mailMerge: boolean;
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
export class AgreementTemplateService {


  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `agreementTemplate`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `agreementTemplate/` + code);
  }
  Create(obj: AgreementElement) {
    return this.http.post<any>(this.apiName + `agreementTemplate`, obj);
  }
  Update(obj: AgreementElement, code: any) {
    return this.http.patch<any>(this.apiName + `agreementTemplate` + `?agreementCode=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `agreementTemplate/` + code);
  }
}
