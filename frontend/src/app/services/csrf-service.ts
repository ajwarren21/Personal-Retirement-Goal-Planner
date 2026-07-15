import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class CsrfService {
  private token: string | null = null;
  private headerName: string = 'X-XSRF-TOKEN';

  setToken(headerName: string, token: string) {
    this.headerName = headerName;
    this.token = token;
  }

  getToken() {
    return this.token;
  }

  getHeaderName() {
    return this.headerName;
  }
}