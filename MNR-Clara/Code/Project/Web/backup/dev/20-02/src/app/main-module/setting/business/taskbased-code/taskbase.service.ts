
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


export interface TaskbaseElement {
  classId: number;
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
  summaryLevel: any;
  taskCode: string;
  taskCodeStatus: string;
  taskcodeDescription: string;
  taskcodeType: string;
  updatedBy: string;
  updatedOn: Date;
}
@Injectable({
  providedIn: 'root'
})
export class TaskbaseService {



  constructor(private http: HttpClient) { }

  apiName = '/mnr-setup-service/';
  method = 'taskbasedCode'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }
  Create(obj: TaskbaseElement) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: TaskbaseElement, code: any) {
    return this.http.patch<any>(this.url + `?taskCode=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + '/' + code);
  }
}
