import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError } from "rxjs";
import { LoginRequest } from '../types/login-request';
import { environment } from "../environments/environments";
import { AuthResponse } from "../types/auth-response";
import {  Router } from "@angular/router";

@Injectable({providedIn: 'root'})
export class AuthService {


  private readonly URL = `${environment.baseApiUrl}`;

  constructor(private http: HttpClient, private router: Router){}

    // This doesn't actually do anything yet but it will hit this for login
    // also endpoint is not correct yet either
  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.URL}api/auth/login`, credentials)
      .pipe(tap(response => {
        localStorage.setItem('user', JSON.stringify(response));
      })
    );
  }

  register(user: any): Observable<any> {
    return this.http.post(`${this.URL}api/auth/register`, user)
      .pipe(
        catchError(error => {
          console.error('Registration error:', error);
          return throwError(() => new Error('Registration failed. Please try again.'));
        })
      );
  }

  getToken(): string | null {
    const user = localStorage.getItem('user');
    if (!user) {
      return null;
    }
    return JSON.parse(user).token;
  }

  logout(): void {
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
  }






}

