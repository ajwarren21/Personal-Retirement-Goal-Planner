import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { FundingSourcesComponent } from './pages/funding-sources/funding-sources.component';

export const routes: Routes = [
    {path: '', component: LoginComponent},
    // { path: '', redirectTo: 'funding-sources', pathMatch: 'full' },
    { path: 'funding-sources', component: FundingSourcesComponent }
];
