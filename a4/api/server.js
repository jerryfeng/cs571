'use strict';

import express from 'express';
import cors from 'cors';
import { getArtist, searchArtists, searchArtworks, searchCategories, searchSimilarArtists } from './services/artsyService.js';
import { addFavorite, createUser, deleteUser, ExistingUserError, getFavorites, getUser, removeFavorite, WrongCredentialsError } from './services/dbService.js';
import bodyParser from 'body-parser';
import cookieParser from 'cookie-parser';
import jwt from "jsonwebtoken";

// This is insecure, but hey, I am only doing an assignment...
const JWT_SECRET = 'I_love_CS571';

const app = express();
const BASE_URL = '/api';

app.use(cors({
	origin: 'http://localhost:4200',
	credentials: true
}));
app.use('/static', express.static('static'));
app.use(bodyParser.json());
app.use(cookieParser());

app.get(`${BASE_URL}/`, (req, res) => {
	res.json({version: '1.0.0'});
});

app.get(`${BASE_URL}/artists`, async (req, res) => {
	try {
		const results = await searchArtists(req.query.q);
		res.json(results);
	} catch (error) {
		console.error(error);
		res.status(500);
		res.json({message: 'Failed to search artists. Artsy API is likely down.'});
	}
});

app.get(`${BASE_URL}/artists/:artistId`, async (req, res) => {
	try {
		const result = await getArtist(req.params.artistId);
		res.json(result);
	} catch (error) {
		console.error(error);
		res.status(500);
		res.json({message: 'Failed to get artist detail. Artsy API is likely down.'});
	}
});

app.get(`${BASE_URL}/artists/similar/:artistId`, async (req, res) => {
	try {
		const results = await searchSimilarArtists(req.params.artistId);
		res.json(results);
	} catch (error) {
		console.error(error);
		res.status(500);
		res.json({message: 'Failed to get similar artists. Artsy API is likely down.'});
	}
});

app.get(`${BASE_URL}/artworks/:artistId`, async (req, res) => {
	try {
		const results = await searchArtworks(req.params.artistId);
		res.json(results);
	} catch (error) {
		console.error(error);
		res.status(500);
		res.json({message: 'Failed to get artwork. Artsy API is likely down.'});
	}
});

app.get(`${BASE_URL}/categories/:artworkId`, async (req, res) => {
	try {
		const results = await searchCategories(req.params.artworkId);
		res.json(results);
	} catch (error) {
		console.error(error);
		res.status(500);
		res.json({message: 'Failed to get artwork categories. Artsy API is likely down.'});
	}
});

app.post(`${BASE_URL}/users`, async (req, res) => {
	try {
		const user = await createUser(req.body);
		const token = jwt.sign(user, JWT_SECRET, {expiresIn: '1h'});
		res.cookie(
			'token',
			token,
			{
				httpOnly: true,
				maxAge: 3600 * 1000
			}
		);
		res.json(user);
	} catch (error) {
		if (error instanceof ExistingUserError) {
			res.status(400);
			res.json({
				email: req.body.email,
				message: error.message
			});
		}
		console.error(error);
	}
});

app.get(`${BASE_URL}/me`, async (req, res) => {
	const token = req.cookies.token;
	if (!token) {
		res.status(401);
		return res.json({message: 'User not logged in.'});
	}
	const user = jwt.verify(token, JWT_SECRET);
	res.json(user);
});

app.post(`${BASE_URL}/login`, async (req, res) => {
	try {
		const user = await getUser(req.body);
		const token = jwt.sign(user, JWT_SECRET, {expiresIn: '1h'});
		res.cookie(
			'token',
			token,
			{
				httpOnly: true,
				maxAge: 3600 * 1000
			}
		);
		res.json(user);
	} catch (error) {
		if (error instanceof WrongCredentialsError) {
			res.status(401);
			res.json({message: error.message});
		}
		console.error(error);
	}
});

app.get(`${BASE_URL}/logout`, async (req, res) => {
	res.clearCookie(
		'token',
		{
			httpOnly: true
		}
	);
	res.json({message: 'Logged out.'});
});

app.delete(`${BASE_URL}/me`, async (req, res) => {
	const token = req.cookies.token;
	if (!token) {
		res.status(401);
		return res.json({message: 'User not logged in.'});
	}
	const user = jwt.verify(token, JWT_SECRET);
	try {
		await deleteUser(user.email);
		res.clearCookie(
			'token',
			{
				httpOnly: true
			}
		);
		res.json({message: 'User deleted.'});
	} catch (error) {
		console.error(error);
		res.status(500);
		res.json({message: 'Failed to delete user'});
	}
});

app.post(`${BASE_URL}/favorites`, async (req, res) => {
	const token = req.cookies.token;
	if (!token) {
		res.status(401);
		return res.json({message: 'User not logged in.'});
	}
	const user = jwt.verify(token, JWT_SECRET);
	try {
		await addFavorite(user.email, req.body.artistId);
		res.json({message: 'Added to favorites.'});
	} catch (error) {
		console.error(error);
		res.status(500);
		res.json({message: 'Failed to add favorite'});
	}
});

app.delete(`${BASE_URL}/favorites`, async (req, res) => {
	const token = req.cookies.token;
	if (!token) {
		res.status(401);
		return res.json({message: 'User not logged in.'});
	}
	const user = jwt.verify(token, JWT_SECRET);
	try {
		await removeFavorite(user.email, req.body.artistId);
		res.json({message: 'Removed from favorites.'});
	} catch (error) {
		console.error(error);
		res.status(500);
		res.json({message: 'Failed to remove favorite'});
	}
});

app.get(`${BASE_URL}/favorites`, async (req, res) => {
	const token = req.cookies.token;
	if (!token) {
		res.status(401);
		return res.json({message: 'User not logged in.'});
	}
	const user = jwt.verify(token, JWT_SECRET);
	try {
		const favorites = await getFavorites(user.email);
		res.json(favorites);
	} catch (error) {
		console.error(error);
		res.status(500);
		res.json({message: 'Failed to get favorites'});
	}
});

// Listen to the App Engine-specified port, or 8080 otherwise
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
	console.log(`Server listening on port ${PORT}...`);
});
