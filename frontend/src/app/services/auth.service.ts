import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError } from "rxjs";
import { LoginRequest } from '../types/login-request';
import { environment } from '../../environments/environments';
import { Router } from '@angular/router';
import { User } from '../types/user';
import { CsrfService } from "./csrf-service";

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly URL = `${environment.baseApiUrl}`;

  constructor(private http: HttpClient, private router: Router, private csrfService: CsrfService) {}

  // initCsrf(): Observable<any> {
  //   return this.http.get<any>(`${this.URL}api/auth/csrf`, { withCredentials: true })
  //     .pipe(
  //       tap(res => {
  //         this.csrfService.setToken(res.headerName, res.token)
  //       })
  //     );
  // }

  login(credentials: LoginRequest): Observable<any> {
    const authHeader = 'Basic ' + btoa(`${credentials.username}:${credentials.password}`);
    
    const headers = new HttpHeaders({
      'Authorization': authHeader
    });

    return this.http.post<any>(`${this.URL}api/auth/login`, {}, { 
      headers, 
      withCredentials: true
    })
    .pipe(
      tap(userProfile => {
        localStorage.setItem('user', JSON.stringify(userProfile));
        localStorage.setItem('token', authHeader); 
      
        console.log('Successfully saved auth token to localStorage:', authHeader);
      }),
      catchError(error => {
        console.error('Login error:', error);
        return throwError(() => new Error('Invalid username or password.'));
      })
    );
  }

  register(user: any): Observable<any> {
   

    return this.http.post(`${this.URL}api/auth/register`, user, {
  
      withCredentials: true 
    })
    .pipe(
      catchError(error => {
        console.error('Registration error:', error);
        return throwError(() => new Error('Registration failed. Username may already exist.'));
      })
    );
  }

  getUser(): User | null {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }

  updateUser(payload: Pick<User, 'username' | 'email'>): Observable<User> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': token ?? ''
    });

    return this.http.put<User>(`${this.URL}api/auth/user`, payload, {
      headers,
      withCredentials: true
    })
    .pipe(
      tap(updatedUser => {
        localStorage.setItem('user', JSON.stringify(updatedUser));

        
        if (token) {
          const decoded = atob(token.replace('Basic ', ''));
          const password = decoded.substring(decoded.indexOf(':') + 1);
          const newToken = 'Basic ' + btoa(`${updatedUser.username}:${password}`);
          localStorage.setItem('token', newToken);
        }
      }),
      catchError(error => {
        console.error('Update user error:', error);
        return throwError(() => new Error('Unable to save profile changes.'));
      })
    );
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('user') !== null;
  }

  logout(): void {
    // need to remove token the route to the login page
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}