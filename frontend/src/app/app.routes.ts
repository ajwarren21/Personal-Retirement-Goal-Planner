import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { FundingSourcesComponent } from './pages/funding-sources/funding-sources.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { RetirementGoalsComponent } from './pages/retirement-goals/retirement-goals.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { GoalDetailComponent } from './pages/goal-detail/goal-detail.component';
import { FundingSourceDetailComponent } from './pages/funding-source-detail/funding-source-detail.component';

export const routes: Routes = [
    { path: '', component: LoginComponent },
    { path: 'login', component: LoginComponent },
    { path: 'profile', component: ProfileComponent },
    { path: 'dashboard', component: DashboardComponent},
    { path: 'funding-sources', component: FundingSourcesComponent },
    { path: 'funding-sources/:id', component: FundingSourceDetailComponent },
    { path: 'retirement-goals', component: RetirementGoalsComponent },
    { path: 'retirement-goals/:id', component: GoalDetailComponent },
    { path: '**', redirectTo: '' },
];