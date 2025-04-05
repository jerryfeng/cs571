export interface Favorite {
	artistId: string;
	artistDetail: {
		name: string;
		birthYear: number;
		deathYear: number;
		nationality: string;
		thumbnail: string | null;
	};
	timestamp: number;
}
