import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {

  constructor(private http: HttpClient) { }
  getAll(){
    return this.http.get<any>('/iwe-user-service/useraccess'); 
  }
  Get(code: string) {
    return this.http.get<any>(`/iwe-user-service/useraccess/` + code);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>('/iwe-user-service/useraccess/' + code, obj);
  }
  Create(obj: any) {
    return this.http.post<any>(`/iwe-user-service/useraccess`, obj);
  }
  Delete(userId: any) {
    return this.http.delete<any>('/iwe-user-service/useraccess/' + userId );
  }
}
