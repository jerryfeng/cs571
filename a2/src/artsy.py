import requests
import datetime
import urllib.parse

API_ENDPOINT = 'https://api.artsy.net/api'

# I should really store these in some secure place, but since it's not paid...
clientId = '24adb7b0b40e62927c69'
clientSecret = '8e88ebd6787c320180761a8e9ec1c935'
tokenRequestBody = {
	'client_id' : clientId,
	'client_secret' : clientSecret
}

token = None

def getToken():
	global token
	oneDayFromNow = datetime.datetime.now(datetime.timezone.utc) + datetime.timedelta(days=1)
	if not token or datetime.datetime.fromisoformat(token['expires_at']) < oneDayFromNow:
		result = requests.post(f'{API_ENDPOINT}/tokens/xapp_token', json=tokenRequestBody).json()
		if (result['type'] == 'xapp_token'):
			token = result
		else:
			raise Exception('failed to renew Artsy token')
	return token

def getAuthHeader():
	t = getToken()
	return {
		'X-XAPP-Token': t['token']
	}

def search(query):
	params = {
		'q' : query,
		'size' : 10,
		'type' : 'artist'
	}
	results = requests.get(
		url=f'{API_ENDPOINT}/search?{urllib.parse.urlencode(params)}',
		headers=getAuthHeader()
	).json()['_embedded']['results']
	return results

def getArtistDetail(id):
	results = requests.get(
		url=f'{API_ENDPOINT}/artists/{id}',
		headers=getAuthHeader()
	).json()
	return results
