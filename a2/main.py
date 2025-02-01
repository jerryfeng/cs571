from flask import Flask, render_template, request
from src import artistsController

app = Flask(__name__)

@app.route('/')
def home():
	return render_template('index.html')

@app.get('/api/artists')
def artists():
	try:
		query = request.args.get('q')
		return artistsController.getArtists(query)
	except Exception as e:
		app.logger.error(e)
		return 'Internal Server Error', 500

@app.get('/api/artists/<id>')
def artist(id):
	try:
		return artistsController.getArtist(id)
	except Exception as e:
		app.logger.error(e)
		return 'Internal Server Error', 500

if __name__ == '__main__':
	app.run(host="127.0.0.1", port=8080, debug=True)
