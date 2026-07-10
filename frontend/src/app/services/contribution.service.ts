import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Contribution, ContributionRequest } from '../types/Contribution';
import { environment } from "../../environments/environments";
import { AuthService } from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class ContributionService {

    private readonly baseUrl = `${environment.baseApiUrl}contribution`;


    constructor(private http: HttpClient, private authService: AuthService){}

    getAll(): Observable<Contribution[]> {
    return this.http.get<Contribution[]>(this.baseUrl);
    }

    getById(id: number): Observable<Contribution> {
    return this.http.get<Contribution>(`${this.baseUrl}/${id}`);
    }

    create(dto: ContributionRequest): Observable<Contribution> {
    return this.http.post<Contribution>(this.baseUrl, dto);
    }

    update(id: number, dto: ContributionRequest): Observable<Contribution> {
    return this.http.put<Contribution>(`${this.baseUrl}/${id}`, dto);
    }

    delete(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
    }
}
