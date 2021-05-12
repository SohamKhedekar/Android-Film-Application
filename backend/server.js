// [START gae_node_request_example]
const express = require('express');
const axios = require('axios');
const cors = require('cors')
const validUrl = require('valid-url');
const path = require('path');

const app = express();
app.use(cors());

app.get('/', (req, res) => {
	res.status(200).send('HW9 boi!').end();
});

app.get('/apis/search/', (req, res) => {
	if (!req.query.hasOwnProperty("search_query") || req.query.search_query.length == 0) {
		res.status(200).send('No results').end();
		return;
	}
	axios.get('https://api.themoviedb.org/3/search/multi?api_key=63bbdaaeac235ced1deca83902eb462a&language=en-US&query=' + req.query.search_query)
	.then(response => {
		var data = response.data;
		var filtered_data = [];
		//filtered_data["results"] = [];
		var i = 0;
		while (filtered_data.length < 20 && i < data["results"].length) {
			if (data["results"][i]["media_type"] == "tv" || data["results"][i]["media_type"] == "movie") {
				var temp = {};
				if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
					temp["id"] = data["results"][i]["id"];
				}
				temp["media_type"] = data["results"][i]["media_type"];
				if (temp["media_type"] == "tv") {
					if (data["results"][i].hasOwnProperty("name") && data["results"][i]["name"] != null && data["results"][i]["name"] != "") {
						temp["name"] = data["results"][i]["name"];
					}
					if (data["results"][i].hasOwnProperty("first_air_date") && data["results"][i]["first_air_date"] != null && data["results"][i]["first_air_date"] != "") {
						temp["release_date"] = data["results"][i]["first_air_date"];
					}
				}
				else {
					if (data["results"][i].hasOwnProperty("title") && data["results"][i]["title"] != null && data["results"][i]["title"] != "") {
						temp["name"] = data["results"][i]["title"];
					}
					if (data["results"][i].hasOwnProperty("release_date") && data["results"][i]["release_date"] != null && data["results"][i]["release_date"] != "") {
						temp["release_date"] = data["results"][i]["release_date"];
					}
				}
				if (data["results"][i].hasOwnProperty("vote_average") && data["results"][i]["vote_average"] != null && data["results"][i]["vote_average"] != "") {
					temp["vote_average"] = data["results"][i]["vote_average"];
				}
				if (data["results"][i].hasOwnProperty("backdrop_path") && data["results"][i]["backdrop_path"] != null && data["results"][i]["backdrop_path"] != ""
				&& data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
					temp["backdrop_path"] = data["results"][i]["backdrop_path"];
					filtered_data.push(temp);
				}
			}
			i++;
		}
		res.status(200).send(filtered_data).end();
    })
  	.catch(error => {
    	console.log(error);
  	});
})

