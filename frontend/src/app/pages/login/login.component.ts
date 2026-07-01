import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, ButtonModule],
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
  ){}

  ngOnInit(): void {
    // this.loadMovies();
    // this.loadDirectors();

    // using form builder to create the form group 
    // form builder lets you initialize form with default values and validators
    this.form = this.formBuilder.group({
      email: ["", [Validators.required, Validators.email]],
      password: ["", [Validators.required]]
    });

  }

  toggleForm() {
    this.isLoggingIn = !this.isLoggingIn;
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.isLoggingIn) {
      this.form.addControl('username', this.formBuilder.control('', Validators.required));
    } else {
      this.form.removeControl('username');
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