import { effect, Injectable, signal } from '@angular/core';
import { API_ENDPOINT } from '../app.constants';
import { LoginService } from './login.service';
import { Favorite } from '../models/favorite';
import { NotificationsService } from './notifications.service';

@Injectable({
  providedIn: 'root'
})
export class FavoritesService {
  private favorites = signal<Array<Favorite>>([]);
  public loading = signal<boolean>(true);

  public getFavorites(): Array<Favorite> {
    return this.favorites();
  }

  public getIds(): Array<string> {
    const ids = this.favorites().map(favorite => favorite.artistId);
    return ids;
  }

  public async addFavorite(artistId: string): Promise<void> {
    await this.updateFavorite(artistId, 'POST');
    this.notificationsService.createNotification('Added to favorites', 'success');
  }

  public async removeFavorite(artistId: string): Promise<void> {
    await this.updateFavorite(artistId, 'DELETE');
    this.notificationsService.createNotification('Removed from favorites', 'danger');
  }

  private async _getFavorites(): Promise<void> {
    const response = await fetch(`${API_ENDPOINT}/favorites`, {
      credentials: 'include'
    });
    const results = await response.json();
    this.favorites.set(results);
    this.loading.set(false);
  }

  private async updateFavorite(artistId: string, method: string): Promise<void> {
    const response = await fetch(`${API_ENDPOINT}/favorites`, {
      credentials: 'include',
      method,
      body: JSON.stringify({email: this.loginService.user().email, artistId}),
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    });
    if (response.ok) {
      await this._getFavorites();
    }
  } 

  constructor(public loginService: LoginService, public notificationsService: NotificationsService) {
    effect(async () => {
      if (loginService.isLoggedIn()) {
        this._getFavorites();
      } else {
        this.favorites.set([]);
      }
    });
  }
}
