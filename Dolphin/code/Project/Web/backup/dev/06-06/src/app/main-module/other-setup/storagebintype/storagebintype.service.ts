import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class StoragebintypeService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  Getall() {
    return this.http.get<any>('/wms-idmaster-service/storagebintypeid');
  }
  Get(code: string) {
    return this.http.get<any>('/wms-idmaster-service/storagebintypeid' + code);
  }
  Create(obj: any) {
    return this.http.post<any>('/wms-idmaster-service/storagebintypeid', obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>('/wms-idmaster-service/storagebintypeid' + code, obj);
  }
  Delete(storageBinTypeId: any,) {
    return this.http.delete<any>('/wms-idmaster-service/storagebintypeid' + storageBinTypeId );
  }
}

