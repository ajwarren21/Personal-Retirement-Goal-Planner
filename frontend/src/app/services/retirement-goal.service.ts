import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { RetirementGoal } from '../types/RetirementGoal';
import { catchError, Observable, throwError } from "rxjs";
import { environment } from "../../environments/environments";
import { AuthService } from "./auth.service";


@Injectable({providedIn: "root"})
export class RetirementGoalService {


    private readonly URL = `${environment.baseApiUrl}goals`;
    
      constructor(private http: HttpClient) {}
    
    
     private getHttpOptions() {
          return {
        withCredentials: true
      };
      }
    
      getRetirementGoals(): Observable<RetirementGoal[]> {
        return this.http.get<RetirementGoal[]>(this.URL, this.getHttpOptions())
          .pipe(
            catchError(() => throwError(() => new Error("Failed to load Retirement Goals.")))
          );
      }
    
      getRetirementGoalById(id: number): Observable<RetirementGoal> {
        return this.http.get<RetirementGoal>(`${this.URL}/${id}`, this.getHttpOptions())
          .pipe(
            catchError(() => throwError(() => new Error("Failed to load Retirement Goals.")))
          );
      }
    
      createRetirementGoal(retirementGoal: RetirementGoal): Observable<RetirementGoal> {
      return this.http.post<RetirementGoal>(this.URL, retirementGoal, this.getHttpOptions())
        .pipe(
          catchError((error) => {
            console.error('Full backend error details:', error);
            return throwError(() => new Error("Failed to create Retirement Goal."));
          })
        );
    }
    
      updateRetirementGoal(id: number, retirementGoal: RetirementGoal): Observable<RetirementGoal> {
        return this.http.put<RetirementGoal>(`${this.URL}/${id}`, retirementGoal, this.getHttpOptions())
          .pipe(
            catchError(() => throwError(() => new Error("Failed to update Retirement Goal.")))
          );
      }
    
      deleteRetirementGoal(id: number): Observable<void> {
        return this.http.delete<void>(`${this.URL}/${id}`, this.getHttpOptions())
          .pipe(
            catchError(() => throwError(() => new Error("Failed to delete Retirement Goal.")))
          );
      }

}