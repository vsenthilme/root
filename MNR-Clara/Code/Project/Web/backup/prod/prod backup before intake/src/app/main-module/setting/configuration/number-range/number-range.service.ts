import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';


export interface NumberRangeElement {

  createdBy: string;
  deletionIndicator: number;
  languageId: string;
  classId: string;
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
  numberRangeCode: number;
  numberRangeCurrent: string;
  numberRangeFrom: string;
  numberRangeObject: string;
  numberRangeStatus: string;
  numberRangeTo: string;
  updatedBy: string;
  updatedOn: string;
  createdOn: string;
}

@Injectable({
  providedIn: 'root'
})
export class NumberRangeService {
  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `numberRange`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `numberRange/` + code);
  }
  Create(obj: NumberRangeElement) {
    return this.http.post<any>(this.apiName + `numberRange`, obj);
  }
  Update(obj: NumberRangeElement, code: any) {
    return this.http.patch<any>(this.apiName + `numberRange?loginUserId=` + this.auth.userID + `&numberRangeId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `numberRange/` + code);
  }
}
