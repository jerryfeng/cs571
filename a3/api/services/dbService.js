import { MongoClient, ServerApiVersion } from "mongodb";
import bcrypt from "bcrypt";
import crypto from "crypto";
import { getArtist } from "./artsyService.js";

/*
free tier user; don't care much about leaking credentials
const USERNAME = 'b9feng';
const PASSWORD = '1CDlBMARFG8e3aol';
*/

const CONNECTION_STRING = 'mongodb+srv://b9feng:1CDlBMARFG8e3aol@cs571.ya7ygzi.mongodb.net/?retryWrites=true&w=majority&appName=CS571';

const client = new MongoClient(CONNECTION_STRING, {
	serverApi: {
	  version: ServerApiVersion.v1,
	  strict: true,
	  deprecationErrors: true,
	}
});

export class ExistingUserError extends Error {
	message = 'User with this email already exists.';
};

export const createUser = async (registration) => {
	const db =  client.db('a3');
	const users = db.collection('users');

	const existingUser = await users.findOne({email: registration.email});
	if (existingUser !== null) {
		throw new ExistingUserError()
	}

	const saltRounds = 10;
	const hash = bcrypt.hashSync(registration.password, saltRounds);

	const trimmedEmail = registration.email.trim().toLowerCase();
	const emailHash = crypto.createHash('sha256').update(trimmedEmail).digest('hex');
	const avatar = `https://www.gravatar.com/avatar/${emailHash}?s=200&d=identicon`;

	const doc = {
		email: registration.email,
		fullname: registration.fullname,
		hash,
		avatar
	};

	await users.insertOne(doc);

	const user = {
		email: registration.email,
		fullname: registration.fullname,
		avatar
	};
	return user;
}

export const getUser = async (credentials) => {
	const db =  client.db('a3');
	const users = db.collection('users');

	const doc = await users.findOne({
		email: credentials.email,
	});

	if (doc === null) {
		throw new WrongCredentialsError();
	}

	if (! await bcrypt.compare(credentials.password, doc.hash)) {
		throw new WrongCredentialsError();
	}

	const user = {
		email: doc.email,
		fullname: doc.fullname,
		avatar: doc.avatar
	};

	return user;
}

export class WrongCredentialsError extends Error {
	message = 'Password or email is incorrect.';
};

export const deleteUser = async (email) => {
	const db =  client.db('a3');
	const users = db.collection('users');
	const favorites = db.collection('favorites');
	
	await favorites.deleteMany({
		email
	});

	await users.deleteOne({
		email
	});
}

export const addFavorite = async (email, artistId) => {
	const artist = await getArtist(artistId);
	const artistDetail = {
		name: artist.name,
		birthYear: artist.birthday,
		deathYear: artist.deathday,
		nationality: artist.nationality,
		thumbnail: artist._links?.thumbnail?.href ?? null
	};

	const db =  client.db('a3');
	const favorites = db.collection('favorites');
	const timestamp = Date.now();

	await favorites.insertOne({
		email,
		artistId,
		artistDetail,
		timestamp
	});
}

export const removeFavorite = async (email, artistId) => {
	const db =  client.db('a3');
	const favorites = db.collection('favorites');

	await favorites.deleteOne({
		email,
		artistId
	});
}

export const getFavorites = async (email) => {
	const db =  client.db('a3');
	const favorites = db.collection('favorites');

	const cursor = favorites.find({ email });
	const userFavorites = [];

	for await (const doc of cursor) {
		userFavorites.push(doc);
	}

	return userFavorites;
}
