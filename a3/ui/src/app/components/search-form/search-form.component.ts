import { Component, computed, signal, effect } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { API_ENDPOINT } from '../../app.constants';
import { SearchStateService } from '../../services/search-state.service';
import { Artist } from '../../models/artist';
import { Router, RouterLink } from '@angular/router';

const MISSING_IMAGE_URL = '/assets/shared/missing_image.png';
const MISSING_IMAGE_REPLACEMENT_URL = '/public/artsy_logo.svg';

@Component({
  selector: 'app-search-form',
  imports: [FormsModule, RouterLink],
  template: `
    <form (ngSubmit)="searchArtist()">
      <div class="input-group">
        <input 
          type="text"
          class="form-control form-control-lg"
          name="searchQuery"
          placeholder="Please enter an artist name."
          [(ngModel)]="searchQuery" />
        <button class="btn btn-primary" [disabled]="isDisabled()">
          <span>Search</span>
          @if (isSearching()) {
            <span class="spinner-border spinner-border-sm ms-1" aria-hidden="true"></span>
          }
        </button>
        <button routerLink="/" type="button" class="btn btn-secondary" (click)="clear()">Clear</button>
      </div>
    </form>
    @if (noResults()) {
      <div class="alert alert-danger mt-2 text-start" role="alert">
        No results.
      </div>
    }
  `,
  styleUrl: './search-form.component.css'
})
export class SearchFormComponent {
  searchQuery = signal<string>('');
  isDisabled = computed<boolean>(() => this.searchQuery() === '');
  isSearching = signal<boolean>(false);
  noResults = signal<boolean>(false);
  async searchArtist(): Promise<void> {
    this.router.navigateByUrl('/');
    this.searchStateService.searchResults.set([]);
    this.noResults.set(false);
    this.isSearching.set(true);
    const response = await fetch(`${API_ENDPOINT}/artists?q=${this.searchQuery()}`);
    const results = await response.json();
    const artists: Array<Artist> = [];
    for(const result of results) {
      const id = result._links.self.href.split('/').pop();
      const imageLink = result._links.thumbnail.href;
      const thumbnail = imageLink !== MISSING_IMAGE_URL ? imageLink : MISSING_IMAGE_REPLACEMENT_URL;
      artists.push({
        title: result.title,
        id,
        thumbnail
      });
    }

    if (artists.length === 0) {
      this.noResults.set(true);
    }
    this.searchStateService.searchResults.set(artists);
    this.isSearching.set(false);
  };

  clear(): void {
    this.searchStateService.clear.update(x => x + 1);
  }

  constructor(private searchStateService : SearchStateService, private router : Router) {
    effect(()=> {
      const _clear = this.searchStateService.clear();
      this.searchQuery.set('');
      this.noResults.set(false);
    });
  } 
}
