import { Component, effect, input, Input, OnChanges, signal, SimpleChanges } from '@angular/core';
import { ArtistInfoComponent } from "../artist-info/artist-info.component";
import { ArtworksComponent } from "../artworks/artworks.component";
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-artist-details',
  imports: [ArtistInfoComponent, ArtworksComponent, RouterLink],
  template: `
    <ul class="nav nav-pills nav-fill">
      <li class="nav-item">
        <a class="nav-link" routerLink={{tabLink(0)}} [class.active]="activeTab()===0" (click)="activeTab.set(0)">Artist Info</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" routerLink={{tabLink(1)}} [class.active]="activeTab()===1" (click)="activeTab.set(1)">Artworks</a>
      </li>
    </ul>
    <div class="mt-2"></div>
    @if (activeTab() === 0) {
      <app-artist-info [artistId]="artistId" />
    } @else if (activeTab() === 1) {
      <app-artworks [artistId]="artistId" />
    }
  `,
  styleUrl: './artist-details.component.css'
})
export class ArtistDetailsComponent implements OnChanges {
  @Input() artistId?: string;
  @Input() tab?: number;
  activeTab = signal<number>(0);

  tabLink(tab: number): string {
    const tabName = tab === 0 ? 'info' : 'artworks';
    return `/artists/${this.artistId}/${tabName}`;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.tab !== undefined) this.activeTab.set(this.tab);
  }

}
