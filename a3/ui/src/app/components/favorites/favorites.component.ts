import { Component, OnDestroy, signal } from '@angular/core';
import { FavoritesService } from '../../services/favorites.service';
import { Favorite } from '../../models/favorite';
import { Router } from '@angular/router';

const MISSING_IMAGE_REPLACEMENT_URL = '/public/artsy_logo.svg';

@Component({
  selector: 'app-favorites',
  imports: [],
  template: `
    @if (favoritesService.loading()) {
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    } @else if (favoritesService.getFavorites().length === 0) {
      <div class="alert alert-danger mt-2 text-start" role="alert">
        No favorites.
      </div>
    } @else {
      <div class="row row-cols-1 row-cols-lg-3 g-3 mb-5">
        @for (favorite of favoritesService.getFavorites(); track $index) {
          <div class="col">
            <div class="card mb-1 text-start" (click)="goToArtistDetail(favorite.artistId)">
              <div class="card-body card-background" [style.backgroundImage]="'url(' + getThumbnail(favorite) + ')'"></div>
              <div class="card-body p-3">
                <h3>{{favorite.artistDetail.name}}</h3>
                <p class="card-title mb-5">
                  {{favorite.artistDetail.birthYear}} - {{favorite.artistDetail.deathYear}}<br />
                  {{favorite.artistDetail.nationality}}
                </p>
                <u class="cart-text position-absolute bottom-0 end-0 me-2 mb-2" (click)="removeFavorite($event, favorite.artistId)">Remove</u>
                <p class="card-text position-absolute bottom-0 start-0 ms-2 mb-2">{{recencies().get(favorite.artistId)}}</p>
              </div>
            </div>
          </div>
        }
      </div>
    }

  `,
  styleUrl: './favorites.component.css'
})
export class FavoritesComponent implements OnDestroy {
  recencies = signal<Map<string, string>>(new Map());
  isloading = signal<boolean>(true);
  recencyComputer = setInterval(()=> {
    let recencies = new Map<string, string>();
    this.favoritesService.getFavorites().forEach(favorite => {
      recencies.set(favorite.artistId, this.getRecency(favorite));
    });
    this.recencies.set(recencies);
  }, 1000);

  constructor(public favoritesService: FavoritesService, public router: Router) {
    let recencies = new Map<string, string>();
    this.favoritesService.getFavorites().forEach(favorite => {
      recencies.set(favorite.artistId, this.getRecency(favorite));
    });
    this.recencies.set(recencies);
  }

  goToArtistDetail(artistId: string): void {
    this.router.navigateByUrl(`/artists/${artistId}/info`);
  }

  removeFavorite(event: MouseEvent, artistId: string): void {
    event.stopPropagation();
    this.favoritesService.removeFavorite(artistId);
  }

  getThumbnail(favorite: Favorite): string {
    return favorite.artistDetail.thumbnail ?? MISSING_IMAGE_REPLACEMENT_URL;
  }

  getRecency(favorite: Favorite): string {
    const secondsPassed = Math.floor((Date.now() - favorite.timestamp) / 1000);
    if (secondsPassed === 1) {
      return `1 second ago`;
    } else if (secondsPassed < 60) {
      return `${secondsPassed} seconds ago`;
    } else if (secondsPassed < 120) {
      return `1 minute ago`;
    } else if (secondsPassed < 3600) {
      return `${Math.floor(secondsPassed/60)} minutes ago`;
    } else if (secondsPassed < 7200) {
      return `1 hour ago`;
    } else if (secondsPassed < 3600 * 24) {
      return `${Math.floor(secondsPassed/3600)} hours ago`;
    } else if (secondsPassed < 3600 * 24 * 2) {
      return `1 day ago`;
    } else {
      return `${Math.floor(secondsPassed/(3600*24))} days ago`;
    }
  }

  ngOnDestroy() {
    clearInterval(this.recencyComputer);
  }

}
