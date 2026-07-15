import { FundingSource } from './FundingSource';
import { RetirementGoal } from './RetirementGoal';

export enum ContributionCategory {
  EMPLOYER_SALARY_DEFERRAL = 'EMPLOYEE_SALARY_DEFERRAL',
  EMPLOYER_MATCH = 'EMPLOYER_MATCH',
  CATCH_UP_CONTRIBUTION = 'CATCH_UP_CONTRIBUTION',
  ROLLOVER = 'ROLLOVER',
}

/**
 * Mirrors ResponseContributionDto coming back from the backend.
 */
export interface Contribution {
  id: number;
  // Detailed nested objects (may be absent if backend returns flattened DTO)
  fundingSource?: FundingSource;
  retirementGoal?: RetirementGoal;

  // Flattened fields returned by backend mappers
  fundingSourceId?: number;
  fundingSourceName?: string;
  retirementGoalId?: number;
  retirementGoalName?: string;

  amount: number;
  contributionDate: string; // ISO date string like "2026-07-09"
  category: ContributionCategory;
  notes?: string;
}

/**
 * Mirrors ContributionDto
 */
export interface ContributionRequest {
  retirementGoalId: number;
  fundingSourceId: number;
  amount: number;
  contributionDate: string;
  category: ContributionCategory;
  notes?: string;
}