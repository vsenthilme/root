import { AuthService } from './Auth/auth.service';
import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpParams,
  HttpHeaders
} from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService) { }
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  private bearerToken: any;
  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {

    return next.handle(request).pipe(catchError(error => {
      if (error.status === 401 || error.status == 400 || error.status == 415) {
        if (request.url.includes('/auth-token') || (error.error != null && error.error.error.includes("Order is not completely Processed"))) {
          return throwError(error);
        }
        return this.handle401Error(request, next, error);
      }
      return throwError(error);
    }));
  }



  private handle401Error(request: HttpRequest<any>, next: HttpHandler, error: any) {


    this.isRefreshing = true;
    this.refreshTokenSubject.next(null);
    ;
    const token = this.auth.token;

    let apiName = "";
    // if (request.url.includes('mnr-management-service'))
    //   apiName = request.url.split('/')[5];
    // else 
    //apiName = request.url.split('/')[4];
    // if (request.url.includes('/iwe-integration-service/login') || request.url.includes('/iwe-integration-service/softdata/')) {
       apiName = 'b2b-portal-service';
    // }
    // else {
    //   apiName = 'b2b-integration-service';
    // }

    return this.auth.refreshToken(apiName).pipe(
      switchMap((token: any) => {

        this.auth.saveToken(token.access_token, apiName);
        this.refreshTokenSubject.next(token.access_token);


        return next.handle(this.addTokenHeader(request, token.access_token));
      }),
      catchError((err) => {

        if (err.status == 401) {

          this.auth.logout();
        }
        return throwError(err);
      })
    );
  }
  private addTokenHeader(request: HttpRequest<any>, token: string) {
    if (request.url.includes('/iwe-integration-service/login'))
      return request.clone({headers: request.headers.set("authToken", token) });  

      if (request.url.includes('/iwe-integration-service/softdata'))
      {
         return request.clone({headers: request.headers.set("authToken", token) });
      }

      if (request.url.includes('/iwe-integration-service/dashboard/getDashboardCount'))
      {
         return request.clone({headers: request.headers.set("authToken", token) });
      }


      if (request.url.includes('/iwe-integration-service/tracking/'))
      {
         return request.clone({headers: request.headers.set("Authorization", "Basic $2a$10$qWHNeBhu4FCGoJfuv2XVbO9Yq4QBUwGSvNM0bGpYUVc3iY8jXsJwO") });
      }

    return request.clone({ params: new HttpParams() });   //this.auth.userID
  }

}


