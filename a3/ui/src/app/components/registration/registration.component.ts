import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Registration } from '../../models/registration';
import { API_ENDPOINT } from '../../app.constants';
import { HttpStatusCode } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-registration',
  imports: [FormsModule, RouterLink],
  template: `
    <div class="row">
      <div class="col-lg-4"></div>
      <div class="col-lg-4">
        <div class="card text-start">
          <div class="card-body">
            <h2 class="card-title mb-4">Register</h2>
            <form (ngSubmit)="register()">
              <div class="mb-3">
                <label for="fullname" class="form-label mb-0">Fullname</label>
                <input type="text" class="form-control" id="fullname" name="fullname" placeholder="John Doe" required [(ngModel)]="registration.fullname" #fullname="ngModel"/>
                @if (fullname.invalid && (fullname.dirty || fullname.touched)) {
                  @if (fullname.hasError('required')) {
                    <div class="invalid-feedback d-block">
                      Fullname is required.
                    </div>
                  }
                }
              </div>
              <div class="mb-3">
                <label for="email" class="form-label mb-0">Email address</label>
                <input type="email" class="form-control" id="email" name="email" placeholder="Enter email" required [email]="true" [(ngModel)]="registration.email" #email="ngModel">
                @if (email.invalid && (email.dirty || email.touched)) {
                  @if (email.hasError('required')) {
                    <div class="invalid-feedback d-block">
                      Email is required.
                    </div>
                  } @else if (email.hasError('email')) {
                    <div class="invalid-feedback d-block">
                      Email must be valid.
                    </div>
                  }
                }
                @if (validationError() !== '' && registration.email === existingEmail()) {
                  <div class="invalid-feedback d-block">
                    {{validationError()}}
                  </div>
                }
              </div>
              <div class="mb-3">
                <label for="password" class="form-label mb-0">Password</label>
                <input type="password" class="form-control" id="password" name="password" placeholder="Password" required [(ngModel)]="registration.password" #password="ngModel">
                @if (password.invalid && (password.dirty || password.touched)) {
                  @if (password.hasError('required')) {
                    <div class="invalid-feedback d-block">
                      Password is required.
                    </div>
                  }
                }
              </div>
              <div class="mb-3 d-grid">
                @let disabled = fullname.invalid || email.invalid || password.invalid || registration.email === existingEmail();
                <button type="submit" class="btn btn-primary" [disabled]="disabled">Register</button>
              </div>
            </form>
          </div>
        </div>
        <p>Already have an account? <a routerLink="/login">Login</a></p>
      </div>
      <div class="col-lg-4"></div>    
  `,
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {
  registration = new Registration('', '', '');
  validationError = signal('');
  existingEmail = signal('');

  async register() {
    const response = await fetch(`${API_ENDPOINT}/users`, {
			method: 'POST',
			body: JSON.stringify(this.registration),
      credentials: 'include',
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json'
			}
		});

    if (response.status === HttpStatusCode.BadRequest) {
      const error = await response.json();
      this.validationError.set(error.message);
      this.existingEmail.set(error.email);
    } else if (response.ok) {
      const user = await response.json();
      this.loginserVice.login(user);
      this.router.navigateByUrl('/');
    }
  }

  constructor(public loginserVice: LoginService, private router: Router) {

  }
}
