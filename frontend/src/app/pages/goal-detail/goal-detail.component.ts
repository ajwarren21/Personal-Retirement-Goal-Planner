import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { IftaLabelModule } from 'primeng/iftalabel';
import { InputNumberModule } from 'primeng/inputnumber';
import { TagModule } from 'primeng/tag';

import { RetirementGoal, RetirementGoalRequest } from '../../types/RetirementGoal';
import { Contribution, ContributionRequest, ContributionCategory } from '../../types/Contribution';
import { FundingSource } from '../../types/FundingSource';

import { RetirementGoalService } from '../../services/retirement-goal.service';
import { ContributionService } from '../../services/contribution.service';
import { FundingSourceService } from '../../services/FundingSourceService';

@Component({
  selector: 'app-goal-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    CardModule,
    ButtonModule,
    TableModule,
    ProgressSpinnerModule,
    DialogModule,
    InputTextModule,
    SelectModule,
    TextareaModule,
    IftaLabelModule,
    InputNumberModule,
    TagModule
  ],
  templateUrl: './goal-detail.component.html',
  styleUrls: ['./goal-detail.component.css']
})
export class GoalDetailComponent implements OnInit {

  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly goalService = inject(RetirementGoalService);
  private readonly contributionService = inject(ContributionService);
  private readonly fundingSourceService = inject(FundingSourceService);
  private readonly formBuilder = inject(FormBuilder);

  goal = signal<RetirementGoal | null>(null);
  fundingSources = signal<FundingSource[]>([]);
  loading = signal<boolean>(true);
  error = signal<string | null>(null);

  // Edit goal dialog
  showEditDialog = signal<boolean>(false);
  editForm!: FormGroup;

  // Contribution dialog
  showContributionDialog = signal<boolean>(false);
  contributionForm!: FormGroup;
  selectedContribution = signal<Contribution | null>(null);

  categoryOptions = [
    { label: 'Employer Match', value: ContributionCategory.EMPLOYER_MATCH },
    { label: 'Employee Contribution', value: ContributionCategory.EMPLOYER_SALARY_DEFERRAL },
    { label: 'Catch-Up', value: ContributionCategory.CATCH_UP_CONTRIBUTION },
    { label: 'Rollover', value: ContributionCategory.ROLLOVER }
  ];

  ngOnInit(): void {
    this.initializeForms();
    this.loadGoal();
    this.loadFundingSources();
  }

  private initializeForms(): void {
    this.editForm = this.formBuilder.group({
      name: ["", [Validators.required, Validators.minLength(2)]],
      targetRetirementAge: [0, [Validators.required]],
      targetAmount: [0, [Validators.required]],
      notes: [""]
    });

    this.contributionForm = this.formBuilder.group({
      fundingSourceId: [null, [Validators.required]],
      amount: [0, [Validators.required, Validators.min(0.01)]],
      contributionDate: [this.today(), [Validators.required]],
      category: [null, [Validators.required]],
      notes: [""]
    });
  }

  private today(): string {
    return new Date().toISOString().substring(0, 10);
  }

  loadGoal(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (!id) {
        this.error.set('Invalid goal ID');
        this.loading.set(false);
        return;
      }

      this.goalService.getRetirementGoalById(Number(id)).subscribe({
        next: (data) => {
          this.goal.set(data);
          this.updateEditForm();
          this.loading.set(false);
        },
        error: (err) => {
          this.error.set('Failed to load retirement goal');
          this.loading.set(false);
          console.error(err);
        }
      });
    });
  }

  loadFundingSources(): void {
    this.fundingSourceService.getFundingSources().subscribe({
      next: (data) => this.fundingSources.set(data),
      error: (err) => console.error('Failed to load funding sources', err)
    });
  }

  private updateEditForm(): void {
    if (!this.goal()) return;
    const g = this.goal()!;
    this.editForm.setValue({
      name: g.name,
      targetRetirementAge: g.targetRetirementAge,
      targetAmount: g.targetAmount,
      notes: g.notes || ""
    });
  }

  handleEditGoal(): void {
    this.showEditDialog.set(true);
  }

  saveGoal(): void {
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }

    const goalId = this.goal()?.id;
    if (!goalId) return;

    const { name, targetRetirementAge, targetAmount, notes } = this.editForm.value;
    const payload: RetirementGoalRequest = {
      name,
      targetRetirementAge,
      targetAmount,
      notes
    };

    this.goalService.updateRetirementGoal(goalId, payload).subscribe({
      next: (data) => {
        this.goal.set(data);
        this.showEditDialog.set(false);
      },
      error: (err) => {
        console.error(err);
        this.showEditDialog.set(false);
      }
    });
  }

  handleDeleteGoal(): void {
    if (!confirm(`Are you sure you want to delete "${this.goal()?.name}"? This action cannot be undone.`)) {
      return;
    }

    const goalId = this.goal()?.id;
    if (!goalId) return;

    this.goalService.deleteRetirementGoal(goalId).subscribe({
      next: () => {
        this.router.navigate(['/retirement-goals']);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  totalContributed(): number {
    return (this.goal()?.contributions ?? []).reduce((sum, c) => sum + Number(c.amount), 0);
  }

  percentageComplete(): number {
    const goal = this.goal();
    if (!goal) return 0;
    const total = this.totalContributed();
    const target = Number(goal.targetAmount);
    return target > 0 ? Math.min((total / target) * 100, 100) : 0;
  }

  yearsToRetirement(): number {
    const goal = this.goal();
    if (!goal) return 0;
    const currentYear = new Date().getFullYear();
    const yearsOld = currentYear - 1990; // Rough estimate, could be calculated from birth date if available
    return Math.max(0, goal.targetRetirementAge - yearsOld);
  }

  handleAddContribution(): void {
    this.selectedContribution.set(null);
    this.contributionForm.setValue({
      fundingSourceId: null,
      amount: 0,
      contributionDate: this.today(),
      category: null,
      notes: ""
    });
    this.showContributionDialog.set(true);
  }

  handleEditContribution(contribution: Contribution): void {
    this.selectedContribution.set(contribution);
    const fundingSourceId = contribution.fundingSource?.id ?? contribution.fundingSourceId ?? null;

    this.contributionForm.setValue({
      fundingSourceId: fundingSourceId,
      amount: contribution.amount,
      contributionDate: contribution.contributionDate,
      category: contribution.category,
      notes: contribution.notes ?? ""
    });
    this.showContributionDialog.set(true);
  }

  saveContribution(): void {
    if (this.contributionForm.invalid) {
      this.contributionForm.markAllAsTouched();
      return;
    }

    const goal = this.goal();
    if (!goal?.id) return;

    const { fundingSourceId, amount, contributionDate, category, notes } = this.contributionForm.value;
    const payload: ContributionRequest = {
      retirementGoalId: goal.id,
      fundingSourceId,
      amount,
      contributionDate,
      category,
      notes
    };

    const existing = this.selectedContribution();
    const request$ = existing
      ? this.contributionService.updateContribution(existing.id, payload)
      : this.contributionService.createContribution(payload);

    request$.subscribe({
      next: () => {
        this.showContributionDialog.set(false);
        this.loadGoal();
      },
      error: (err) => console.error(err)
    });
  }

  deleteContribution(contribution: Contribution): void {
    this.contributionService.deleteContribution(contribution.id).subscribe({
      next: () => this.loadGoal(),
      error: (err) => console.error(err)
    });
  }

  goBack(): void {
    this.router.navigate(['/retirement-goals']);
  }
}
