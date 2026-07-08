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
import { RetirementGoalService } from '../../services/retirement-goal.service';
import { RetirementGoal } from '../../types/RetirementGoal';
import { InputNumberModule } from 'primeng/inputnumber';

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

  // Angular signals for reactive state
  retirementGoals = signal<RetirementGoal[]>([]);
  selectedRetirementGoal = signal<RetirementGoal | null>(null);  // source being updated/deleted
  showDialog = signal<boolean>(false);                          // form dialog toggle
  showDeleteDialog = signal<boolean>(false);                    // delete confirmation toggle
  dialogTitle = signal<string>("");

  loading = signal<boolean>(true);
  error = signal<string | null>(null);

//   sourceTypeOptions = [
//   { label: 'Traditional 401(k)', value: 'TRADITIONAL_401K' },
//   { label: 'Roth 401(k)', value: 'ROTH_401K' },
//   { label: 'Traditional IRA', value: 'TRADITIONAL_IRA' },
//   { label: 'Roth IRA', value: 'ROTH_IRA' },
//   { label: 'SEP IRA', value: 'SEP_IRA' },
//   { label: 'Taxable Brokerage', value: 'TAXABLE_BROKERAGE' }
// ];

  form!: FormGroup;

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.loadRetirementGoals();

    this.form = this.formBuilder.group({
      name: ["", [Validators.required, Validators.minLength(2)]],
      targetRetirementAge: [0, [Validators.required]],
      targetAmount: [0, [Validators.required]],
      notes: [""]
    });
  }

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
      // contributions: [],
      notes
    };

    if (this.selectedRetirementGoal() === null) {
      // CREATE
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
      // UPDATE
      const id = this.selectedRetirementGoal()!.id!;
      this.retirementGoalService.udpateRetirementGoal(id, payload).subscribe({
        next: (data) => {
          this.retirementGoals.update((currentList) =>
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

  handleCreateRetirementGoal(): void {
    this.dialogTitle.set("Create Retirement Goal");
    this.selectedRetirementGoal.set(null);

    this.form.setValue({
      name: "",
      targetRetirementAge: 0,
      targetAmount: 0,
      // contributions: [],
      notes: ""
    });

    this.showDialog.set(true);
  }

  handleUpdateRetirementGoal(source: RetirementGoal): void {
    this.dialogTitle.set("Update Funding Source");
    this.selectedRetirementGoal.set(source);

    this.form.setValue({
      name: source.name,
      targetRetirementAge: source.targetRetirementAge,
      targetAmount: source.targetAmount,
      // contributions: this.selectedRetirementGoal()?.contributions ?? [],
      // The contributions field might could be ignored by backend and handled in the backend service instead
      notes: source.notes
    });

    this.showDialog.set(true);
  }

  handleDeleteRetirementGoal(source: RetirementGoal): void {
    this.selectedRetirementGoal.set(source);
    this.showDeleteDialog.set(true);
  }

  deleteRetirementGoal(): void {
    const id = this.selectedRetirementGoal()!.id!;

    this.retirementGoalService.deleteRetirementGoal(id).subscribe({
      next: () => {
        this.retirementGoals.update((currentList) =>
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
}

