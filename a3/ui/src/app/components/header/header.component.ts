import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { LoginService } from '../../services/login.service';
import { API_ENDPOINT } from '../../app.constants';

@Component({
  selector: 'app-header',
  imports: [RouterLink],
  template: `
    <nav class="navbar navbar-expand-lg bg-body-tertiary">
      <div class="container-fluid">
        <div class="navbar-brand m-1 h1">Artist Search</div>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="nav navbar-nav ms-auto">
            <li class="nav-item">
              <button routerLink='/' type="button" class="btn m-1" [class.btn-primary]="isCurrentRoute('search')" [class.btn-light]="!isCurrentRoute('search')">Search</button>
            </li>
            @if (loginService.isLoggedIn()) {
              <li class="nav-item">
                <button routerLink='/favorites' type="button" class="btn m-1" [class.btn-primary]="isCurrentRoute('favorites')" [class.btn-light]="!isCurrentRoute('favorites')">Favorites</button>
              </li>
              <li class="nav-item text-center pe-3">
                <div class="dropdown">
                  <a class="nav-link dropdown-toggle" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    <img class="me-2" src={{loginService.user().avatar}} alt={{loginService.user().fullname}} />{{loginService.user().fullname}}
                  </a>
                  <ul class="dropdown-menu dropdown-menu-lg-end position-absolute">
                    <li><a class="dropdown-item text-danger" (click)="loginService.deleteUser()">Delete account</a></li>
                    <li><a class="dropdown-item" (click)="loginService.logout()">Log out</a></li>
                  </ul>
                </div>
              </li>
            } @else {
              <li class="nav-item">
                <button routerLink='/login' type="button" class="btn m-1" [class.btn-primary]="isCurrentRoute('login')" [class.btn-light]="!isCurrentRoute('login')">Log in</button>
              </li>
              <li class="nav-item">
                <button routerLink='/register' type="button" class="btn m-1" [class.btn-primary]="isCurrentRoute('register')" [class.btn-light]="!isCurrentRoute('register')">Register</button>
              </li>
            }
          </ul>
        </div>
      </div>
    </nav>
  `,
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  isCurrentRoute(route: string): boolean {
    let currentRoute = '';
    if (this.router.url.startsWith('/login')) {
      currentRoute = 'login';
    } else if (this.router.url.startsWith('/register')) {
      currentRoute = 'register';
    } else if (this.router.url.startsWith('/favorites')) {
      currentRoute = 'favorites';
    } else {
      currentRoute = 'search';
    }
    return route === currentRoute;
  }

  constructor(public loginService: LoginService, public router: Router) {

  }
}
