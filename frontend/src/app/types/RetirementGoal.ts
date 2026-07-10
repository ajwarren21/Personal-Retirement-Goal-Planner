// RetirementGoal.ts
import { Contribution } from "./Contribution";

/** Matches ResponseRetirementGoalDto  */
export interface RetirementGoal {
  id?: number;
  name: string;
  targetRetirementAge: number;
  targetAmount: number;
  contributions: Array<Contribution>;
  notes: string;
}

/** Matches RetirementGoalDto  */
export interface RetirementGoalRequest {
  name: string;
  targetRetirementAge: number;
  targetAmount: number;
  notes: string;
}