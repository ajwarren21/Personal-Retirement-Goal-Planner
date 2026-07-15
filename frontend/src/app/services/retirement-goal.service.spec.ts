import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { describe, it, expect, beforeEach, afterEach } from 'vitest';

import { RetirementGoalService } from './retirement-goal.service';
import { RetirementGoal, RetirementGoalRequest } from '../types/RetirementGoal';
import { environment } from '../../environments/environments';

describe('RetirementGoalService', () => {


  let service: RetirementGoalService;
  let httpMock: HttpTestingController;

  const URL = `${environment.baseApiUrl}goals`;

  const mockGoal: RetirementGoal = {

    id: 1,
    name: 'Retire at 60',
    targetRetirementAge: 60,
    targetAmount: 1000000,
    notes: 'Some notes',
    contributions: []

  } as unknown as RetirementGoal;

  const mockGoalRequest: RetirementGoalRequest = {
    name: 'Retire at 60',
    targetRetirementAge: 60,
    targetAmount: 1000000,
    notes: 'Some notes'
  } as unknown as RetirementGoalRequest;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        RetirementGoalService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(RetirementGoalService);
    httpMock = TestBed.inject(HttpTestingController);


  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getRetirementGoals', () => {
    it('should GET all retirement goals with Credentials', () => {
      const mockGoals: RetirementGoal[] = [mockGoal];

      service.getRetirementGoals().subscribe((result) => {
        expect(result).toEqual(mockGoals);
      });

      const req = httpMock.expectOne(URL);
      expect(req.request.method).toBe('GET');
      expect(req.request.withCredentials).toBe(true);
      req.flush(mockGoals);
    });

    it('should get a friendly error when the request fails', () => {
      service.getRetirementGoals().subscribe({
        next: () => expect.fail('should not succeed'),
        error: (err) => {
          expect(err.message).toBe('Failed to load Retirement Goals.');
        }
      });

      const req = httpMock.expectOne(URL);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });
  });

  describe('getRetirementGoalById', () => {
    it('should GET a single retirement goal by id', () => {
      service.getRetirementGoalById(1).subscribe((result) => {
        expect(result).toEqual(mockGoal);
      });

      const req = httpMock.expectOne(`${URL}/1`);
      expect(req.request.method).toBe('GET');
      expect(req.request.withCredentials).toBe(true);
      req.flush(mockGoal);
    });

    it('should get a friendly error when the request fails', () => {
      service.getRetirementGoalById(1).subscribe({
        next: () => expect.fail('should not succeed'),
        error: (err) => {
          expect(err.message).toBe('Failed to load Retirement Goals.');
        }
      });

      const req = httpMock.expectOne(`${URL}/1`);
      req.flush('Not found', { status: 404, statusText: 'Not Found' });
    });
  });

  describe('createRetirementGoal', () => {
    it('should POST a new retirement goal', () => {
      service.createRetirementGoal(mockGoalRequest).subscribe((result) => {
        expect(result).toEqual(mockGoal);
      });

      const req = httpMock.expectOne(URL);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockGoalRequest);
      expect(req.request.withCredentials).toBe(true);
      req.flush(mockGoal);
    });

    it('should get a friendly error when creation fails', () => {
      service.createRetirementGoal(mockGoalRequest).subscribe({
        next: () => expect.fail('should not succeed'),
        error: (err) => {
          expect(err.message).toBe('Failed to create Retirement Goal.');
        }
      });

      const req = httpMock.expectOne(URL);
      req.flush('Bad request', { status: 400, statusText: 'Bad Request' });
    });
  });

  describe('updateRetirementGoal', () => {
    it('should PUT an updated retirement goal', () => {
      service.updateRetirementGoal(1, mockGoalRequest).subscribe((result) => {
        expect(result).toEqual(mockGoal);
      });

      const req = httpMock.expectOne(`${URL}/1`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(mockGoalRequest);
      expect(req.request.withCredentials).toBe(true);
      req.flush(mockGoal);
    });

    it('should get a friendly error when update fails', () => {
      service.updateRetirementGoal(1, mockGoalRequest).subscribe({
        next: () => expect.fail('should not succeed'),
        error: (err) => {
          expect(err.message).toBe('Failed to update Retirement Goal.');
        }
      });

      const req = httpMock.expectOne(`${URL}/1`);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });
  });

  describe('deleteRetirementGoal', () => {
    it('should DELETE a retirement goal', () => {
      service.deleteRetirementGoal(1).subscribe((result) => {
        expect(result).toBeNull();
      });

      const req = httpMock.expectOne(`${URL}/1`);
      expect(req.request.method).toBe('DELETE');
      expect(req.request.withCredentials).toBe(true);
      req.flush(null);
    });

    it('should get a friendly error when deletion fails', () => {
      service.deleteRetirementGoal(1).subscribe({
        next: () => expect.fail('should not succeed'),
        error: (err) => {
          expect(err.message).toBe('Failed to delete Retirement Goal.');
        }
      });

      const req = httpMock.expectOne(`${URL}/1`);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });
  });
});