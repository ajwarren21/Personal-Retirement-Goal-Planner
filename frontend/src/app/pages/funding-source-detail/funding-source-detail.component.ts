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
import { TagModule } from 'primeng/tag';

import { FundingSource } from '../../types/FundingSource';
import { Contribution } from '../../types/Contribution';
import { SourceType } from '../../types/SourceType';

import { FundingSourceService } from '../../services/FundingSourceService';
import { ContributionService } from '../../services/contribution.service';

@Component({
  selector: 'app-funding-source-detail',
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
    TagModule
  ],
  templateUrl: './funding-source-detail.component.html',
  styleUrls: ['./funding-source-detail.component.css']
})
export class FundingSourceDetailComponent implements OnInit {

  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fundingSourceService = inject(FundingSourceService);
  private readonly contributionService = inject(ContributionService);
  private readonly formBuilder = inject(FormBuilder);

  fundingSource = signal<FundingSource | null>(null);
  loading = signal<boolean>(true);
  error = signal<string | null>(null);

  // Edit dialog
  showEditDialog = signal<boolean>(false);
  editForm!: FormGroup;

  sourceTypeOptions = [
    { label: 'Traditional 401(k)', value: SourceType.TRADITIONAL_401K },
    { label: 'Roth 401(k)', value: SourceType.ROTH_401K },
    { label: 'Traditional IRA', value: SourceType.TRADITIONAL_IRA },
    { label: 'Roth IRA', value: SourceType.ROTH_IRA },
    { label: 'SEP IRA', value: SourceType.SEP_IRA },
    { label: 'Taxable Brokerage', value: SourceType.TAXABLE_BROKERAGE }
  ];

  ngOnInit(): void {
    this.initializeForm();
    this.loadFundingSource();
  }

  private initializeForm(): void {
    this.editForm = this.formBuilder.group({
      name: ["", [Validators.required, Validators.minLength(2)]],
      sourceType: [null, [Validators.required]],
      institution: ["", [Validators.required]],
      notes: [""]
    });
  }

  loadFundingSource(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (!id) {
        this.error.set('Invalid funding source ID');
        this.loading.set(false);
        return;
      }

      this.fundingSourceService.getFundingSourceById(Number(id)).subscribe({
        next: (data) => {
          this.fundingSource.set(data);
          this.updateEditForm();
          this.loading.set(false);
        },
        error: (err) => {
          this.error.set('Failed to load funding source');
          this.loading.set(false);
          console.error(err);
        }
      });
    });
  }

  private updateEditForm(): void {
    if (!this.fundingSource()) return;
    const source = this.fundingSource()!;
    this.editForm.setValue({
      name: source.name,
      sourceType: source.sourceType,
      institution: source.institution,
      notes: source.notes || ""
    });
  }

  handleEditSource(): void {
    this.showEditDialog.set(true);
  }

  saveSource(): void {
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }

    const sourceId = this.fundingSource()!.id!;
    if (!sourceId) return;

    const payload = this.editForm.value;
    this.fundingSourceService.updateFundingSource(sourceId, payload).subscribe({
      next: (data) => {
        this.fundingSource.set(data);
        this.showEditDialog.set(false);
      },
      error: (err) => {
        console.error(err);
        this.showEditDialog.set(false);
      }
    });
  }

  handleDeleteSource(): void {
    if (!confirm(`Are you sure you want to delete "${this.fundingSource()?.name}"? This action cannot be undone.`)) {
      return;
    }

    const sourceId = this.fundingSource()!.id!;
    if (!sourceId) return;

    this.fundingSourceService.deleteFundingSource(sourceId).subscribe({
      next: () => {
        this.router.navigate(['/funding-sources']);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  getSourceTypeLabel(type: string): string {
    const option = this.sourceTypeOptions.find(o => o.value === type);
    return option ? option.label : type;
  }

  getSourceTypeSeverity(type: string): 'info' | 'success' | 'warn' | 'secondary' {
    switch (type) {
      case 'TRADITIONAL_401K':
      case 'TRADITIONAL_IRA':
        return 'info';
      case 'ROTH_401K':
      case 'ROTH_IRA':
        return 'success';
      case 'SEP_IRA':
        return 'warn';
      case 'TAXABLE_BROKERAGE':
        return 'secondary';
      default:
        return 'secondary';
    }
  }

  totalContributed(): number {
    const source = this.fundingSource();
    if (!source?.contributions) return 0;
    return source.contributions.reduce((sum, c) => sum + Number(c.amount), 0);
  }

  totalGoalsSupported(): Set<number> {
    const source = this.fundingSource();
    if (!source?.contributions) return new Set();
    const goalIds = source.contributions.map(c => c.retirementGoal?.id ?? c.retirementGoalId).filter(id => id !== undefined) as number[];
    return new Set(goalIds);
  }

  goBack(): void {
    this.router.navigate(['/funding-sources']);
  }
}
