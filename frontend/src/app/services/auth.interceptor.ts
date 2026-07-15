// import { HttpInterceptorFn } from '@angular/common/http';

// export const authInterceptor: HttpInterceptorFn = (req, next) => {

//   const token = localStorage.getItem('token');

//   const xsrfToken = document.cookie
//     .split('; ')
//     .find(row => row.startsWith('XSRF-TOKEN='))
//     ?.split('=')[1];

//   let headers: any = {};

//   if (token) {
//     headers.Authorization = token;
//   }

//   if (xsrfToken) {
//     headers['X-XSRF-TOKEN'] = xsrfToken;
//   }

//   const clonedRequest = req.clone({
//     setHeaders: headers,
//     withCredentials: true
//   });

//   return next(clonedRequest);
// };
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { CsrfService } from './csrf-service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const csrfService = inject(CsrfService);
  const token = localStorage.getItem('token');

  let headers: any = {};

  if (token) {
    headers.Authorization = token;
  }

  const csrfToken = csrfService.getToken();
  if (csrfToken) {
    headers[csrfService.getHeaderName()] = csrfToken;
  }

  const clonedRequest = req.clone({
    setHeaders: headers,
    withCredentials: true
  });

  return next(clonedRequest);
};