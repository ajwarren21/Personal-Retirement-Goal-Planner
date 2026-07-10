import { TestBed } from '@angular/core/testing';

import { RetirementGoalService } from './retirement-goal.service';

describe('RetirementGoalService', () => {
  let service: RetirementGoalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RetirementGoalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
