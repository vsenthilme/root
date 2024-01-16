import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';



export interface MessageElement {

  classId: number;
  createdBy: string;
  deletionIndicator: number;
  languageId: string;
  messageDescription: string;
  messageId: number;
  messageType: string;
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
}


@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `message`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `message/` + code);
  }
  Create(obj: MessageElement) {
    return this.http.post<any>(this.apiName + `message`, obj);
  }
  Update(obj: MessageElement, code: any) {
    return this.http.patch<any>(this.apiName + `message` + `?messageId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `message/` + code);
  }
}
