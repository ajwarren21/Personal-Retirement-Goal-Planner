import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";

import { environment } from "../../environments/environments";

import {
  Contribution,
  ContributionRequest
} from "../types/Contribution";

@Injectable({
  providedIn: "root"
})
export class ContributionService {

  private readonly URL = `${environment.baseApiUrl}contributions`;

  constructor(private http: HttpClient) {}

  private getHttpOptions() {
    return {
      withCredentials: true
    };
  }

  getContributions(): Observable<Contribution[]> {
    return this.http.get<Contribution[]>(
      this.URL,
      this.getHttpOptions()
    ).pipe(
      catchError(() =>
        throwError(() => new Error("Failed to load Contributions."))
      )
    );
  }

  getContributionById(id: number): Observable<Contribution> {
    return this.http.get<Contribution>(
      `${this.URL}/${id}`,
      this.getHttpOptions()
    ).pipe(
      catchError(() =>
        throwError(() => new Error("Failed to load Contribution."))
      )
    );
  }

  createContribution(
    contribution: ContributionRequest
  ): Observable<Contribution> {

    return this.http.post<Contribution>(
      this.URL,
      contribution,
      this.getHttpOptions()
    ).pipe(
      catchError(error => {
        console.error("Full backend error:", error);
        return throwError(() =>
          new Error("Failed to create Contribution.")
        );
      })
    );
  }

  updateContribution(
    id: number,
    contribution: ContributionRequest
  ): Observable<Contribution> {

    return this.http.put<Contribution>(
      `${this.URL}/${id}`,
      contribution,
      this.getHttpOptions()
    ).pipe(
      catchError(() =>
        throwError(() =>
          new Error("Failed to update Contribution.")
        )
      )
    );
  }

  deleteContribution(id: number): Observable<void> {

    return this.http.delete<void>(
      `${this.URL}/${id}`,
      this.getHttpOptions()
    ).pipe(
      catchError(() =>
        throwError(() =>
          new Error("Failed to delete Contribution.")
        )
      )
    );
  }

}