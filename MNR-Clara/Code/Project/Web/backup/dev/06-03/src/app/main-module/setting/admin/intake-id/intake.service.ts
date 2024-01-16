import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';


export interface IntakeElement {

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
  updatedBy: string;
  createdOn: string;
  classId: string,
  clientTypeId: string,
  intakeFormDescription: string,
  intakeFormId: string,
  updatedOn: string
}

@Injectable({
  providedIn: 'root'
})
export class IntakeService {
  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `intakeFormId`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `intakeFormId/` + code);
  }
  Create(obj: IntakeElement) {
    return this.http.post<any>(this.apiName + `intakeFormId`, obj);
  }
  Update(obj: IntakeElement, code: any) {
    return this.http.patch<any>(this.apiName + `intakeFormId` + `?intakeFormIdId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `intakeFormId/` + code);
  }
}
