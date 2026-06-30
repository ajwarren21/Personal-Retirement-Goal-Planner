import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { FundingSourceService } from '../../services/FundingSourceService'; 
import { FundingSource } from '../../types/FundingSource';

@Component({
  selector: 'app-funding-sources',
  standalone: true,
  imports: [
    CommonModule,
    TableModule,
    TagModule,
    ButtonModule,
    ProgressSpinnerModule
  ],
  templateUrl: './funding-sources.component.html'
})
export class FundingSourcesComponent implements OnInit {

  private readonly fundingSourceService = inject(FundingSourceService);

  // Angular signals for reactive state
  fundingSources = signal<FundingSource[]>([]);
  loading = signal<boolean>(true);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.loadFundingSources();
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
        this.error.set('Failed to load funding sources. Make sure the backend is running.');
        this.loading.set(false);
        console.error(err);
      }
    });
  }

}