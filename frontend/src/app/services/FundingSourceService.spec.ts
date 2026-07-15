import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { describe, it, expect, beforeEach, afterEach } from 'vitest';

import { FundingSourceService } from './FundingSourceService';
import { FundingSource } from '../types/FundingSource';
import { environment } from '../../environments/environments';

describe('FundingSourceService', () => {


  let service: FundingSourceService;
  let httpMock: HttpTestingController;

  const URL = `${environment.baseApiUrl}funding-source`;

  const mockFundingSource: FundingSource = {

    id: 1,
    name: '401k - Employer',
    type: 'RETIREMENT_ACCOUNT'

  } as unknown as FundingSource;


  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        FundingSourceService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(FundingSourceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getFundingSources', () => {

    it('should GET all funding sources with withCredentials', () => {
      const mockSources: FundingSource[] = [mockFundingSource];

      service.getFundingSources().subscribe((result) => {
        expect(result).toEqual(mockSources);
      });

      const req = httpMock.expectOne(URL);
      expect(req.request.method).toBe('GET');
      expect(req.request.withCredentials).toBe(true);
      req.flush(mockSources);
    });

    it('should get a friendly error when the request fails', () => {
      service.getFundingSources().subscribe({
        next: () => expect.fail('should not succeed'),
        error: (err) => {
          expect(err.message).toBe('Failed to load Funding Sources.');
        }
      });

      const req = httpMock.expectOne(URL);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });
  });

  describe('getFundingSourceById', () => {
    it('should GET a single funding source by id', () => {
      service.getFundingSourceById(1).subscribe((result) => {
        expect(result).toEqual(mockFundingSource);
      });

      const req = httpMock.expectOne(`${URL}/1`);
      expect(req.request.method).toBe('GET');
      expect(req.request.withCredentials).toBe(true);
      req.flush(mockFundingSource);
    });

    it('should get a friendly error when the request fails', () => {
      service.getFundingSourceById(1).subscribe({
        next: () => expect.fail('should not succeed'),
        error: (err) => {
          expect(err.message).toBe('Failed to load Funding Source.');
        }
      });

      const req = httpMock.expectOne(`${URL}/1`);
      req.flush('Not found', { status: 404, statusText: 'Not Found' });
    });
  });

  describe('createFundingSource', () => {
    it('should POST a new funding source', () => {
      service.createFundingSource(mockFundingSource).subscribe((result) => {
        expect(result).toEqual(mockFundingSource);
      });

      const req = httpMock.expectOne(URL);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockFundingSource);
      expect(req.request.withCredentials).toBe(true);
      req.flush(mockFundingSource);
    });

    it('should get a friendly error when creation fails', () => {
      service.createFundingSource(mockFundingSource).subscribe({
        next: () => expect.fail('should not succeed'),
        error: (err) => {
          expect(err.message).toBe('Failed to create Funding Source.');
        }
      });

      const req = httpMock.expectOne(URL);
      req.flush('Bad request', { status: 400, statusText: 'Bad Request' });
    });
  });

  describe('updateFundingSource', () => {

    it('should PUT an updated funding source', () => {
      service.updateFundingSource(1, mockFundingSource).subscribe((result) => {
        expect(result).toEqual(mockFundingSource);
      });

      const req = httpMock.expectOne(`${URL}/1`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(mockFundingSource);
      expect(req.request.withCredentials).toBe(true);
      req.flush(mockFundingSource);
    });

    it('should get a friendly error when update fails', () => {

      service.updateFundingSource(1, mockFundingSource).subscribe({
        next: () => expect.fail('should not succeed'),
        error: (err) => {
          expect(err.message).toBe('Failed to update Funding Source.');
        }
      });

      const req = httpMock.expectOne(`${URL}/1`);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });
  });

  describe('deleteFundingSource', () => {

    it('should DELETE a funding source', () => {
      service.deleteFundingSource(1).subscribe((result) => {
        expect(result).toBeNull();
      });

      const req = httpMock.expectOne(`${URL}/1`);
      expect(req.request.method).toBe('DELETE');
      expect(req.request.withCredentials).toBe(true);
      req.flush(null);
    });

    it('should get a friendly error when deletion fails, including status details', () => {
        
      service.deleteFundingSource(1).subscribe({
        next: () => expect.fail('should not succeed'),
        error: (err) => {
          expect(err.message).toBe('Failed to delete Funding Source.');
        }
      });

      const req = httpMock.expectOne(`${URL}/1`);
      req.flush(
        { message: 'Funding source in use' },
        { status: 409, statusText: 'Conflict' }
      );
    });
  });
});