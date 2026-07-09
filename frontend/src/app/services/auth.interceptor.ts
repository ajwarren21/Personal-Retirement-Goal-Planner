import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const token = localStorage.getItem('token');

  const xsrfToken = document.cookie
    .split('; ')
    .find(row => row.startsWith('XSRF-TOKEN='))
    ?.split('=')[1];

  let headers: any = {};

  if (token) {
    headers.Authorization = token;
  }

  if (xsrfToken) {
    headers['X-XSRF-TOKEN'] = xsrfToken;
  }

  const clonedRequest = req.clone({
    setHeaders: headers,
    withCredentials: true
  });

  return next(clonedRequest);
};