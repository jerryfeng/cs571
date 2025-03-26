import { Component, effect, signal } from '@angular/core';
import { SearchStateService } from '../../services/search-state.service';
import { Artist } from '../../models/artist';

@Component({
  selector: 'app-result-list',
  providers: [],
  imports: [],
  template: `
    <div class="d-flex flex-row flex-nowrap overflow-x-auto gap-2">
      @for (artist of searchResults(); track $index) {
        <div class="card mb-1" (click)="selectArtist(artist.id)" [class.selected]="selected()===artist.id">
          <img src={{artist.thumbnail}} class="card-img-top" alt={{artist.title}}>
          <div class="card-body p-1">
            <p class="card-title text-start ps-1">{{artist.title}}</p>
          </div>
        </div>
      }
    </div>
  `,
  styleUrl: './result-list.component.css'
})
export class ResultListComponent {
  searchResults = signal<Array<Artist>>([]);
  selected = signal<number>(-1);
  constructor(public searchStateService: SearchStateService) {
    effect(() => {
      this.searchResults.set(searchStateService.searchResults());
    });
    effect(() => {
      const _clear = searchStateService.clear();
      this.searchResults.set([]);
      this.selected.set(-1);
    });
  }

  selectArtist(id: number): void {
    this.selected.set(id);
  }
}
