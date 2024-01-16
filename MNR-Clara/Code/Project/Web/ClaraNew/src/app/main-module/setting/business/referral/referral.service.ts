import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';


export interface ReferralElement {
  caseCategoryId: number;
  referralDescription: string;
  referralStatus: string;
  subReferralDescription: number;
  classId: number;
  referralId: number;
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  subReferralId: number
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
  classIddes: string;
}
@Injectable({
  providedIn: 'root'
})
export class ReferralService {


  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `referral`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `referral/` + code);
  }
  Create(obj: ReferralElement) {
    return this.http.post<any>(this.apiName + `referral`, obj);
  }
  Update(obj: ReferralElement, code: any) {
    return this.http.patch<any>(this.apiName + `referral` + `?referralId=` + code + '&classId=' + obj.classId + '&languageId=' + obj.languageId, obj);
  }
  Delete(code: any, classId: any, languageId: any) {
    return this.http.delete<any>(this.apiName + `referral/` + code + '?classId=' + classId + '&languageId=' + languageId);
  }
}
