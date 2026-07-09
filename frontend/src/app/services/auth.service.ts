import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError } from "rxjs";
import { LoginRequest } from '../types/login-request';
<<<<<<< HEAD
import { environment } from "../../environments/environments";
import { Router } from "@angular/router";
=======
import { environment } from "../environments/environments";
import { Router } from '@angular/router';
import {User} from '../types/user';
>>>>>>> 9b26f8bd217f6fdfeb1a57b931bf10efdd6885cd

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly URL = `${environment.baseApiUrl}`;
  private csrfToken: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  initCsrf(): Observable<any> {
    return this.http.get<any>(`${this.URL}api/auth/csrf`, { withCredentials: true })
      .pipe(
        tap(res => {
          if (res && res.token) {
            this.csrfToken = res.token;
          }
        })
      );
  }

  login(credentials: LoginRequest): Observable<any> {
    const authHeader = 'Basic ' + btoa(`${credentials.username}:${credentials.password}`);
    
    const headers = new HttpHeaders({
      'Authorization': authHeader,
      'X-XSRF-TOKEN': this.csrfToken
    });

    return this.http.post<any>(`${this.URL}api/auth/login`, {}, { 
      headers, 
      withCredentials: true
    })
    .pipe(
      tap(userProfile => {
        localStorage.setItem('user', JSON.stringify(userProfile));
      }),
      catchError(error => {
        console.error('Login error:', error);
        return throwError(() => new Error('Invalid username or password.'));
      })
    );
  }

<<<<<<< HEAD
  register(user: any): Observable<any> {
    const headers = new HttpHeaders({
      'X-XSRF-TOKEN': this.csrfToken
    });

    return this.http.post(`${this.URL}api/auth/register`, user, {
      headers,
      withCredentials: true 
    })
    .pipe(
      catchError(error => {
        console.error('Registration error:', error);
        return throwError(() => new Error('Registration failed. Username may already exist.'));
      })
    );
  }

  getUser(): any | null {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('user') !== null;
  }

=======
>>>>>>> 9b26f8bd217f6fdfeb1a57b931bf10efdd6885cd
  logout(): void {
    // need to remove token the route to the login page
    this.router.navigate(['/login']);
  }
<<<<<<< HEAD
}
=======

  // Will hit the backend authservice /auth/me, but it isn't working for now
  // getCurrentUser(): User {}

  // temp get user by id 1
  getUser(): Observable<any> {
    return this.http.get(`${this.URL}/users/1`);
  }
}

>>>>>>> 9b26f8bd217f6fdfeb1a57b931bf10efdd6885cd
