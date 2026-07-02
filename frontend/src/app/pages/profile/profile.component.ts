// import { Component } from '@angular/core';
import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ButtonModule } from 'primeng/button';
import {User} from '../../types/user';
import { DialogModule } from 'primeng/dialog';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ButtonModule, DialogModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
 
  user: User | null = null;
  errorMessage = '';
  // showProfile = signal(false);

  
  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}
  
  ngOnInit(): void {
    this.loadUser();
  }
  
  loadUser(): void {
    // assumes AuthService exposes getCurrentUser(), but do not know what method it will actually call yet
    // this.authService.getCurrentUser().subscribe({
    //   next: (response: User) => {
    //     this.user = response;
    //   },

    // making temp method for testing
    this.authService.getUser().subscribe({
      next: (response: User) => {
        this.user = response;
      },
      error: () => {
        this.errorMessage = 'Unable to load profile information';
      }
    });
  }
 
  get initials(): string {
    if (!this.user?.name) return '';
    return this.user.name
      .split(' ')
      .map(part => part.charAt(0))
      .join('')
      .substring(0, 2)
      .toUpperCase();
  }
 
  onLogout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
