import { Contribution } from "./Contribution";

export interface FundingSource {
  id?: number;
  name: string;
  sourceType: string;
  institution: string;
  notes: string;
  contributions?: Array<Contribution>;
}