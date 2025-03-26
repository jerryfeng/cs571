import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { ContentComponent } from './components/content/content.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HeaderComponent, FooterComponent, ContentComponent],
  template: `
    <app-header />
    <app-content />
    <app-footer />
  `,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'ui';
}
