import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';



export interface StatusElement {

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
  status: string;
  statusId: number;
  updatedBy: string;
  createdOn: string;
}


@Injectable({
  providedIn: 'root'
})
export class StatusService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `status`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `status/` + code);
  }
  Create(obj: StatusElement) {
    return this.http.post<any>(this.apiName + `status`, obj);
  }
  Update(obj: StatusElement, code: any) {
    return this.http.patch<any>(this.apiName + `status` + `?statusId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `status/` + code);
  }
}
