import { Component, signal } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';

import { ProfileComponent } from '../profile/profile.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, ButtonModule, DialogModule, ProfileComponent],
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
<<<<<<< HEAD
   
    this.authService.initCsrf().subscribe({
    next: () => console.log('CSRF Token initialized successfully'),
    error: (err) => console.error('Failed to initialize CSRF', err)
    });

=======
    // this.loadMovies();
    // this.loadDirectors();
    
    // using form builder to create the form group 
    // form builder lets you initialize form with default values and validators
>>>>>>> 9b26f8bd217f6fdfeb1a57b931bf10efdd6885cd
    this.form = this.formBuilder.group({
      username: ["", [Validators.required]],
      password: ["", [Validators.required]]
    });
    
  }
  
  // showProfile = signal(false);
  // openProfile() {
  //   this.showProfile.set(true);
  // }

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



// Old html, needed here:
// <!-- <p-dialog
//   header="Profile"
//   [visible]="showProfile()"
//   (visibleChange)="showProfile.set($event)"
//   [modal]="true"
//   [style]="{ width: '50rem' }"> -->
//   <div class="login-bg">
//     <div class="login-card">

//       <div class="login-logo">
//         <div class="login-logo-icon">
//           <i class="ti ti-chart-line"></i>
//         </div>
//         <h1>NAME (idk for now)</h1>
//         <p>Sign in to your account</p>
//       </div>

//       <form [formGroup]="form" (ngSubmit)="onSubmit()">

//         <div class="form-field">
//           <label for="email">Email address</label>
//           <input id="email" type="email" formControlName="email" placeholder="name@example.com">
//         </div>

//         <div class="form-field">
//           <label for="password">Password</label>
//           <input id="password" type="password" formControlName="password" placeholder="••••••••">
//         </div>

//         @if (errorMessage) {
//           <p class="error">{{ errorMessage }}</p>
//         }

//         <button type="submit" [disabled]="form.invalid">Sign in</button>
//         <!-- <div class="flex justify-center">
//               <button pButton disabled>Submit</button>
//             </div> -->
//             <!-- <p-button 
//               label="Open Profile"
//               (onClick)="openProfile()">
//             </p-button> -->
            
//       </form>

//       <div class="login-footer">
//         No account? <a routerLink="/register">Register</a> 
//       </div>
//       <!-- <p-button 
//               label="Open Profile"
//               (onClick)="openProfile()">
//             </p-button> -->

//     </div>
//   </div>

// <!-- </p-dialog> -->
