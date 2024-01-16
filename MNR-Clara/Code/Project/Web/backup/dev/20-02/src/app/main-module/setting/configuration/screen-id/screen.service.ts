import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';


export interface ScreenElement {

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
  screenId: number;
  screenText: string;
  subScreenId: number;
  subScreenName: string;
  updatedBy: string;
  createdOn: string;
}

@Injectable({
  providedIn: 'root'
})
export class ScreenService {
  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `screen`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `screen/` + code);
  }
  Create(obj: ScreenElement) {
    return this.http.post<any>(this.apiName + `screen`, obj);
  }
  Update(obj: ScreenElement, code: any) {
    return this.http.patch<any>(this.apiName + `screen` + `?screenId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `screen/` + code);
  }
}
