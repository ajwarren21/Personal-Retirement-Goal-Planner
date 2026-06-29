import { Routes } from '@angular/router';
import { FundingSourcesComponent } from './pages/funding-sources/funding-sources.component';

export const routes: Routes = [
  { path: '', redirectTo: 'funding-sources', pathMatch: 'full' },
  { path: 'funding-sources', component: FundingSourcesComponent }
];