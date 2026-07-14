import { Component, signal } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';

// import { ProfileComponent } from '../profile/profile.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, ButtonModule, DialogModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  form! : FormGroup; 
  isLoggingIn = true;
  errorMessage = '';
  successMessage = '';

  
  
  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ){}
  
  ngOnInit(): void {
   
    this.authService.initCsrf().subscribe({
    next: () => console.log('CSRF Token initialized successfully'),
    error: (err) => console.error('Failed to initialize CSRF', err)
    });

   
    this.form = this.formBuilder.group({
      username: ["", [Validators.required]],
      password: ["", [Validators.required]]
    });
    
  }
  

  showProfile = false;

  openProfile() {
    this.showProfile = true;
  }

  closeProfile() {
    this.showProfile = false;
  }

  toggleForm() {
    this.isLoggingIn = !this.isLoggingIn;
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.isLoggingIn) {
      this.form.addControl('email', this.formBuilder.control('', Validators.required));
    } else {
      this.form.removeControl('email');
    }
  }

  

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.errorMessage = '';
    this.successMessage = '';

    if (this.isLoggingIn) {
    this.authService.login(this.form.value as any).subscribe({
      next: (response) => {
        console.log('Login success', response);
        this.successMessage = 'Login successful! Redirecting...';
        this.router.navigate(['/funding-sources']); 
      },
      error: () => {
        this.errorMessage = 'Invalid credentials';
      }
    });
  } else {
    this.authService.register(this.form.value as any).subscribe({
      next: (response) => {
        console.log('Registration success', response);
        this.successMessage = 'Registration successful! Please log in.';
        this.toggleForm();
      },
      error: () => {
        this.errorMessage = 'Registration failed. Please try again.';
      }
    }); 
  }
}


}