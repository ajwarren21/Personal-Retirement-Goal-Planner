import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import { LoginRequest } from '../types/login-request';
import { environment } from "../environments/environments";
import { Router } from '@angular/router';

@Injectable({providedIn: 'root'})
export class AuthService {


  private readonly URL = `${environment.baseApiUrl}`;

  constructor(private http: HttpClient, private router: Router){}

    // This doesn't actually do anything yet but it will hit this for login
    // also endpoint is not correct yet either
  login(credentials: LoginRequest): Observable<any> {
    return this.http.post(`${this.URL}/auth/login`, credentials);
  }

  logout(): void {
    // need to remove token the route to the login page
    this.router.navigate(['/login']);
  }
}

