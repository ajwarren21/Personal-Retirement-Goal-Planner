import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { FundingSource } from "../types/FundingSource";
import { catchError, Observable, throwError } from "rxjs";
import { environment } from "../../environments/environments";
import { AuthService } from "./auth.service";


@Injectable({providedIn: "root"})
export class FundingSourceService {


    private readonly URL = `${environment.baseApiUrl}funding-source`;


    constructor(private http: HttpClient, private authService: AuthService){}


    
    getFundingSources(): Observable<FundingSource[]> {

        const token = this.authService.getToken();

        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });

        return this.http.get<FundingSource[]>(this.URL, { headers })
            .pipe(
                catchError(
                    () => throwError(
                        () => new Error("Failed to load Funding Sources.")
                    )
                )
            );
    }

    getFundingSourceById(id: number): Observable<FundingSource> {
         const token = this.authService.getToken();

        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });
        return this.http.get<FundingSource>(this.URL + `/${id}`, { headers })
            .pipe(
                catchError(
                    () => throwError(
                        () => new Error("Failed to load Funding Source.")
                    )
                )
            );
    }

    createFundingSource(fundingSource: FundingSource): Observable<FundingSource> {
         const token = this.authService.getToken();

        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });
        return this.http.post<FundingSource>(this.URL, fundingSource, { headers })
            .pipe(
                catchError(
                    () => throwError(
                        () => new Error("Failed to create Funding Source.")
                    )
                )
            );
    }

    udpateFundingSource(id: number, fundingSource: FundingSource): Observable<FundingSource> {
         const token = this.authService.getToken();

        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });
        return this.http.put<FundingSource>(this.URL + `/${id}`, fundingSource, { headers })
            .pipe(
                catchError(
                    () => throwError(
                        () => new Error("Failed to update Funding Source.")
                    )
                )
            );
    }

    deleteFundingSource(id: number): Observable<void> {
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