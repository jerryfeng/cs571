import { Component } from '@angular/core';
import { SearchFormComponent } from '../search-form/search-form.component';
import { ResultListComponent } from '../result-list/result-list.component';

@Component({
  selector: 'app-content',
  imports: [SearchFormComponent, ResultListComponent],
  template: `
    <div class="container mt-4 text-center">
      <div class="row">
        <div class="col-1"></div>
        <div class="col-10">
          <app-search-form />
          <div class="mt-2"></div>
          <app-result-list />
        </div>
        <div class="col-1"></div>
      </div>
    </div>
  `,
  styleUrl: './content.component.css'
})
export class ContentComponent {

}
