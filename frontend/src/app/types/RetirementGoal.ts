// import { Contribution } from "./Contribution";
export interface RetirementGoal {
  id?: number;
  name: string;
  targetRetirementAge: number;
  targetAmount: number;
//   contributions: Array<Contribution>;
  notes: string;
}
