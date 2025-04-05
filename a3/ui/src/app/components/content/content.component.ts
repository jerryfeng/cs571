import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NotificationsComponent } from "../notifications/notifications.component";

@Component({
  selector: 'app-content',
  imports: [RouterOutlet, NotificationsComponent],
  template: `
    <div class="container mt-4 text-center">
      <div class="row">
        <div class="col-lg-1"></div>
        <div class="col-lg-10">
          <router-outlet></router-outlet>
        </div>
        <div class="col-lg-1"></div>
      </div>
    </div>
    <app-notifications class="position-absolute top-0 end-0 mt-5 pt-5 me-3 z-3" />
  `,
  styleUrl: './content.component.css'
})
export class ContentComponent {

}
