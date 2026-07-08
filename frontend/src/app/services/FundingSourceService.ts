import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { FundingSource } from "../types/FundingSource";
import { catchError, Observable, throwError } from "rxjs";
import { environment } from "../../environments/environments";

@Injectable({ providedIn: "root" })
export class FundingSourceService {

  private readonly URL = `${environment.baseApiUrl}api/funding-source`;

  constructor(private http: HttpClient) {}


  private readonly httpOptions = {
    withCredentials: true
  };

  getFundingSources(): Observable<FundingSource[]> {
    return this.http.get<FundingSource[]>(this.URL, this.httpOptions)
      .pipe(
        catchError(() => throwError(() => new Error("Failed to load Funding Sources.")))
      );
  }

  getFundingSourceById(id: number): Observable<FundingSource> {
    return this.http.get<FundingSource>(`${this.URL}/${id}`, this.httpOptions)
      .pipe(
        catchError(() => throwError(() => new Error("Failed to load Funding Source.")))
      );
  }

  createFundingSource(fundingSource: FundingSource): Observable<FundingSource> {
    return this.http.post<FundingSource>(this.URL, fundingSource, this.httpOptions)
      .pipe(
        catchError(() => throwError(() => new Error("Failed to create Funding Source.")))
      );
  }

  udpateFundingSource(id: number, fundingSource: FundingSource): Observable<FundingSource> {
    return this.http.put<FundingSource>(`${this.URL}/${id}`, fundingSource, this.httpOptions)
      .pipe(
        catchError(() => throwError(() => new Error("Failed to update Funding Source.")))
      );
  }

  deleteFundingSource(id: number): Observable<void> {
    return this.http.delete<void>(`${this.URL}/${id}`, this.httpOptions)
      .pipe(
        catchError(() => throwError(() => new Error("Failed to delete Funding Source.")))
      );
  }
}