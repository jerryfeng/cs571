import { Component, effect, Input, OnChanges, signal, SimpleChanges } from '@angular/core';
import { ArtistInfo } from '../../models/artist-info';
import { API_ENDPOINT } from '../../app.constants';
import { LoginService } from '../../services/login.service';
import { FontAwesomeModule, FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faStar as farStar } from '@fortawesome/free-regular-svg-icons';
import { faStar } from '@fortawesome/free-solid-svg-icons';
import { FavoritesService } from '../../services/favorites.service';
import { Artist } from '../../models/artist';
import { RouterLink } from '@angular/router';
import { SearchStateService } from '../../services/search-state.service';

const MISSING_IMAGE_URL = '/assets/shared/missing_image.png';
const MISSING_IMAGE_REPLACEMENT_URL = '/public/artsy_logo.svg';

@Component({
  selector: 'app-artist-info',
  imports: [FontAwesomeModule, RouterLink],
  template: `
    @if (loading()) {
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    }
    @if (artistInfo() !== null) {
      <div>
				<h3 class="mb-0">
          {{artistInfo()?.name}}
          @if (loginService.isLoggedIn()) {
            @if (artistId && favoritesService.getIds().includes(artistId)) {
              <span class="pt-1" (click)="removeFavorite($event, artistId)">
                <fa-icon class="yellow" [icon]="['fas', 'star']" [size]="'sm'"></fa-icon>
              </span>
            } @else if (artistId) {
              <span class="pt-1" (click)="addFavorite($event, artistId)">
                <fa-icon [icon]="['far', 'star']" [size]="'sm'"></fa-icon>
              </span>
            }
          }
        </h3>
				<p class="mb-1">{{artistInfo()?.nationality}}, {{artistInfo()?.birthYear}} - {{artistInfo()?.deathYear}}</p>
				<p class="text-justify">
          @for (paragraph of artistInfo()?.biography?.split('\n'); track $index) {
            <p>{{paragraph}}</p>
          }
        </p>
      </div>
      @if (loginService.isLoggedIn()) {
        <div class="mt-4"></div>
        <div class="d-flex flex-row flex-nowrap overflow-x-auto gap-2">
          @for (artist of similarArtists(); track $index) {
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
      }
      <div class="mt-5"></div>

    }
  `,
  styleUrl: './artist-info.component.css'
})
export class ArtistInfoComponent implements OnChanges {
  @Input() artistId?: string = '';
  loading = signal<boolean>(false);
  artistInfo = signal<ArtistInfo | null>(null);
  similarArtists = signal<Array<Artist>>([]);

  constructor(
    public loginService: LoginService,
    library: FaIconLibrary,
    public favoritesService: FavoritesService,
    public searchStateService: SearchStateService
  ) {
    library.addIcons(faStar, farStar);
  }

  async addFavorite(event : MouseEvent, artistId: string): Promise<void> {
    event.stopPropagation();
    await this.favoritesService.addFavorite(artistId);
  }

  async removeFavorite(event : MouseEvent, artistId: string): Promise<void> {
    event.stopPropagation();
    await this.favoritesService.removeFavorite(artistId);
  }

  async getArtistInfo(): Promise<void> {
    if (!this.artistId) return;
    this.artistInfo.set(null);
    this.loading.set(true);
    const response = await fetch(`${API_ENDPOINT}/artists/${this.artistId}`);
    const result = await response.json();
    const artistInfo: ArtistInfo = {
      name: result.name,
      birthYear: result.birthday || null,
      deathYear: result.deathday || null,
      nationality: result.nationality || null,
      biography: result.biography || null
    };
    this.loading.set(false);
    this.artistInfo.set(artistInfo);
  }

  async getSimilarArtists(): Promise<void> {
    const response = await fetch(`${API_ENDPOINT}/artists/similar/${this.artistId}`);
    const results = await response.json();
    const artists: Array<Artist> = [];
    for(const result of results) {
      const id = result._links.self.href.split('/').pop();
      const imageLink = result._links.thumbnail.href;
      const thumbnail = imageLink !== MISSING_IMAGE_URL ? imageLink : MISSING_IMAGE_REPLACEMENT_URL;
      artists.push({
        title: result.name,
        id,
        thumbnail
      });
    }
    this.similarArtists.set(artists);
  }

  async ngOnChanges(changes: SimpleChanges): Promise<void> {
    await this.getArtistInfo();
    if (this.loginService.isLoggedIn()) {
      await this.getSimilarArtists();
    }
  }

  selectArtist(artistId: string): void {
    this.searchStateService.selectedArtistId.set(artistId);
  }

  artistDetailLink(id: string): string {
    return `/artists/${id}/info`;
  }

}
