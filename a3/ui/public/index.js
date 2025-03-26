'use strict';

const API_ENDPOINT = '/api/artists';
const MISSING_IMAGE_URL = '/assets/shared/missing_image.png';
const MISSING_IMAGE_REPLACEMENT_URL = '/static/artsy_logo.svg';

const footOnClickHandler = () => {
	window.location = 'https://www.artsy.net';
};

const addFooterOnClickHandler = () => {
	document.getElementById('footer').addEventListener('click', footOnClickHandler);
};

const querySearchOnClickHandler = () => {
	document.getElementById('search').requestSubmit();
};

const addQuerySearchOnClickHandler = () => {
	document.getElementById('query-search').addEventListener('click', querySearchOnClickHandler);
};

const queryClearOnClickHandler = () => {
	document.getElementById('query').value = '';
};

const addQueryClearOnClickHandler = () => {
	document.getElementById('query-clear').addEventListener('click', queryClearOnClickHandler);
};

const artistCardOnClickHandler = async (event) => {
	const artistCardElement = event.target.tagName === 'TD' ? event.target : event.target.parentNode;
	const artistDetail = document.getElementById('artist-detail');
	const artistBasic = document.getElementById('artist-basic');
	const artistNationality = document.getElementById('artist-nationality');
	const artistBiography = document.getElementById('artist-biography');
	const resultList = document.getElementById('result-list');
	artistBasic.innerHTML = '';
	artistNationality.innerHTML = '';
	artistBiography.innerHTML = '';
	artistDetail.hidden = true;
	artistDetail.style.display = 'none';
	loading.hidden = false;
	[...resultList.children].forEach((artistCard) => artistCard.removeAttribute('selected'));
	artistCardElement.setAttribute('selected', '');
	try {
		const response = await fetch(`${API_ENDPOINT}/${artistCardElement.getAttribute('artist-id')}`);
		if (!response.ok) {
			throw new Error(`Response status ${response.status}`);
		}

		const result = await response.json();
		const artist = {
			name: result.name || '',
			birthYear: result.birthday || '',
			deathYear: result.deathday || '',
			nationaility: result.nationality || '',
			biography: result.biography || ''
		};
		artistBasic.innerHTML = `${artist.name} (${artist.birthYear} - ${artist.deathYear})`;
		artistNationality.innerHTML = artist.nationaility;
		artistBiography.innerHTML = artist.biography;
		artistDetail.hidden = false;
		artistDetail.style.display = 'inline-block';
	} catch (error) {
		console.error(error.message)
	} finally {
		loading.hidden = true;
	}
};

const searchOnSubmitHandler = async (event) => {
	event.preventDefault();
	const query = document.getElementById('query').value;
	const loading = document.getElementById('loading');
	const noResults = document.getElementById('no-results');
	const artistDetail = document.getElementById('artist-detail');
	loading.hidden = false;
	noResults.hidden = true;
	noResults.style.display = 'none';
	artistDetail.hidden = true;
	artistDetail.style.display = 'none';
	try {
		const response = await fetch(`${API_ENDPOINT}?q=${query}`);
		if (!response.ok) {
			throw new Error(`Response status ${response.status}`);
		}
		
		const results = await response.json();
		const resultList = document.getElementById('result-list');
		resultList.innerHTML = '';
		if (results.length === 0) {
			noResults.hidden = false;
			noResults.style.display = 'inline-block';
		}
		results.forEach((artist) => {
			const artistImage = new Image();
			const artistImageLink = artist._links.thumbnail.href;
			const artistId = artist._links.self.href.split('/').reverse()[0];
			artistImage.src = artistImageLink !== MISSING_IMAGE_URL ? artistImageLink : MISSING_IMAGE_REPLACEMENT_URL;
			const artistName = document.createTextNode(artist.title);
			const artistCard = document.createElement('td');
			artistCard.setAttribute('artist-id', artistId);
			artistCard.appendChild(artistImage);
			artistCard.appendChild(artistName);
			resultList.appendChild(artistCard);
			artistCard.addEventListener('click', artistCardOnClickHandler);
		});

	} catch (error) {
		console.error(error.message)
	} finally {
		loading.hidden = true;
	}
};

const addSearchOnSubmitHandler = () => {
	document.getElementById('search').addEventListener('submit', searchOnSubmitHandler);
};

window.addEventListener('load', () => {
	addFooterOnClickHandler();
	addQuerySearchOnClickHandler();
	addSearchOnSubmitHandler();
	addQueryClearOnClickHandler();
});
