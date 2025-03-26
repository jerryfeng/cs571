const API_ENDPOINT = 'https://api.artsy.net/api';

// I should really store these in some secure place, but since it's not paid...
const CLIENT_ID = '24adb7b0b40e62927c69';
const CLIENT_SECRET = '8e88ebd6787c320180761a8e9ec1c935';
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

export const searchArtist = async (q) => {
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

	const results = await response.json();
	
	return results._embedded.results;
};
