import { computed, Injectable, signal } from '@angular/core';
import { User } from '../models/user';
import { API_ENDPOINT } from '../app.constants';
import { NotificationsService } from './notifications.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  user = signal<User>({fullname: '', email: '', avatar: ''});
  isLoggedIn = computed<boolean>(() => this.user().email !== '');

  public login(user: User): void {
    this.user.set(user);
  }

  public async logout(): Promise<void> {
    await fetch(`${API_ENDPOINT}/logout`, {
      credentials: 'include'
    });
    this.user.set({fullname: '', email: '', avatar: ''});
    this.notificationsService.createNotification('Logged out', 'success');
    this.router.navigateByUrl('/');
  }

  public async deleteUser(): Promise<void> {
    await fetch(`${API_ENDPOINT}/me`, {
      method: 'DELETE',
      credentials: 'include'
    });
    this.user.set({fullname: '', email: '', avatar: ''});
    this.notificationsService.createNotification('Account deleted', 'danger');
    this.router.navigateByUrl('/');
  }

  public async loadUser(): Promise<void> {
    const response = await fetch(`${API_ENDPOINT}/me`, {
      credentials: 'include'
    });
    if (response.ok) {
      const user = await response.json();
      this.user.set({
        fullname: user.fullname,
        email: user.email,
        avatar: user.avatar
      });
    }
  }

  constructor(public notificationsService: NotificationsService, public router: Router) {

  }
}
