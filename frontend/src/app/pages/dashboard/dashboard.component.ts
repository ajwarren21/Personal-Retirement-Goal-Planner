import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { forkJoin } from 'rxjs';

import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';

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
    ButtonModule
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

  constructor(
    private goalService: RetirementGoalService,
    private fundingSourceService: FundingSourceService
  ) {}

  ngOnInit() {

    forkJoin({
      goals: this.goalService.getRetirementGoals(),
      sources: this.fundingSourceService.getFundingSources()
    }).subscribe(result => {

      this.goals = result.goals;
      this.fundingSources = result.sources;

      this.calculateSummary();

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

}