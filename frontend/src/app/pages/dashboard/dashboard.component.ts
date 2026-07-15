import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

// import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { forkJoin } from 'rxjs';

import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { ProgressBarModule } from 'primeng/progressbar';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { TagModule } from 'primeng/tag';

import { RetirementGoal } from '../../types/RetirementGoal';
import { FundingSource } from '../../types/FundingSource';

import { RetirementGoalService } from '../../services/retirement-goal.service';
import { FundingSourceService } from '../../services/FundingSourceService';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    CardModule,
    ButtonModule,
    ProgressBarModule,
    ProgressSpinnerModule,
    TagModule,

  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  goals: RetirementGoal[] = [];
  fundingSources: FundingSource[] = [];

  goalCount = 0;
  fundingSourceCount = 0;
  totalTargetAmount = 0;
  totalContributed = 0;

  loading = true;
  error: string | null = null;

  constructor(
    private goalService: RetirementGoalService,
    private fundingSourceService: FundingSourceService
  ) {}

  ngOnInit() {

    this.loading = true;
    this.error = null;

    forkJoin({
      goals: this.goalService.getRetirementGoals(),
      sources: this.fundingSourceService.getFundingSources()
    }).subscribe({
      next: (result) => {

        this.goals = result.goals;
        this.fundingSources = result.sources;

        this.calculateSummary();

        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load your dashboard data.';
        this.loading = false;
        console.error(err);
      }
    });

  }

  calculateSummary() {

    this.goalCount = this.goals.length;

    this.fundingSourceCount = this.fundingSources.length;

    this.totalTargetAmount = this.goals.reduce(
      (sum, goal) => sum + goal.targetAmount,
      0
    );

    this.totalContributed = this.goals.reduce(
      (sum, goal) => sum + (goal.contributions ?? []).reduce((contribSum, contrib) => contribSum + contrib.amount, 0),
      0
    );

  }

  /** Amount contributed so far toward a single goal. */
  goalContributed(goal: RetirementGoal): number {
    return (goal.contributions ?? []).reduce((sum, c) => sum + c.amount, 0);
  }

  /** Percentage (0-100) of a single goal's target that has been contributed. */
  goalProgress(goal: RetirementGoal): number {
    if (!goal.targetAmount || goal.targetAmount <= 0) {
      return 0;
    }
    return Math.min(100, Math.round((this.goalContributed(goal) / goal.targetAmount) * 100));
  }

  /** Percentage (0-100) of the combined target across all goals that has been contributed. */
  overallProgress(): number {
    if (!this.totalTargetAmount || this.totalTargetAmount <= 0) {
      return 0;
    }
    return Math.min(100, Math.round((this.totalContributed / this.totalTargetAmount) * 100));
    // this only does it by 1 percent at a time, maybe look into more precision
  }

}