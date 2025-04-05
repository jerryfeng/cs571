import { Component } from '@angular/core';
import { NotificationsService } from '../../services/notifications.service';

@Component({
  selector: 'app-notifications',
  imports: [],
  template: `
    @for (notification of notificationsService.getNotifications(); track $index) {
        <div class="alert alert-{{notification.style}}">
          {{notification.message}}
          <button type="button" class="btn-close" (click)="notificationsService.dismissNotification(notification.id)" aria-label="Close"></button>
        </div>
    }
  `,
  styleUrl: './notifications.component.css'
})
export class NotificationsComponent {


  constructor(public notificationsService: NotificationsService) {

  }
}
