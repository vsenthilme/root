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

    // if (req.url.includes('mnr-management-service'))
    //   url = environment.apiURL_management_service;
    if (bearerToken) {
      // if (req.method != 'get') {
      // if (req.url.includes('mnr-management-service'))
      //   authReq = req.clone({
      //     url: `${url}${req.url}`,
      //     setHeaders: { Authorization: 'Bearer ' + `${bearerToken}` },
      //   });
      // else
      // authReq = req.clone({
      //   url: `${url}${req.url}`,
      //   params: new HttpParams().set("authToken", bearerToken)
      // });
      // }
      // else {
      //   // if (req.url.includes('mnr-management-service'))
      //   //   authReq = req.clone({
      //   //     url: `${url}${req.url}`, params: new HttpParams().set('loginUserID', this.authService.userID),
      //   //     setHeaders: { Authorization: 'Bearer ' + `${bearerToken}` },
      //   //   });
      //   // else
      //   authReq = req.clone({
      //     url: `${url}${req.url}`,
      //     params: new HttpParams().set("authToken", bearerToken).set('loginUserID', this.authService.userID)
      //   });
      //}

      // else
      // if (req.method != 'get')
      //   authReq = req.clone({
      //     url: `${this.url}${req.url}`,
      //     params: new HttpParams().set("authToken", bearerToken).set('loginUserID', this.authService.userID)
      //   });
      // else
      //   authReq = req.clone({
      //     url: `${this.url}${req.url}`,
      //     params: new HttpParams().set("authToken", bearerToken)
      //   });

      authReq = req.clone({
        url: `${url}${req.url}`,
        params: new HttpParams().set("authToken", bearerToken).set('loginUserID', this.authService.userID)
      });
    }
    else {
      authReq = req.clone({
        url: `${url}${req.url}`
      });
    }
    return next.handle(authReq);
  }
}
