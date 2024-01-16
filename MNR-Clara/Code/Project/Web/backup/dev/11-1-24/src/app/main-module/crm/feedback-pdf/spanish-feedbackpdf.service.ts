import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SpanishFeedbackpdfService {

  constructor(private http: HttpClient) {}
  
   GetFeedback(intakeFormNumber:any){
    return this.http.get<any>('/mnr-crm-service/feedbackform/'+ intakeFormNumber)
  }

  smsFeedBack(intakeFormNumber:any, obj: any){
    return this.http.post<any>('/mnr-crm-service/feedbackform/'+ intakeFormNumber+ '/feedback/sms', obj)
  }
}
