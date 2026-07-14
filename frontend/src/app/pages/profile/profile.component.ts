import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { User } from '../../types/user';

import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { SkeletonModule } from 'primeng/skeleton';
import { TagModule } from 'primeng/tag';
import { Location } from '@angular/common';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,ReactiveFormsModule,AvatarModule,ButtonModule,
    IconFieldModule,InputIconModule,InputTextModule,MessageModule,
    SkeletonModule,TagModule
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {

  user: User | null = null;
  errorMessage = '';

  isEditing = false;
  isSaving = false;
  
  // FormBuilder fb;
  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder,
    private location: Location
  ) {}
  

  profileForm!: FormGroup;


  ngOnInit(): void {
    this.profileForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
    });
    
    // profileForm: FormGroup;

    this.loadUser();
  }
  
  // profileForm: FormGroup = this.fb.group({
  //   username: ['', [Validators.required, Validators.minLength(2)]],
  //   email: ['', [Validators.required, Validators.email]],
  // });

  loadUser(): void {
    // AuthService.getUser() reads synchronously from localStorage — it's not
    // an Observable, so no .subscribe() here.
    const user = this.authService.getUser();

    if (user) {
      this.user = user;
      this.patchForm(user);
    } else {
      this.router.navigate(['/login']);
      this.errorMessage = 'Unable to load profile information';
    }
  }

  private patchForm(user: User): void {
    this.profileForm.patchValue({
      username: user.username,
      email: user.email,
    });
  }

  get initials(): string {
    if (!this.user?.username) return '';
    return this.user.username.substring(0, 2).toUpperCase();
  }

  startEditing(): void {
    if (!this.user) return;
    this.patchForm(this.user);
    this.isEditing = true;
  }

  cancelEditing(): void {
    if (this.user) {
      this.patchForm(this.user);
    }
    this.isEditing = false;
  }

  saveProfile(): void {
    if (this.profileForm.invalid || !this.user) {
      this.profileForm.markAllAsTouched();
      return;
    }

    this.isSaving = true;

    const payload: Pick<User, 'username' | 'email'> = {
      username: this.profileForm.value.username,
      email: this.profileForm.value.email,
    };

    this.authService.updateUser(payload).subscribe({
      next: (updated: User) => {
        this.user = updated;
        this.isEditing = false;
        this.isSaving = false;
      },
      error: () => {
        this.errorMessage = 'Unable to save profile changes';
        this.isSaving = false;
      }
    });
  }

  onLogout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  goBack(): void {
    this.location.back();
  }
}