app.get('/apis/trendingMovies', (req, res) => {
	axios.get('https://api.themoviedb.org/3/trending/movie/day?api_key=63bbdaaeac235ced1deca83902eb462a')
	.then(response => {
		var data = response.data;
		var filtered_data = [];
		var i = 0;
		var data_set = [];
		while (i < data["results"].length) {
			var temp = {};
			if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
				temp["id"] = data["results"][i]["id"];
			}
			if (data["results"][i].hasOwnProperty("title") && data["results"][i]["title"] != null && data["results"][i]["title"] != "") {
				temp["title"] = data["results"][i]["title"];
			}
			if (data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
				temp["poster_path"] = data["results"][i]["poster_path"];
				data_set.push(temp);
			}
			if (data_set.length == 6) {
				filtered_data.push(data_set);
				data_set = [];
			}
			i++;
		}
		if (data_set.length < 6 && data_set.length > 0) {
			while(data_set.length != 6) {
				data_set.push({});
			}
			filtered_data.push(data_set);
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/topRatedMovies', (req, res) => {
	axios.get('https://api.themoviedb.org/3/movie/top_rated?api_key=63bbdaaeac235ced1deca83902eb462a&language=enUS&page=1')
	.then(response => {
		var data = response.data;
		var filtered_data = {};
		filtered_data["results"] = [];
		var i = 0;
		while(i < data["results"].length && filtered_data["results"].length < 10) {
			var temp = {};
			temp["type"] = "Movie";
			if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
				temp["id"] = data["results"][i]["id"];
			}
			if (data["results"][i].hasOwnProperty("title") && data["results"][i]["title"] != null && data["results"][i]["title"] != "") {
				temp["title"] = data["results"][i]["title"];
			}
			if (data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
				temp["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
				filtered_data["results"].push(temp);
			}
			i++;
		}
		res.status(200).send(filtered_data).end();
		// var data = response.data;
		// var filtered_data = [];
		// var i = 0;
		// var data_set = [];
		// while (i < data["results"].length) {
		// 	var temp = {};
		// 	if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
		// 		temp["id"] = data["results"][i]["id"];
		// 	}
		// 	if (data["results"][i].hasOwnProperty("title") && data["results"][i]["title"] != null && data["results"][i]["title"] != "") {
		// 		temp["title"] = data["results"][i]["title"];
		// 	}
		// 	if (data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
		// 		temp["poster_path"] = data["results"][i]["poster_path"];
		// 		data_set.push(temp);
		// 	}
		// 	if (data_set.length == 6) {
		// 		filtered_data.push(data_set);
		// 		data_set = [];
		// 	}
		// 	i++;
		// }
		// if (data_set.length < 6 && data_set.length > 0) {
		// 	while(data_set.length != 6) {
		// 		data_set.push({});
		// 	}
		// 	filtered_data.push(data_set);
		// }
		// res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/currentlyPlayingMovies', (req, res) => {
	axios.get('https://api.themoviedb.org/3/movie/now_playing?api_key=63bbdaaeac235ced1deca83902eb462a&language=enUS&page=1')
	.then(response => {
		var data = response.data;
		var filtered_data = {};
		filtered_data["results"] = [];
		var i = 0;
		while(filtered_data["results"].length < 6 && i < data["results"].length) {
			var temp = {};
			temp["type"] = "Movie";
			if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
				temp["id"] = data["results"][i]["id"];
			}
			if (data["results"][i].hasOwnProperty("title") && data["results"][i]["title"] != null && data["results"][i]["title"] != "") {
				temp["title"] = data["results"][i]["title"];
			}
			if (data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
				temp["poster_path"] = data["results"][i]["poster_path"];
				filtered_data["results"].push(temp);
			}
			i++;
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/popularMovies', (req, res) => {
	axios.get('https://api.themoviedb.org/3/movie/popular?api_key=63bbdaaeac235ced1deca83902eb462a&language=enUS&page=1')
	.then(response => {
		var data = response.data;
		var filtered_data = {};
		filtered_data["results"] = [];
		var i = 0;
		while(i < data["results"].length  && filtered_data["results"].length < 10) {
			var temp = {};
			temp["type"] = "Movie";
			if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
				temp["id"] = data["results"][i]["id"];
			}
			if (data["results"][i].hasOwnProperty("title") && data["results"][i]["title"] != null && data["results"][i]["title"] != "") {
				temp["title"] = data["results"][i]["title"];
			}
			if (data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
				temp["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
				filtered_data["results"].push(temp);
			}
			i++;
		}
		res.status(200).send(filtered_data).end();
		// var data = response.data;
		// var filtered_data = [];
		// var i = 0;
		// var data_set = [];
		// while (i < data["results"].length) {
		// 	var temp = {};
		// 	if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
		// 		temp["id"] = data["results"][i]["id"];
		// 	}
		// 	if (data["results"][i].hasOwnProperty("title") && data["results"][i]["title"] != null && data["results"][i]["title"] != "") {
		// 		temp["title"] = data["results"][i]["title"];
		// 	}
		// 	if (data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
		// 		temp["poster_path"] = data["results"][i]["poster_path"];
		// 		data_set.push(temp);
		// 	}
		// 	if (data_set.length == 6) {
		// 		filtered_data.push(data_set);
		// 		data_set = [];
		// 	}
		// 	i++;
		// }
		// if (data_set.length < 6 && data_set.length > 0) {
		// 	while(data_set.length != 6) {
		// 		data_set.push({});
		// 	}
		// 	filtered_data.push(data_set);
		// }
		// res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/recommendedMovies/', (req, res) => {
	axios.get('https://api.themoviedb.org/3/movie/' + req.query.movie_id + '/recommendations?api_key=63bbdaaeac235ced1deca83902eb462a&language=en-US&page=1')
	.then(response => {
		var data = response.data;
		var filtered_data = {};
		if (!data.hasOwnProperty("results") || data["results"].length == 0 || data["results"] == null || data["results"] == "") {
			res.status(200).send(filtered_data).end();
			return;
		}
		filtered_data["results"] = [];
		var i = 0;
		while(i < data["results"].length  && filtered_data["results"].length < 10) {
			var temp = {};
			temp["type"] = "Movie";
			if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
				temp["id"] = data["results"][i]["id"];
			}
			if (data["results"][i].hasOwnProperty("title") && data["results"][i]["title"] != null && data["results"][i]["title"] != "") {
				temp["title"] = data["results"][i]["title"];
			}
			if (data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
				temp["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
				filtered_data["results"].push(temp);
			}
			i++;
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/similarMovies/', (req, res) => {
	var filtered_data = [];
	if (!req.query.hasOwnProperty("movie_id") || req.query.movie_id.length == 0) {
		res.status(200).send(filtered_data).end();
		return;
	}
	axios.get('https://api.themoviedb.org/3/movie/' + req.query.movie_id + '/similar?api_key=63bbdaaeac235ced1deca83902eb462a&language=en-US&page=1')
	.then(response => {
		var data = response.data;
		var i = 0;
		var data_set = [];
		while (i < data["results"].length) {
			var temp = {};
			if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
				temp["id"] = data["results"][i]["id"];
			}
			if (data["results"][i].hasOwnProperty("title") && data["results"][i]["title"] != null && data["results"][i]["title"] != "") {
				temp["title"] = data["results"][i]["title"];
			}
			if (data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
				temp["poster_path"] = data["results"][i]["poster_path"];
				data_set.push(temp);
			}
			if (data_set.length == 6) {
				filtered_data.push(data_set);
				data_set = [];
			}
			i++;
		}
		if (data_set.length < 6 && data_set.length > 0) {
			while(data_set.length != 6) {
				data_set.push({});
			}
			filtered_data.push(data_set);
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/moviesVideo/', (req, res) => {
	var filtered_data = {};
	if (!req.query.hasOwnProperty("movie_id") || req.query.movie_id.length == 0) {
		res.status(200).send(filtered_data).end();
		return;
	}
	axios.get('https://api.themoviedb.org/3/movie/' + req.query.movie_id + '/videos?api_key=63bbdaaeac235ced1deca83902eb462a&language=en-US&page=1')
	.then(response => {
		var data = response.data;
		for (var i = 0; i < data["results"].length; i++) {
			if (data["results"][i]["type"] == "Trailer") {
				if (data["results"][i].hasOwnProperty("site") && data["results"][i]["site"] != null && data["results"][i]["site"] != "") {
					filtered_data["site"] = data["results"][i]["site"];
				}
				if (data["results"][i].hasOwnProperty("type") && data["results"][i]["type"] != null && data["results"][i]["type"] != "") {
					filtered_data["type"] = data["results"][i]["type"];
				}
				if (data["results"][i].hasOwnProperty("name") && data["results"][i]["name"] != null && data["results"][i]["name"] != "") {
					filtered_data["name"] = data["results"][i]["name"];
				}
				if (data["results"][i].hasOwnProperty("key") && data["results"][i]["key"] != null && data["results"][i]["key"] != "") {
					filtered_data["key"] = data["results"][i]["key"];
				}
				res.status(200).send(filtered_data).end();
				return;
			}
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/movieDetails/', (req, res) => {
	var filtered_data = {};
	if (!req.query.hasOwnProperty("movie_id") || req.query.movie_id.length == 0) {
		res.status(200).send(filtered_data).end();
		return;
	}
	axios.get('https://api.themoviedb.org/3/movie/' + req.query.movie_id + '?api_key=63bbdaaeac235ced1deca83902eb462a&language=enUS&page=1')
	.then(response => {
		var data = response.data;
		filtered_data["type"] = "Movie";
		if (data.hasOwnProperty("id") && data["id"] != null && data["id"] != "") {
			filtered_data["id"] = data["id"];
		}
		if (data.hasOwnProperty("title") && data["title"] != null && data["title"] != "") {
			filtered_data["title"] = data["title"];
		}
		if (data.hasOwnProperty("genres") && data["genres"] != null && data["genres"] != "") {
			var gen = "";
			for (var k = 0; k < data["genres"].length; k++) {
				if (k == 0) {
					gen = data["genres"][k]["name"];
				}
				else {
					gen += ", " + data["genres"][k]["name"];
				}
			}
			filtered_data["genres"] = gen;
		}
		if (data.hasOwnProperty("spoken_languages") && data["spoken_languages"] != null && data["spoken_languages"] != "") {
			var sl = "";
			for (var k = 0; k < data["spoken_languages"].length; k++) {
				if (k == 0) {
					sl = data["spoken_languages"][k]["english_name"];
				}
				else {
					sl += ", " + data["spoken_languages"][k]["english_name"];
				}
			}
			filtered_data["spoken_languages"] = sl;
		}
		if (data.hasOwnProperty("release_date") && data["release_date"] != null && data["release_date"] != "") {
			filtered_data["release_date"] = data["release_date"].split("-")[0];
		}
		if (data.hasOwnProperty("runtime") && data["runtime"] != null && data["runtime"] != "") {
			const time = Number(data["runtime"]);
			var rtime = "";
			if (time-time%60 != 0) {
				rtime += (time-time%60)/60;
				if ((time-time%60)/60 == 1) {
					rtime += "hr ";
				}
				else {
					rtime += "hrs ";
				}
			}
			rtime += time%60 + "mins";
			filtered_data["runtime"] = rtime;
		}
		if (data.hasOwnProperty("overview") && data["overview"] != null && data["overview"] != "") {
			filtered_data["overview"] = data["overview"];
		}
		if (data.hasOwnProperty("vote_average") && data["vote_average"] != null && data["vote_average"] != "") {
			filtered_data["vote_average"] = data["vote_average"];
		}
		if (data.hasOwnProperty("tagline") && data["tagline"] != null && data["tagline"] != "") {
			filtered_data["tagline"] = data["tagline"];
		}
		if (data.hasOwnProperty("poster_path") && data["poster_path"] != null && data["poster_path"] != "") {
			filtered_data["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["poster_path"];
		}
		if (data.hasOwnProperty("backdrop_path") && data["backdrop_path"] != null && data["backdrop_path"] != "") {
			filtered_data["backdrop_path"] = "https://image.tmdb.org/t/p/w500" + data["backdrop_path"];
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/movieReviews/', (req, res) => {
	var filtered_data = {};
	if (!req.query.hasOwnProperty("movie_id") || req.query.movie_id.length == 0) {
		res.status(200).send(filtered_data).end();
		return;
	}
	axios.get('https://api.themoviedb.org/3/movie/' + req.query.movie_id + '/reviews?api_key=63bbdaaeac235ced1deca83902eb462a&language=en-US&page=1')
	.then(response => {
		var data = response.data;
		if ( data["results"].length == 0) {
			res.status(200).send(filtered_data).end();
			return;
		}
		filtered_data["results"] = [];
		for (var i = 0; i < Math.min(data["results"].length, 3); i++) {
			var temp = {};
			if (data["results"][i].hasOwnProperty("author") && data["results"][i]["author"] != null && data["results"][i]["author"] != "") {
				temp["author"] = data["results"][i]["author"];
			}
			if (data["results"][i].hasOwnProperty("content") && data["results"][i]["content"] != null && data["results"][i]["content"] != "") {
				temp["content"] = data["results"][i]["content"];
			}
			if (data["results"][i].hasOwnProperty("created_at") && data["results"][i]["created_at"] != null && data["results"][i]["created_at"] != "") {
				temp["created_at"] = new Date(data["results"][i]["created_at"]);
			}
			if (data["results"][i].hasOwnProperty("url") && data["results"][i]["url"] != null && data["results"][i]["url"] != "") {
				temp["url"] = data["results"][i]["url"];
			}
			if (data["results"][i]["author_details"].hasOwnProperty("rating") && data["results"][i]["author_details"]["rating"] != null && data["results"][i]["author_details"]["rating"] != "") {
				temp["rating"] = data["results"][i]["author_details"]["rating"];
			}
			else {
				temp["rating"] = 0;
			}
			if (data["results"][i]["author_details"].hasOwnProperty("avatar_path") && data["results"][i]["author_details"]["avatar_path"] != null && data["results"][i]["author_details"]["avatar_path"] != "") {
				if (validUrl.isUri(data["results"][i]["author_details"]["avatar_path"].substring(1))) {
					temp["avatar_path"] = data["results"][i]["author_details"]["avatar_path"].substring(1);
				}
				else {
					temp["avatar_path"] = "https://image.tmdb.org/t/p/original" + data["results"][i]["author_details"]["avatar_path"];
				}
			}
			else {
				temp["avatar_path"] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHnPmUvFLjjmoYWAbLTEmLLIRCPpV_OgxCVA&usqp=CAU";
			}
			filtered_data["results"].push(temp);
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/movieCast/', (req, res) => {
	var filtered_data = {};
	if (!req.query.hasOwnProperty("movie_id") || req.query.movie_id.length == 0) {
		res.status(200).send(filtered_data).end();
		return;
	}
	axios.get('https://api.themoviedb.org/3/movie/' + req.query.movie_id + '/credits?api_key=63bbdaaeac235ced1deca83902eb462a&language=en-US&page=1')
	.then(response => {
		var data = response.data;
		if (!data.hasOwnProperty("cast") || data["cast"] == null || data["cast"] == "" || data["cast"].length == 0) {
			res.status(200).send(filtered_data).end();
			return;
		}
		filtered_data["results"] = [];
		var i = 0;
		while (filtered_data["results"].length < 6 && filtered_data["results"].length < data["cast"].length) {
			var temp = {};
			if (data["cast"][i].hasOwnProperty("id") && data["cast"][i]["id"] != null && data["cast"][i]["id"] != "") {
				temp["id"] = data["cast"][i]["id"];
			}
			if (data["cast"][i].hasOwnProperty("name") && data["cast"][i]["name"] != null && data["cast"][i]["name"] != "") {
				temp["name"] = data["cast"][i]["name"];
			}
			if (data["cast"][i].hasOwnProperty("character") && data["cast"][i]["character"] != null && data["cast"][i]["character"] != "") {
				temp["character"] = data["cast"][i]["character"];
			}
			if (data["cast"][i].hasOwnProperty("profile_path") && data["cast"][i]["profile_path"] != null && data["cast"][i]["profile_path"] != "") {
				temp["profile_path"] = "https://image.tmdb.org/t/p/w500" + data["cast"][i]["profile_path"];
				filtered_data["results"].push(temp);
			}
			i++;
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/trendingTVShows', (req, res) => {
	axios.get('https://api.themoviedb.org/3/trending/tv/day?api_key=63bbdaaeac235ced1deca83902eb462a')
	.then(response => {
		var data = response.data;
		var filtered_data = {};
		filtered_data["results"] = [];
		var i = 0;
		while(filtered_data["results"].length < 6 && i < data["results"].length) {
			var temp = {};
			temp["type"] = "TV";
			if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
				temp["id"] = data["results"][i]["id"];
			}
			if (data["results"][i].hasOwnProperty("name") && data["results"][i]["name"] != null && data["results"][i]["name"] != "") {
				temp["title"] = data["results"][i]["name"];
			}
			if (data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
				temp["poster_path"] = data["results"][i]["poster_path"];
				filtered_data["results"].push(temp);
			}
			i++;
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/topRatedTVShows', (req, res) => {
	axios.get('https://api.themoviedb.org/3/tv/top_rated?api_key=63bbdaaeac235ced1deca83902eb462a&language=enUS&page=1')
	.then(response => {
		var data = response.data;
		var filtered_data = {};
		filtered_data["results"] = [];
		var i = 0;
		while(i < data["results"].length  && filtered_data["results"].length < 10) {
			var temp = {};
			temp["type"] = "TV";
			if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
				temp["id"] = data["results"][i]["id"];
			}
			if (data["results"][i].hasOwnProperty("name") && data["results"][i]["name"] != null && data["results"][i]["name"] != "") {
				temp["title"] = data["results"][i]["name"];
			}
			if (data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
				temp["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
				filtered_data["results"].push(temp);
			}
			i++;
		}
		res.status(200).send(filtered_data).end();
		// var data = response.data;
		// var filtered_data = [];
		// var i = 0;
		// var data_set = [];
		// while (i < data["results"].length) {
		// 	var temp = {};
		// 	if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
		// 		temp["id"] = data["results"][i]["id"];
		// 	}
		// 	if (data["results"][i].hasOwnProperty("name") && data["results"][i]["name"] != null && data["results"][i]["name"] != "") {
		// 		temp["title"] = data["results"][i]["name"];
		// 	}
		// 	if (data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
		// 		temp["poster_path"] = data["results"][i]["poster_path"];
		// 		data_set.push(temp);
		// 	}
		// 	if (data_set.length == 6) {
		// 		filtered_data.push(data_set);
		// 		data_set = [];
		// 	}
		// 	i++;
		// }
		// if (data_set.length < 6 && data_set.length > 0) {
		// 	while(data_set.length != 6) {
		// 		data_set.push({});
		// 	}
		// 	filtered_data.push(data_set);
		// }
		// res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/popularTVShows', (req, res) => {
	axios.get('https://api.themoviedb.org/3/tv/popular?api_key=63bbdaaeac235ced1deca83902eb462a&language=enUS&page=1')
	.then(response => {
		var data = response.data;
		var filtered_data = {};
		filtered_data["results"] = [];
		var i = 0;
		while(i < data["results"].length && filtered_data["results"].length < 10) {
			var temp = {};
			temp["type"] = "TV";
			if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
				temp["id"] = data["results"][i]["id"];
			}
			if (data["results"][i].hasOwnProperty("name") && data["results"][i]["name"] != null && data["results"][i]["name"] != "") {
				temp["title"] = data["results"][i]["name"];
			}
			if (data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
				temp["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
				filtered_data["results"].push(temp);
			}
			i++;
		}
		res.status(200).send(filtered_data).end();
		// var data = response.data;
		// var filtered_data = [];
		// var i = 0;
		// var data_set = [];
		// while (i < data["results"].length) {
		// 	var temp = {};
		// 	if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
		// 		temp["id"] = data["results"][i]["id"];
		// 	}
		// 	if (data["results"][i].hasOwnProperty("name") && data["results"][i]["name"] != null && data["results"][i]["name"] != "") {
		// 		temp["title"] = data["results"][i]["name"];
		// 	}
		// 	if (data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
		// 		temp["poster_path"] = data["results"][i]["poster_path"];
		// 		data_set.push(temp);
		// 	}
		// 	if (data_set.length == 6) {
		// 		filtered_data.push(data_set);
		// 		data_set = [];
		// 	}
		// 	i++;
		// }
		// if (data_set.length < 6 && data_set.length > 0) {
		// 	while(data_set.length != 6) {
		// 		data_set.push({});
		// 	}
		// 	filtered_data.push(data_set);
		// }
		// res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/recommendedTVShows/', (req, res) => {
	axios.get('https://api.themoviedb.org/3/tv/' + req.query.tv_show_id + '/recommendations?api_key=63bbdaaeac235ced1deca83902eb462a&language=en-US&page=1')
	.then(response => {
		var data = response.data;
		var filtered_data = {};
		if (!data.hasOwnProperty("results") || data["results"].length == 0 || data["results"] == null || data["results"] == "") {
			res.status(200).send(filtered_data).end();
			return;
		}
		filtered_data["results"] = [];
		var i = 0;
		while(i < data["results"].length && filtered_data["results"].length < 10) {
			var temp = {};
			temp["type"] = "TV";
			if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
				temp["id"] = data["results"][i]["id"];
			}
			if (data["results"][i].hasOwnProperty("name") && data["results"][i]["name"] != null && data["results"][i]["name"] != "") {
				temp["title"] = data["results"][i]["name"];
			}
			if (data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
				temp["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
				filtered_data["results"].push(temp);
			}
			i++;
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/similarTVShows/', (req, res) => {
	var filtered_data = [];
	if (!req.query.hasOwnProperty("tv_show_id") || req.query.tv_show_id.length == 0) {
		res.status(200).send(filtered_data).end();
		return;
	}
	axios.get('https://api.themoviedb.org/3/tv/' + req.query.tv_show_id + '/similar?api_key=63bbdaaeac235ced1deca83902eb462a&language=en-US&page=1')
	.then(response => {
		var data = response.data;
		var i = 0;
		var data_set = [];
		while (i < data["results"].length) {
			var temp = {};
			if (data["results"][i].hasOwnProperty("id") && data["results"][i]["id"] != null && data["results"][i]["id"] != "") {
				temp["id"] = data["results"][i]["id"];
			}
			if (data["results"][i].hasOwnProperty("name") && data["results"][i]["name"] != null && data["results"][i]["name"] != "") {
				temp["title"] = data["results"][i]["name"];
			}
			if (data["results"][i].hasOwnProperty("poster_path") && data["results"][i]["poster_path"] != null && data["results"][i]["poster_path"] != "") {
				temp["poster_path"] = data["results"][i]["poster_path"];
				data_set.push(temp);
			}
			if (data_set.length == 6) {
				filtered_data.push(data_set);
				data_set = [];
			}
			i++;
		}
		if (data_set.length < 6 && data_set.length > 0) {
			while(data_set.length != 6) {
				data_set.push({});
			}
			filtered_data.push(data_set);
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/tvShowVideo/', (req, res) => {
	var filtered_data = {};
	if (!req.query.hasOwnProperty("tv_show_id") || req.query.tv_show_id.length == 0) {
		res.status(200).send(filtered_data).end();
		return;
	}
	axios.get('https://api.themoviedb.org/3/tv/' + req.query.tv_show_id + '/videos?api_key=63bbdaaeac235ced1deca83902eb462a&language=en-US&page=1')
	.then(response => {
		var data = response.data;
		for (var i = 0; i < data["results"].length; i++) {
			if (data["results"][i]["type"] == "Trailer") {
				if (data["results"][i].hasOwnProperty("site") && data["results"][i]["site"] != null && data["results"][i]["site"] != "") {
					filtered_data["site"] = data["results"][i]["site"];
				}
				if (data["results"][i].hasOwnProperty("type") && data["results"][i]["type"] != null && data["results"][i]["type"] != "") {
					filtered_data["type"] = data["results"][i]["type"];
				}
				if (data["results"][i].hasOwnProperty("name") && data["results"][i]["name"] != null && data["results"][i]["name"] != "") {
					filtered_data["name"] = data["results"][i]["name"];
				}
				if (data["results"][i].hasOwnProperty("key") && data["results"][i]["key"] != null && data["results"][i]["key"] != "") {
					filtered_data["key"] = data["results"][i]["key"];
				}
				res.status(200).send(filtered_data).end();
				return;
			}
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/tvShowDetails/', (req, res) => {
	var filtered_data = {};
	if (!req.query.hasOwnProperty("tv_show_id") || req.query.tv_show_id.length == 0) {
		res.status(200).send(filtered_data).end();
		return;
	}
	axios.get('https://api.themoviedb.org/3/tv/' + req.query.tv_show_id + '?api_key=63bbdaaeac235ced1deca83902eb462a&language=enUS&page=1')
	.then(response => {
		var data = response.data;
		filtered_data["type"] = "TV";
		if (data.hasOwnProperty("id") && data["id"] != null && data["id"] != "") {
			filtered_data["id"] = data["id"];
		}
		if (data.hasOwnProperty("name") && data["name"] != null && data["name"] != "") {
			filtered_data["title"] = data["name"];
		}
		if (data.hasOwnProperty("genres") && data["genres"] != null && data["genres"] != "") {
			var gen = "";
			for (var k = 0; k < data["genres"].length; k++) {
				if (k == 0) {
					gen = data["genres"][k]["name"];
				}
				else {
					gen += ", " + data["genres"][k]["name"];
				}
			}
			filtered_data["genres"] = gen;
		}
		if (data.hasOwnProperty("spoken_languages") && data["spoken_languages"] != null && data["spoken_languages"] != "") {
			var sl = "";
			for (var k = 0; k < data["spoken_languages"].length; k++) {
				if (k == 0) {
					sl = data["spoken_languages"][k]["name"];
				}
				else {
					sl += ", " + data["spoken_languages"][k]["name"];
				}
			}
			filtered_data["spoken_languages"] = sl;
		}
		if (data.hasOwnProperty("first_air_date") && data["first_air_date"] != null && data["first_air_date"] != "") {
			filtered_data["release_date"] = data["first_air_date"].split("-")[0];
		}
		if (data.hasOwnProperty("episode_run_time") && data["episode_run_time"] != null && data["episode_run_time"] != "") {
			const time = Number(data["episode_run_time"][0]);
			var rtime = "";
			if (time-time%60 != 0) {
				rtime += (time-time%60)/60;
				if ((time-time%60)/60 == 1) {
					rtime += "hr ";
				}
				else {
					rtime += "hrs ";
				}
			}
			rtime += time%60 + "mins";
			filtered_data["runtime"] = rtime;
		}
		if (data.hasOwnProperty("overview") && data["overview"] != null && data["overview"] != "") {
			filtered_data["overview"] = data["overview"];
		}
		if (data.hasOwnProperty("vote_average") && data["vote_average"] != null && data["vote_average"] != "") {
			filtered_data["vote_average"] = data["vote_average"];
		}
		if (data.hasOwnProperty("tagline") && data["tagline"] != null && data["tagline"] != "") {
			filtered_data["tagline"] = data["tagline"];
		}
		if (data.hasOwnProperty("poster_path") && data["poster_path"] != null && data["poster_path"] != "") {
			filtered_data["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["poster_path"];
		}
		if (data.hasOwnProperty("backdrop_path") && data["backdrop_path"] != null && data["backdrop_path"] != "") {
			filtered_data["backdrop_path"] = "https://image.tmdb.org/t/p/w500" + data["backdrop_path"];
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/tvShowReviews/', (req, res) => {
	var filtered_data = {};
	if (!req.query.hasOwnProperty("tv_show_id") || req.query.tv_show_id.length == 0) {
		res.status(200).send(filtered_data).end();
		return;
	}
	axios.get('https://api.themoviedb.org/3/tv/' + req.query.tv_show_id + '/reviews?api_key=63bbdaaeac235ced1deca83902eb462a&language=en-US&page=1')
	.then(response => {
		var data = response.data;
		if ( data["results"].length == 0) {
			res.status(200).send(filtered_data).end();
			return;
		}
		filtered_data["results"] = [];
		for (var i = 0; i < Math.min(data["results"].length, 3); i++) {
			var temp = {};
			if (data["results"][i].hasOwnProperty("author") && data["results"][i]["author"] != null && data["results"][i]["author"] != "") {
				temp["author"] = data["results"][i]["author"];
			}
			if (data["results"][i].hasOwnProperty("content") && data["results"][i]["content"] != null && data["results"][i]["content"] != "") {
				temp["content"] = data["results"][i]["content"];
			}
			if (data["results"][i].hasOwnProperty("created_at") && data["results"][i]["created_at"] != null && data["results"][i]["created_at"] != "") {
				temp["created_at"] = new Date(data["results"][i]["created_at"]);
			}
			if (data["results"][i].hasOwnProperty("url") && data["results"][i]["url"] != null && data["results"][i]["url"] != "") {
				temp["url"] = data["results"][i]["url"];
			}
			if (data["results"][i]["author_details"].hasOwnProperty("rating") && data["results"][i]["author_details"]["rating"] != null && data["results"][i]["author_details"]["rating"] != "") {
				temp["rating"] = data["results"][i]["author_details"]["rating"];
			}
			else {
				temp["rating"] = 0;
			}
			if (data["results"][i]["author_details"].hasOwnProperty("avatar_path") && data["results"][i]["author_details"]["avatar_path"] != null && data["results"][i]["author_details"]["avatar_path"] != "") {
				if (validUrl.isUri(data["results"][i]["author_details"]["avatar_path"].substring(1))) {
					temp["avatar_path"] = data["results"][i]["author_details"]["avatar_path"].substring(1);
				}
				else {
					temp["avatar_path"] = "https://image.tmdb.org/t/p/original" + data["results"][i]["author_details"]["avatar_path"];
				}
			}
			else {
				temp["avatar_path"] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHnPmUvFLjjmoYWAbLTEmLLIRCPpV_OgxCVA&usqp=CAU";
			}
			filtered_data["results"].push(temp);
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/tvShowCast/', (req, res) => {
	var filtered_data = {};
	if (!req.query.hasOwnProperty("tv_show_id") || req.query.tv_show_id.length == 0) {
		res.status(200).send(filtered_data).end();
		return;
	}
	axios.get('https://api.themoviedb.org/3/tv/' + req.query.tv_show_id + '/credits?api_key=63bbdaaeac235ced1deca83902eb462a&language=en-US&page=1')
	.then(response => {
		var data = response.data;
		if (!data.hasOwnProperty("cast") || data["cast"] == null || data["cast"] == "" || data["cast"].length == 0) {
			res.status(200).send(filtered_data).end();
			return;
		}
		filtered_data["results"] = [];
		var i = 0;
		while (filtered_data["results"].length < 6 && filtered_data["results"].length < data["cast"].length) {
			var temp = {};
			if (data["cast"][i].hasOwnProperty("id") && data["cast"][i]["id"] != null && data["cast"][i]["id"] != "") {
				temp["id"] = data["cast"][i]["id"];
			}
			if (data["cast"][i].hasOwnProperty("name") && data["cast"][i]["name"] != null && data["cast"][i]["name"] != "") {
				temp["name"] = data["cast"][i]["name"];
			}
			if (data["cast"][i].hasOwnProperty("character") && data["cast"][i]["character"] != null && data["cast"][i]["character"] != "") {
				temp["character"] = data["cast"][i]["character"];
			}
			if (data["cast"][i].hasOwnProperty("profile_path") && data["cast"][i]["profile_path"] != null && data["cast"][i]["profile_path"] != "") {
				temp["profile_path"] = "https://image.tmdb.org/t/p/w500" + data["cast"][i]["profile_path"];
				filtered_data["results"].push(temp);
			}
			i++;
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/castDetails/', (req, res) => {
	if (!req.query.hasOwnProperty("person_id") || req.query.person_id.length == 0) {
		res.status(200).send('No results').end();
		return;
	}
	axios.get('https://api.themoviedb.org/3/person/' + req.query.person_id + '?api_key=63bbdaaeac235ced1deca83902eb462a&language=enUS&page=1')
	.then(response => {
		var data = response.data;
		var filtered_data = {};
		if (data.hasOwnProperty("birthday") && data["birthday"] != null && data["birthday"] != "") {
			filtered_data["birthday"] = data["birthday"];
		}
		if (data.hasOwnProperty("place_of_birth") && data["place_of_birth"] != null && data["place_of_birth"] != "") {
			filtered_data["place_of_birth"] = data["place_of_birth"];
		}
		if (data.hasOwnProperty("gender") && data["gender"] != null && data["gender"] != "") {
			if (data["gender"] == "1") {
				filtered_data["gender"] = "Female";
			}
			else if (data["gender"] == "2") {
				filtered_data["gender"] = "Male";
			}
			else {
				filtered_data["gender"] = data["gender"];
			}
		}
		if (data.hasOwnProperty("name") && data["name"] != null && data["name"] != "") {
			filtered_data["name"] = data["name"];
		}
		if (data.hasOwnProperty("homepage") && data["homepage"] != null && data["homepage"] != "") {
			filtered_data["homepage"] = data["homepage"];
		}
		if (data.hasOwnProperty("also_known_as") && data["also_known_as"] != null && data["also_known_as"] != "") {
			filtered_data["also_known_as"] = data["also_known_as"];
		}
		if (data.hasOwnProperty("known_for_department") && data["known_for_department"] != null && data["known_for_department"] != "") {
			filtered_data["known_for_department"] = data["known_for_department"];
		}
		if (data.hasOwnProperty("biography") && data["biography"] != null && data["biography"] != "") {
			filtered_data["biography"] = data["biography"];
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

app.get('/apis/castExternalIDs/', (req, res) => {
	if (!req.query.hasOwnProperty("person_id") || req.query.person_id.length == 0) {
		res.status(200).send('No results').end();
		return;
	}
	axios.get('https://api.themoviedb.org/3/person/' + req.query.person_id + '/external_ids?api_key=63bbdaaeac235ced1deca83902eb462a&language=en-US&page=1')
	.then(response => {
		var data = response.data;
		var filtered_data = {};
		if (data.hasOwnProperty("imdb_id") && data["imdb_id"] != null && data["imdb_id"] != "") {
			filtered_data["imdb_id"] = data["imdb_id"];
		}
		if (data.hasOwnProperty("facebook_id") && data["facebook_id"] != null && data["facebook_id"] != "") {
			filtered_data["facebook_id"] = data["facebook_id"];
		}
		if (data.hasOwnProperty("instagram_id") && data["instagram_id"] != null && data["instagram_id"] != "") {
			filtered_data["instagram_id"] = data["instagram_id"];
		}
		if (data.hasOwnProperty("twitter_id") && data["twitter_id"] != null && data["twitter_id"] != "") {
			filtered_data["twitter_id"] = data["twitter_id"];
		}
		res.status(200).send(filtered_data).end();
	})
	.catch(error => {
		console.log(error);
	});
});

// Start the server
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`App listening on port ${PORT}`);
  console.log('Press Ctrl+C to quit.');
});

