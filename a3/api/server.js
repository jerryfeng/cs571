'use strict';

import express from 'express';
import cors from 'cors';
import { searchArtist } from './services/artsyService.js';

const app = express();

app.use(cors());
app.use('/static', express.static('static'));

app.get('/', (req, res) => {
	res.json({version: '1.0.0'});
});

app.get('/artists', async (req, res) => {
	const results = await searchArtist(req.query.q);
	res.json(results);
});

// Listen to the App Engine-specified port, or 8080 otherwise
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
	console.log(`Server listening on port ${PORT}...`);
});
