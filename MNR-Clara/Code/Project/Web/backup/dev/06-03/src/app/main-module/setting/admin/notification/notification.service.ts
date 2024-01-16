import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';


export interface NotificationElement {



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
  notificationId: number;
  notificationText: string;
  classId: number;
  transactionId: number;
  userId: string;
  notificationDescription: string;
  updatedBy: string;
  createdOn: string;


  classIddes: string;
  transactionIddes: string;

}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `notification`);
  }
  Get(code: string, classId: any, languageId: any, transactionId: any, userId: any) {
    return this.http.get<any>(this.apiName + `notification/` + code + '?classId=' + classId + '&languageId=' + languageId + '&transactionId=' + transactionId + '&userId=' + userId);
  }
  Create(obj: NotificationElement) {
    return this.http.post<any>(this.apiName + `notification`, obj);
  }
  Update(obj: NotificationElement, code: any, classId: any, languageId: any, transactionId: any, userId: any) {
    return this.http.patch<any>(this.apiName + `notification` + `?notificationId=` + code + '&classId=' + classId + '&languageId=' + languageId + '&transactionId=' + transactionId + '&userId=' + userId, obj);
  }
  Delete(code: any, classId: any, languageId: any, transactionId: any, userId: any) {
    return this.http.delete<any>(this.apiName + `notification/` + code + '?classId=' + classId + '&languageId=' + languageId + '&transactionId=' + transactionId + '&userId=' + userId);
  }
}
