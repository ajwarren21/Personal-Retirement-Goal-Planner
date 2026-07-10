import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { FundingSourcesComponent } from './pages/funding-sources/funding-sources.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { RetirementGoalsComponent } from './pages/retirement-goals/retirement-goals.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';

export const routes: Routes = [
    { path: '', component: LoginComponent },
    { path: 'login', component: LoginComponent },
    { path: 'profile', component: ProfileComponent },
    { path: 'dashboard', component: DashboardComponent},
    { path: 'funding-sources', component: FundingSourcesComponent },
    { path: 'retirement-goals', component: RetirementGoalsComponent },
    { path: '**', redirectTo: '' },
];