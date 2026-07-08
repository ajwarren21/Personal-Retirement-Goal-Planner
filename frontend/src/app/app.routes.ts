import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { FundingSourcesComponent } from './pages/funding-sources/funding-sources.component';
import { RetirementGoalsComponent } from './pages/retirement-goals/retirement-goals.component';

export const routes: Routes = [
    {path: '', component: LoginComponent},
    // { path: '', redirectTo: 'funding-sources', pathMatch: 'full' },
    { path: 'funding-sources', component: FundingSourcesComponent },
    { path: 'retirement-goals', component: RetirementGoalsComponent }
];
