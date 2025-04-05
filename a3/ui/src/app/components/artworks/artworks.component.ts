import { Component, Input, OnChanges, signal, SimpleChanges } from '@angular/core';
import { API_ENDPOINT } from '../../app.constants';
import { Artwork } from '../../models/artwork';
import { CategoriesModalComponent } from "../categories-modal/categories-modal.component";

@Component({
  selector: 'app-artworks',
  imports: [CategoriesModalComponent],
  template: `
    @if (loading()) {
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    }
    @if (artworks().length > 0) {
      <div class="row row-cols-1 row-cols-lg-4 g-3 mb-5">
        @for (artwork of artworks(); track $index) {
          <div class="col">
            <div class="card mb-1">
              <img src={{artwork.href}} class="card-img-top" alt={{artwork.title}}>
              <div class="card-body p-1">
                <p class="card-title text-center ps-1">{{artwork.title}}, {{artwork.year}}</p>
              </div>
              <div class="card-footer" data-bs-toggle="modal" data-bs-target="#categoriesModal" (click)="openCategoriesModal(artwork)">
                View categories
              </div>
            </div>
          </div>
        }
      </div>
    }
    @if (noResults()) {
      <div class="alert alert-danger mt-2 text-start" role="alert">
        No artworks.
      </div>
    }
    <app-categories-modal [artwork]="selectedArtwork()" />
  `,
  styleUrl: './artworks.component.css'
})
export class ArtworksComponent implements OnChanges {
  @Input() artistId?: string = '';
  loading = signal<boolean>(false);
  artworks = signal<Array<Artwork>>([]);
  noResults = signal<boolean>(false);
  selectedArtwork = signal<Artwork | undefined>(undefined);

  async getArtworks(): Promise<void> {
    if (!this.artistId) return;
    this.artworks.set([]);
    this.loading.set(true);
    const response = await fetch(`${API_ENDPOINT}/artworks/${this.artistId}`);
    const results = await response.json();
    const artworks: Array<Artwork> = [];
    for (const result of results) {
      artworks.push({
        id: result.id,
        href: result._links.thumbnail.href,
        title: result.title,
        year: result.date
      });
    }
    this.loading.set(false);
    if (artworks.length === 0) this.noResults.set(true);
    this.artworks.set(artworks);
  }

  openCategoriesModal(artwork: Artwork): void {
    this.selectedArtwork.set(artwork);
  }

  async ngOnChanges(changes: SimpleChanges): Promise<void> {
    await this.getArtworks();
  }
}
