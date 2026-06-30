import { Component, ChangeDetectionStrategy, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { MenubarModule } from 'primeng/menubar';
import { MenuItem } from 'primeng/api';
import { FundingSourcesComponent } from './pages/funding-sources/funding-sources.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MenubarModule],
  templateUrl: './app.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './app.component.css'
})
export class AppComponent {
  protected readonly title = signal('Finance App');

  constructor(private router: Router) {}

   navItems: MenuItem[] = [
    {label: "Funding Sources", command: () => this.router.navigate(["/funding-sources"])}
  ]
  

}
