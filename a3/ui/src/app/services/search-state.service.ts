import { effect, Injectable, signal } from '@angular/core';
import { Artist } from '../models/artist';

@Injectable({
  providedIn: 'root'
})
export class SearchStateService {

  clear = signal<number>(0);

  searchResults = signal<Array<Artist>>([]);

  selectedArtistId = signal<string>('');

  constructor() {
    effect(() =>{
      const _clear = this.clear();
      this.searchResults.set([]);
    });
    effect(() => {
      const searchResults = this.searchResults();
    });
  }
}
