import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EnglishFeedbackpdfService {

 
  constructor(private http: HttpClient) {
    
  }
  GetFeedback(intakeFormNumber:any){
   return this.http.get<any>('/mnr-crm-service/feedbackform/'+intakeFormNumber)
 }
}
