import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';



export interface LanguageElement {

  classId: number;
  createdBy: string;
  deletionIndicator: number;
  languageDescription: string;
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
  createdOn: Date;
}


@Injectable({
  providedIn: 'root'
})
export class LanguageService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `language`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `language/` + code);
  }
  Create(obj: LanguageElement) {
    return this.http.post<any>(this.apiName + `language`, obj);
  }
  Update(obj: LanguageElement, code: any) {
    return this.http.patch<any>(this.apiName + `language` + `?languageId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `language/` + code);
  }
}
