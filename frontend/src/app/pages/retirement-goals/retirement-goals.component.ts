import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { IftaLabelModule } from 'primeng/iftalabel';
import { InputNumberModule } from 'primeng/inputnumber';

import { RetirementGoalService } from '../../services/retirement-goal.service';
import { ContributionService } from '../../services/contribution.service';
import { FundingSourceService } from '../../services/FundingSourceService'; 
import { RetirementGoal } from '../../types/RetirementGoal';
import { Contribution, ContributionRequest, ContributionCategory } from '../../types/Contribution';
import { FundingSource } from '../../types/FundingSource';

@Component({
  selector: 'app-retirement-goals',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    TableModule,
    TagModule,
    ButtonModule,
    ProgressSpinnerModule,
    DialogModule,
    InputTextModule,
    SelectModule,
    TextareaModule,
    IftaLabelModule,
    InputNumberModule
  ],
  templateUrl: './retirement-goals.component.html'
})
export class RetirementGoalsComponent implements OnInit {

  private readonly retirementGoalService = inject(RetirementGoalService);
  private readonly contributionService = inject(ContributionService);
  private readonly fundingSourceService = inject(FundingSourceService);

  // ---- Goal state ----
  retirementGoals = signal<RetirementGoal[]>([]);
  selectedRetirementGoal = signal<RetirementGoal | null>(null);
  showDialog = signal<boolean>(false);
  showDeleteDialog = signal<boolean>(false);
  dialogTitle = signal<string>("");

  loading = signal<boolean>(true);
  error = signal<string | null>(null);

  form!: FormGroup;

  // ---- Contribution state ----
  fundingSources = signal<FundingSource[]>([]);
  showContributionDialog = signal<boolean>(false);
  contributionForm!: FormGroup;
  contributionGoal = signal<RetirementGoal | null>(null); // which goal we're adding a contribution to
  selectedContribution = signal<Contribution | null>(null); // set when editing, null when creating

  categoryOptions = [
    { label: 'Employer Match', value: ContributionCategory.EMPLOYER_MATCH },
    { label: 'Employee Contribution', value: ContributionCategory.EMPLOYER_SALARY_DEFERRAL },
    { label: 'Catch-Up', value: ContributionCategory.CATCH_UP_CONTRIBUTION },
    { label: 'Rollover', value: ContributionCategory.ROLLOVER }
  ];

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.loadRetirementGoals();
    this.loadFundingSources();

    this.form = this.formBuilder.group({
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

  // ---- Goal CRUD  ----

  loadRetirementGoals(): void {
    this.loading.set(true);
    this.error.set(null);

    this.retirementGoalService.getRetirementGoals().subscribe({
      next: (data) => {
        this.retirementGoals.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load retirement goals.');
        this.loading.set(false);
        console.error(err);
      }
    });
  }

  loadFundingSources(): void {
    this.fundingSourceService.getFundingSources().subscribe({
      next: (data) => this.fundingSources.set(data),
      error: (err) => console.error('Failed to load funding sources', err)
    });
  }

  saveRetirementGoal(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { name, targetRetirementAge, targetAmount, notes } = this.form.value;

    const payload: RetirementGoal = {
      name,
      targetRetirementAge,
      targetAmount,
      notes
    };

    if (this.selectedRetirementGoal() === null) {
      this.retirementGoalService.createRetirementGoal(payload).subscribe({
        next: (data) => {
          this.retirementGoals.update((currentList) => [...currentList, data]);
          this.showDialog.set(false);
        },
        error: (err) => {
          console.error(err);
          this.showDialog.set(false);
        }
      });
    } else {
      const id = this.selectedRetirementGoal()!.id!;
      this.retirementGoalService.udpateRetirementGoal(id, payload).subscribe({
        next: (data) => {
          this.retirementGoals.update((currentList) =>
            currentList.map(goal => goal.id === data.id ? data : goal)
          );
          this.showDialog.set(false);
        },
        error: (err) => {
          console.error(err);
          this.showDialog.set(false);
        }
      });
    }
  }

  handleCreateRetirementGoal(): void {
    this.dialogTitle.set("Create Retirement Goal");
    this.selectedRetirementGoal.set(null);

    this.form.setValue({
      name: "",
      targetRetirementAge: 0,
      targetAmount: 0,
      notes: ""
    });

    this.showDialog.set(true);
  }

  handleUpdateRetirementGoal(goal: RetirementGoal): void {
    this.dialogTitle.set("Update Retirement Goal");
    this.selectedRetirementGoal.set(goal);

    this.form.setValue({
      name: goal.name,
      targetRetirementAge: goal.targetRetirementAge,
      targetAmount: goal.targetAmount,
      notes: goal.notes
    });

    this.showDialog.set(true);
  }

  handleDeleteRetirementGoal(goal: RetirementGoal): void {
    this.selectedRetirementGoal.set(goal);
    this.showDeleteDialog.set(true);
  }

  deleteRetirementGoal(): void {
    const id = this.selectedRetirementGoal()!.id!;

    this.retirementGoalService.deleteRetirementGoal(id).subscribe({
      next: () => {
        this.retirementGoals.update((currentList) =>
          currentList.filter(goal => goal.id !== id)
        );
        this.showDeleteDialog.set(false);
      },
      error: (err) => {
        console.error(err);
        this.showDeleteDialog.set(false);
      }
    });
  }

  // ---- Contribution CRUD, scoped to a specific goal ----

  totalContributed(goal: RetirementGoal): number {
    return (goal.contributions ?? []).reduce((sum, c) => sum + c.amount, 0);
  }

  handleAddContribution(goal: RetirementGoal): void {
    this.contributionGoal.set(goal);
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

  handleEditContribution(goal: RetirementGoal, contribution: Contribution): void {
    this.contributionGoal.set(goal);
    this.selectedContribution.set(contribution);

    this.contributionForm.setValue({
      fundingSourceId: contribution.fundingSource.id,
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

    const goal = this.contributionGoal();
    if (!goal?.id) {
      return;
    }

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
      ? this.contributionService.update(existing.id, payload)
      : this.contributionService.create(payload);

    request$.subscribe({
      next: () => {
        this.showContributionDialog.set(false);
        // Simplest correct option: re-fetch goals so the embedded
        // contributions list stays in sync with the backend.
        this.loadRetirementGoals();
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  deleteContribution(contribution: Contribution): void {
    this.contributionService.delete(contribution.id).subscribe({
      next: () => this.loadRetirementGoals(),
      error: (err) => console.error(err)
    });
  }
}