import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthService } from './core';

@Injectable()
export class CommonInterceptor implements HttpInterceptor {
  cur: any;
  constructor(private authService: AuthService) { }
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq;
    let url = environment.apiURL;
    const bearerToken = this.authService.getToken(req.url.split('/')[1]);//sessionStorage.getItem('token');
    ;
    if (bearerToken) {

      if (req.url.split('/')[1] == 'mv-master-service' && req.url.split('/')[2] == 'doc-storage' &&  req.method == 'GET') {
        authReq = req.clone({
          url: `${url}${req.url}`,
         params: new HttpParams()
        });

      }
      else if (req.url.split('/')[1] == 'mv-master-service' && req.url.split('/')[2] == 'doc-storage' && req.method == 'POST') {
        authReq = req.clone({
          url: `${url}${req.url}`,
          params: new HttpParams()
        });

      }
      else {
        authReq = req.clone({
          headers: req.headers.set('Content-Type', 'application/json').set('Accept', 'application/json'),
          url: `${url}${req.url}`,
    //    params: new HttpParams().set("authToken", bearerToken).set('loginUserID', this.authService.userID)
          params: new HttpParams().set("authToken", bearerToken).set('loginUserID', 'Admin')
        });

      }
    }
    else {
      authReq = req.clone({
        url: `${url}${req.url}`
      });

    }
    return next.handle(authReq);
  }
}
