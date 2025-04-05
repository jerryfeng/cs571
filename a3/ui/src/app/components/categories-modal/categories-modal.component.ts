import { Component, Input, OnChanges, signal, SimpleChanges } from '@angular/core';
import { API_ENDPOINT } from '../../app.constants';
import { ArtworkCategory } from '../../models/artwork-category';
import { Artwork } from '../../models/artwork';

@Component({
  selector: 'app-categories-modal',
  imports: [],
  template: `
    <div class="modal modal-lg fade" id="categoriesModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <div class="modal-title row">
              <img class="col-2" src={{artwork?.href}} alt={{artwork?.title}}>
              <div class="col-10">
                <p class="row mb-0">
                  {{artwork?.title}}
                </p>
                <p class="row mb-0">
                  {{artwork?.year}}
                </p>
              </div>
            </div>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            @if (loading()) {
              <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
              </div>
            } @else if (categories().length > 0) {
              <div class="row row-cols-1 row-cols-lg-4 g-3">
                @for (category of categories(); track $index) {
                  <div class="col">
                    <div class="card mb-1">
                      <img src={{category.href}} class="card-img-top" alt={{category.name}}>
                      <div class="card-body p-1">
                        <p class="card-title text-center ps-1">{{category.name}}</p>
                      </div>
                    </div>
                  </div>
                }
              </div>
            } @else if (noResults()) {
              <div class="alert alert-danger mt-2 text-start" role="alert">
                No categories.
              </div>
            }
          </div>
        </div>
      </div>
    </div>
  `,
  styleUrl: './categories-modal.component.css'
})
export class CategoriesModalComponent implements OnChanges {
  @Input() artwork?: Artwork = undefined;
  categories = signal<Array<ArtworkCategory>>([]);
  loading = signal<boolean>(false);
  noResults = signal<boolean>(false);

  async getCategories(): Promise<void> {
      if (!this.artwork) return;
      this.categories.set([]);
      this.loading.set(true);
      const response = await fetch(`${API_ENDPOINT}/categories/${this.artwork.id}`);
      const results = await response.json();
      const categories: Array<ArtworkCategory> = [];
      for (const result of results) {
        categories.push({
          name: result.name,
          href: result._links.thumbnail.href
        });
      }
      this.loading.set(false);
      if (categories.length === 0) this.noResults.set(true);
      this.categories.set(categories);
    }

  async ngOnChanges(changes: SimpleChanges): Promise<void> {
    await this.getCategories();
  }
}
