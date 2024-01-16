import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class FeedbackService {
  constructor(private http: HttpClient) { }

  url = '/mnr-crm-service/feedbackform/';
  Get(itFormNo: any) {
    return this.http.get<any>(this.url + itFormNo );
  }
  
  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
}
