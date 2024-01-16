import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';


export interface CaseSubCategoryElement {
  categoryDescription: string;
  caseCategory: string;
  categoryStatus: string;
  caseCategoryId: number;
  caseSubcategoryId: string;
  classId: number;
  taxType: string
  clientId: number;
  subCategory: string;
  subCategoryDescription: string;
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

  caseCategoryIddes: string;
  classIddes: number;

}
@Injectable({
  providedIn: 'root'
})
export class CaseSubCategoryService {


  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `caseSubcategory`);
  }
  Get(code: string, classId: any, languageId: any, caseCategoryId: any) {
    return this.http.get<any>(this.apiName + `caseSubcategory/` + code + '?caseCategoryId=' + caseCategoryId + '&classId=' + classId + '&languageId=' + languageId);
  }
  Create(obj: CaseSubCategoryElement) {
    return this.http.post<any>(this.apiName + `caseSubcategory`, obj);
  }
  Update(obj: CaseSubCategoryElement, code: any, classId: any, languageId: any, caseCategoryId: any) {
    return this.http.patch<any>(this.apiName + `caseSubcategory?caseSubcategoryId=` + code + '&caseCategoryId=' + caseCategoryId + '&classId=' + classId + '&languageId=' + languageId, obj);
  }
  Delete(code: any, classId: any, languageId: any, caseCategoryId: any) {
    return this.http.delete<any>(this.apiName + 'caseSubcategory/{caseSubcategoryId}?caseSubcategoryId=' + code + '&caseCategoryId=' + caseCategoryId + '&classId=' + classId + '&languageId=' + languageId);
  }

  Getcasecat() {
    return this.http.get<any>('/mnr-setup-service/caseCategory/');
  }
}
