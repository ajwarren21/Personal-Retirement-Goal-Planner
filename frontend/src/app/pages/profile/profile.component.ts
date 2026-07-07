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
  ) {}
  

  profileForm!: FormGroup;


  ngOnInit(): void {
    this.profileForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
    });
    
    // profileForm: FormGroup;

    this.loadUser();
  }
  
  // profileForm: FormGroup = this.fb.group({
  //   name: ['', [Validators.required, Validators.minLength(2)]],
  //   email: ['', [Validators.required, Validators.email]],
  // });

  loadUser(): void {
    // assumes AuthService exposes getCurrentUser(), but do not know what method it will actually call yet
    // this.authService.getCurrentUser().subscribe({
    //   next: (response: User) => {
    //     this.user = response;
    //     this.patchForm(response);
    //   },

    // making temp method for testing
    this.authService.getUser().subscribe({
      next: (response: User) => {
        this.user = response;
        this.patchForm(response);
      },
      error: () => {
        this.errorMessage = 'Unable to load profile information';
      }
    });
  }

  private patchForm(user: User): void {
    this.profileForm.patchValue({
      name: user.name,
      email: user.email,
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

    // Whatever the request looks like, the frontend just needs a response
    // shaped like `User` back so it can replace `this.user` below.
    const payload: Pick<User, 'name' | 'email'> = {
      name: this.profileForm.value.name,
      email: this.profileForm.value.email,
    };

    // Swap this block out for the real call once I know what I am doing for the put, idk how user service is ganna be for now
    // this.authService.updateUser(payload).subscribe({
    //   next: (updated: User) => {
    //     this.user = updated;
    //     this.isEditing = false;
    //     this.isSaving = false;
    //   },
    //   error: () => {
    //     this.errorMessage = 'Unable to save profile changes';
    //     this.isSaving = false;
    //   }
    // });

    // making temp method for testing
    this.mockSave(payload);
  }

  private mockSave(payload: Pick<User, 'name' | 'email'>): void {
    setTimeout(() => {
      this.user = { ...this.user, ...payload } as User;
      this.isEditing = false;
      this.isSaving = false;
    }, 400);
  }

  onLogout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

