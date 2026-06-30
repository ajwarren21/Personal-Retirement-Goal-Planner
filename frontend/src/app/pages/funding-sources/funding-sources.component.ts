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
import { FundingSourceService } from '../../services/FundingSourceService';
import { FundingSource } from '../../types/FundingSource';
import { SourceType } from '../../types/SourceType';

@Component({
  selector: 'app-funding-sources',
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
    IftaLabelModule
  ],
  templateUrl: './funding-sources.component.html'
})
export class FundingSourcesComponent implements OnInit {

  private readonly fundingSourceService = inject(FundingSourceService);

  // Angular signals for reactive state
  fundingSources = signal<FundingSource[]>([]);
  selectedFundingSource = signal<FundingSource | null>(null);  // source being updated/deleted
  showDialog = signal<boolean>(false);                          // form dialog toggle
  showDeleteDialog = signal<boolean>(false);                    // delete confirmation toggle
  dialogTitle = signal<string>("");

  loading = signal<boolean>(true);
  error = signal<string | null>(null);

  sourceTypeOptions = [
  { label: 'Traditional 401(k)', value: 'TRADITIONAL_401K' },
  { label: 'Roth 401(k)', value: 'ROTH_401K' },
  { label: 'Traditional IRA', value: 'TRADITIONAL_IRA' },
  { label: 'Roth IRA', value: 'ROTH_IRA' },
  { label: 'SEP IRA', value: 'SEP_IRA' },
  { label: 'Taxable Brokerage', value: 'TAXABLE_BROKERAGE' }
];

  form!: FormGroup;

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.loadFundingSources();

    this.form = this.formBuilder.group({
      name: ["", [Validators.required, Validators.minLength(2)]],
      sourceType: [null, [Validators.required]],
      institution: ["", [Validators.required]],
      notes: [""]
    });
  }

  loadFundingSources(): void {
    this.loading.set(true);
    this.error.set(null);

    this.fundingSourceService.getFundingSources().subscribe({
      next: (data) => {
        this.fundingSources.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load funding sources.');
        this.loading.set(false);
        console.error(err);
      }
    });
  }

  saveFundingSource(): void {

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { name, sourceType, institution, notes } = this.form.value;

    const payload: FundingSource = {
      name,
      sourceType,
      institution,
      notes
    };

    if (this.selectedFundingSource() === null) {
      // CREATE
      this.fundingSourceService.createFundingSource(payload).subscribe({
        next: (data) => {
          this.fundingSources.update((currentList) => [...currentList, data]);
          this.showDialog.set(false);
        },
        error: (err) => {
          console.error(err);
          this.showDialog.set(false);
        }
      });
    } else {
      // UPDATE
      const id = this.selectedFundingSource()!.id!;
      this.fundingSourceService.udpateFundingSource(id, payload).subscribe({
        next: (data) => {
          this.fundingSources.update((currentList) =>
            currentList.map(source => source.id === data.id ? data : source)
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

  handleCreateFundingSource(): void {
    this.dialogTitle.set("Create Funding Source");
    this.selectedFundingSource.set(null);

    this.form.setValue({
      name: "",
      sourceType: null,
      institution: "",
      notes: ""
    });

    this.showDialog.set(true);
  }

  handleUpdateFundingSource(source: FundingSource): void {
    this.dialogTitle.set("Update Funding Source");
    this.selectedFundingSource.set(source);

    this.form.setValue({
      name: source.name,
      sourceType: source.sourceType,
      institution: source.institution,
      notes: source.notes ?? ""
    });

    this.showDialog.set(true);
  }

  handleDeleteFundingSource(source: FundingSource): void {
    this.selectedFundingSource.set(source);
    this.showDeleteDialog.set(true);
  }

  deleteFundingSource(): void {
    const id = this.selectedFundingSource()!.id!;

    this.fundingSourceService.deleteFundingSource(id).subscribe({
      next: () => {
        this.fundingSources.update((currentList) =>
          currentList.filter(source => source.id !== id)
        );
        this.showDeleteDialog.set(false);
      },
      error: (err) => {
        console.error(err);
        this.showDeleteDialog.set(false);
      }
    });
  }

  // formatSourceType(sourceType: string): string {
  //   return (SourceType as Record<string, string>)[sourceType] ?? sourceType;
  // }

  // getSourceTypeSeverity(sourceType: string): 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast' | null | undefined {
  //   const severities: Record<string, 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast'> = {
  //     TRADITIONAL_401K: 'info',
  //     ROTH_401K: 'success',
  //     TRADITIONAL_IRA: 'info',
  //     ROTH_IRA: 'success',
  //     SEP_IRA: 'warn',
  //     TAXABLE_BROKERAGE: 'secondary'
  //   };
  //   return severities[sourceType] ?? 'info';
  // }
}