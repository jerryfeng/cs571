import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Login } from '../../models/login';
import { LoginService } from '../../services/login.service';
import { API_ENDPOINT } from '../../app.constants';
import { HttpStatusCode } from '@angular/common/http';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  template: `
    <div class="row">
      <div class="col-lg-4"></div>
      <div class="col-lg-4">
        <div class="card text-start">
          <div class="card-body">
            <h2 class="card-title mb-4">Login</h2>
            <form (ngSubmit)="login()">
              <div class="mb-3">
                <label for="email" class="form-label mb-0">Email address</label>
                <input type="email" class="form-control" id="email" name="email" placeholder="Enter email" required [email]="true" [(ngModel)]="credential.email" #email="ngModel">
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
              </div>
              <div class="mb-3">
                <label for="password" class="form-label mb-0">Password</label>
                <input type="password" class="form-control" id="password" name="password" placeholder="Password" required [(ngModel)]="credential.password" #password="ngModel">
                @if (password.invalid && (password.dirty || password.touched)) {
                  @if (password.hasError('required')) {
                    <div class="invalid-feedback d-block">
                      Password is required.
                    </div>
                  }
                }
                @if (validationError() !== '' && credential.email === wrongEmail() && credential.password === wrongPassword()) {
                  <div class="invalid-feedback d-block">
                    {{validationError()}}
                  </div>
                }
              </div>
              <div class="mb-3 d-grid">
                @let disabled = email.invalid || password.invalid;
                <button type="submit" class="btn btn-primary" [disabled]="disabled">Log in</button>
              </div>
            </form>
          </div>
        </div>
        <p>Don't have an account yet? <a routerLink="/register">Register</a></p>
      </div>
      <div class="col-lg-4"></div>
  `,
  styleUrl: './login.component.css'
})
export class LoginComponent {
  credential = new Login('', '');
  validationError = signal('');
  wrongEmail = signal('');
  wrongPassword = signal('');

  async login() {
    const response = await fetch(`${API_ENDPOINT}/login`, {
			method: 'POST',
			body: JSON.stringify(this.credential),
      credentials: 'include',
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json'
			}
		});

    if (response.status === HttpStatusCode.Unauthorized) {
      const error = await response.json();
      this.wrongEmail.set(this.credential.email);
      this.wrongPassword.set(this.credential.password);
      this.validationError.set(error.message);
    } else if (response.ok) {
      const user = await response.json();
      this.loginserVice.login(user);
      this.router.navigateByUrl('/');
    }
  }

  constructor(public loginserVice: LoginService, private router: Router) {

  }

}
