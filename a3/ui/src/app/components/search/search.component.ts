import { Component } from '@angular/core';
import { SearchFormComponent } from "../search-form/search-form.component";
import { ResultListComponent } from "../result-list/result-list.component";
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-search',
  imports: [RouterOutlet, SearchFormComponent, ResultListComponent],
  template: `
    <app-search-form />
    <div class="mt-2"></div>
    <app-result-list />
    <div class="mt-2"></div>
    <router-outlet></router-outlet>
  `,
  styleUrl: './search.component.css'
})
export class SearchComponent {

}
