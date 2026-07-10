import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { FundingSourcesComponent } from './pages/funding-sources/funding-sources.component';
import { ProfileComponent } from './pages/profile/profile.component';

export const routes: Routes = [
    { path: '', component: LoginComponent },
    { path: 'login', component: LoginComponent },
    { path: 'profile', component: ProfileComponent },
    { path: 'funding-sources', component: FundingSourcesComponent },
    { path: '**', redirectTo: '' },
];