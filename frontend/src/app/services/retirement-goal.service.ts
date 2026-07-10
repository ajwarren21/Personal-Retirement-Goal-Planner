import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { RetirementGoal } from '../types/RetirementGoal';
import { catchError, Observable, throwError } from "rxjs";
import { environment } from "../../environments/environments";
import { AuthService } from "./auth.service";


@Injectable({providedIn: "root"})
export class RetirementGoalService {


    private readonly URL = `${environment.baseApiUrl}funding-source`;


    constructor(private http: HttpClient, private authService: AuthService){}


    
    getRetirementGoals(): Observable<RetirementGoal[]> {

        const token = this.authService.getToken();

        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });

        return this.http.get<RetirementGoal[]>(this.URL, { headers })
            .pipe(
                catchError(
                    () => throwError(
                        () => new Error("Failed to load Funding Sources.")
                    )
                )
            );
    }

    getRetirementGoalById(id: number): Observable<RetirementGoal> {
         const token = this.authService.getToken();

        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });
        return this.http.get<RetirementGoal>(this.URL + `/${id}`, { headers })
            .pipe(
                catchError(
                    () => throwError(
                        () => new Error("Failed to load Funding Source.")
                    )
                )
            );
    }

    createRetirementGoal(retirementGoal: RetirementGoal): Observable<RetirementGoal> {
         const token = this.authService.getToken();

        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });
        return this.http.post<RetirementGoal>(this.URL, retirementGoal, { headers })
            .pipe(
                catchError(
                    () => throwError(
                        () => new Error("Failed to create Funding Source.")
                    )
                )
            );
    }

    udpateRetirementGoal(id: number, retirementGoal: RetirementGoal): Observable<RetirementGoal> {
         const token = this.authService.getToken();

        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });
        return this.http.put<RetirementGoal>(this.URL + `/${id}`, retirementGoal, { headers })
            .pipe(
                catchError(
                    () => throwError(
                        () => new Error("Failed to update Funding Source.")
                    )
                )
            );
    }

    deleteRetirementGoal(id: number): Observable<void> {
         const token = this.authService.getToken();

        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });
        return this.http.delete<void>(this.URL + `/${id}`, { headers })
            .pipe(
                catchError(
                    () => throwError(
                        () => new Error("Failed to delete Funding Source.")
                    )
                )
            );
    }

}