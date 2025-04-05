import { Injectable, signal } from '@angular/core';
import { AppNotification } from '../models/app-notification';

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {
  private notifications = signal<Array<AppNotification>>([]);
  id = 0;

  getNotifications(): Array<AppNotification> {
    return this.notifications();
  }

  createNotification(message: string, style: 'success' | 'danger'): void {
    const id = this.id;
    this.notifications.update((notifications) => {
      return [...notifications, {id, message, style}];
    });
    setTimeout(() => this.dismissNotification(id), 3000);
    this.id += 1;
  }

  dismissNotification(id: number): void {
    this.notifications.update((notifications) => {
      return notifications.filter((notification) => notification.id !== id);
    });
  }

  constructor() { }
}
