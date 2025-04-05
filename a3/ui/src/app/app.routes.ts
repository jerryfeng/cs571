import { Router, Routes } from '@angular/router';
import { ArtistDetailsComponent } from './components/artist-details/artist-details.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { SearchComponent } from './components/search/search.component';
import { LoginComponent } from './components/login/login.component';
import { FavoritesComponent } from './components/favorites/favorites.component';
import { inject } from '@angular/core';
import { LoginService } from './services/login.service';

const loggedInOnlyFilter = (): boolean => {
	const loginService = inject(LoginService);
	if (!loginService.isLoggedIn()) {
		const router = inject(Router);
		router.navigateByUrl('/');
		return false;
	} else {
		return true;
	}
}

const guestOnlyFilter = (): boolean => {
	const loginService = inject(LoginService);
	if (loginService.isLoggedIn()) {
		const router = inject(Router);
		router.navigateByUrl('/');
		return false;
	} else {
		return true;
	}
}

export const routes: Routes = [
	{
		path: 'register',
		canActivate: [guestOnlyFilter],
		component: RegistrationComponent
	},
	{
		path: 'login',
		canActivate: [guestOnlyFilter],
		component: LoginComponent
	},
	{
		path: 'favorites',
		canActivate: [loggedInOnlyFilter],
		component: FavoritesComponent
	},
	{
		path: '',
		component: SearchComponent,
		children: [
			{
				path: 'artists/:artistId/info',
				data: { tab: 0 },
				component: ArtistDetailsComponent
			},
			{
				path: 'artists/:artistId/artworks',
				data: { tab: 1 },
				component: ArtistDetailsComponent
			}
		]
	}
];
