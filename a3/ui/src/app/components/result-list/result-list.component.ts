import { Component, effect, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { SearchStateService } from '../../services/search-state.service';
import { Artist } from '../../models/artist';
import { FontAwesomeModule, FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faStar as farStar } from '@fortawesome/free-regular-svg-icons';
import { faStar } from '@fortawesome/free-solid-svg-icons';
import { LoginService } from '../../services/login.service';
import { FavoritesService } from '../../services/favorites.service';

@Component({
  selector: 'app-result-list',
  providers: [],
  imports: [RouterLink, FontAwesomeModule],
  template: `
    <div class="d-flex flex-row flex-nowrap overflow-x-auto gap-2">
      @for (artist of searchResults(); track $index) {
        <div routerLink={{artistDetailLink(artist.id)}} class="card mb-1" (click)="selectArtist(artist.id)" [class.selected]="searchStateService.selectedArtistId()===artist.id">
          <img src={{artist.thumbnail}} class="card-img-top" alt={{artist.title}}>
          @if (loginService.isLoggedIn()) {
            @if (favoritesService.getIds().includes(artist.id)) {
              <div class="circle position-absolute top-0 end-0 m-2 pt-1" (click)="removeFavorite($event, artist.id)">
                <fa-icon class="yellow" [icon]="['fas', 'star']" [size]="'lg'"></fa-icon>
              </div>
            } @else {
              <div class="circle position-absolute top-0 end-0 m-2 pt-1" (click)="addFavorite($event, artist.id)">
                <fa-icon class="white" [icon]="['far', 'star']" [size]="'lg'"></fa-icon>
              </div>
            }
          }
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

  constructor(
    public searchStateService: SearchStateService,
    public loginService: LoginService,
    library: FaIconLibrary,
    public favoritesService: FavoritesService
  ) {
    library.addIcons(faStar, farStar);
    effect(() => {
      this.searchResults.set(searchStateService.searchResults());
      this.searchStateService.selectedArtistId.set('');
    });
    effect(() => {
      const _clear = searchStateService.clear();
      this.searchResults.set([]);
      this.searchStateService.selectedArtistId.set('');
    });
  }

  async addFavorite(event : MouseEvent, artistId: string): Promise<void> {
    event.stopPropagation();
    await this.favoritesService.addFavorite(artistId);
  }

  async removeFavorite(event : MouseEvent, artistId: string): Promise<void> {
    event.stopPropagation();
    await this.favoritesService.removeFavorite(artistId);
  }

  selectArtist(id: string): void {
    this.searchStateService.selectedArtistId.set(id);
  }
  artistDetailLink(id: string): string {
    return `/artists/${id}/info`;
  }
}
