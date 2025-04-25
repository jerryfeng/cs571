const API_ENDPOINT = 'https://api.artsy.net/api';

// I should really store these in some secure place, but since it's not paid...
const CLIENT_ID = '4a4d4b425efbc6d1def8';
const CLIENT_SECRET = '0b9c5bb933e3643e3c530796f03eb691';
const tokenRequestBody = {
	'client_id' : CLIENT_ID,
	'client_secret' : CLIENT_SECRET
};

let token = undefined;

const getToken = async () => {
	const oneDayFromNow = new Date();
	oneDayFromNow.setDate(oneDayFromNow.getDate() + 1);
	if (token === undefined || new Date(token.expires_at) < oneDayFromNow) {
		const response = await fetch(`${API_ENDPOINT}/tokens/xapp_token`, {
			method: 'POST',
			body: JSON.stringify(tokenRequestBody),
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json'
			}
		});

		if (!response.ok) {
			throw new Error(`Response status ${response.status}`);
		}

		const result = await response.json();
		if (result.type === 'xapp_token') {
			token = result;
		} else {
			throw new Error('failed to renew Artsy token');
		}
	}
	return token;
};

const getAuthHeader = async () => {
	token = await getToken();
	return {
		'X-XAPP-Token': token.token
	};
};

export const searchArtists = async (q) => {
	const params = {
		q,
		size : 10,
		type : 'artist'
	};
	const response = await fetch(
		`${API_ENDPOINT}/search?${new URLSearchParams(params).toString()}`,
		{ headers: await getAuthHeader() }
	);

	if (!response.ok) {
		throw new Error(`Response status ${response.status}`);
	}

	const results = (await response.json())._embedded.results;

	const artists = [];
	results.forEach(result => {
		artists.push({
			id: result._links.self.href.split('/').pop(),
			title: result.title,
			thumbnail: result._links.thumbnail.href
		});
	});
	
	return artists;
};

export const searchSimilarArtists = async (artistId) => {
	const params = {
		size: 10,
		similar_to_artist_id: artistId
	}

	const response = await fetch(
		`${API_ENDPOINT}/artists?${new URLSearchParams(params).toString()}`,
		{ headers: await getAuthHeader() }
	);

	if (!response.ok) {
		throw new Error(`Response status ${response.status}`);
	}

	const results = await response.json();

	return results._embedded.artists;
}

export const getArtist = async (id) => {
	const response = await fetch(
		`${API_ENDPOINT}/artists/${id}`,
		{ headers: await getAuthHeader() }
	);

	if (!response.ok) {
		throw new Error(`Response status ${response.status}`);
	}

	const result = await response.json();
	return {
		name: result.name,
		birthYear: result.birthday,
		deathYear: result.deathday,
		nationality: result.nationality,
		biography: result.biography.split('\n')
	};
};

export const searchArtworks = async (artist_id) => {
	const params = {
		artist_id,
		size : 10
	};
	const response = await fetch(
		`${API_ENDPOINT}/artworks?${new URLSearchParams(params).toString()}`,
		{ headers: await getAuthHeader() }
	);

	if (!response.ok) {
		throw new Error(`Response status ${response.status}`);
	}

	const results = await response.json();
	return results._embedded.artworks;
};

export const searchCategories = async (artwork_id) => {
	const params = {
		artwork_id
	};
	const response = await fetch(
		`${API_ENDPOINT}/genes?${new URLSearchParams(params).toString()}`,
		{ headers: await getAuthHeader() }
	);

	if (!response.ok) {
		throw new Error(`Response status ${response.status}`);
	}

	const results = await response.json();
	return results._embedded.genes;
};
