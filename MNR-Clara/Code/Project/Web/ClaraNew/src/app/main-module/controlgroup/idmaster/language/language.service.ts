import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';


export interface CaseCategoryElement {
  createdBy: string
  createdOn: string
  deletionIndicator: number
  languageDescription: string
  languageId: string
  referenceField1: string
  referenceField10: string
  referenceField2: string
  referenceField3: string
  referenceField4: string
  referenceField5: string
  referenceField6: string
  referenceField7: string
  referenceField8: string
  referenceField9: string
  updatedBy: string
  updatedOn: string
}
@Injectable({
  providedIn: 'root'
})
export class LanguageService {
    constructor(private http: HttpClient) { }
  
    apiName = '/mnr-cg-setup-service/';
  
    Getall() {
      return this.http.get<any>(this.apiName + `languageid`);
    }
    Get(code: string) {
      return this.http.get<any>(this.apiName + `languageid/` + code);
    }
    Create(obj: any) {
      return this.http.post<any>(this.apiName + `languageid`, obj);
    }
    Update(obj: any, code: any) {
      return this.http.patch<any>(this.apiName + `languageid/`  + code, obj);
    }
    Delete(code: any) {
      return this.http.delete<any>(this.apiName + `languageid/` + code);
    }
    search(obj:any){
      return this.http.post<any>('/mnr-cg-setup-service/languageid/findlanguageid', obj);
    }
    searchStorePartner(obj:any){
      return this.http.post<any>('/mnr-cg-transaction-service/storepartnerlisting/findStorePartnerListing', obj);
    }
  }