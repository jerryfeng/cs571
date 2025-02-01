from .artsy import search, getArtistDetail

def getArtists(query):
	response = search(query.strip())
	return response

def getArtist(id):
	response = getArtistDetail(id.strip())
	return response
