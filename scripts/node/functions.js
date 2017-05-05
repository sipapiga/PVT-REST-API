let fetch = require('node-fetch');
let FormData = require('form-data');

let buildUri = function(endpoint, requestObject) {

    let uri = endpoint + '?';
    let first = true;

    for (let key in requestObject) {

        if (first) {
            first = false;
        } else {
            uri += '&';
        }

        if (requestObject.hasOwnProperty(key)) {
            uri += (key + '=' + requestObject[key]);
        }
    }

    return uri;

};

let localLogin = function(server, email, password, successCallback) {

	let formData = new FormData();
	formData.append('emailAddress', email);
	formData.append('password', password);

	fetch(server+ '/login', {

		method: 'POST',
		credentials: 'include',
		body: formData

	}).then(function(response) {

		return response.json();

	}).then(function(responseObject) {

		successCallback(responseObject);

	}).catch(function(error) {

		console.log(error);

	});
};

let getInterests = function(server, authToken, parameters) {
	performAuthenticatedGetRequest(server, 'interests', authToken, parameters, console.log);
};

let getAccommodation = function(server, authToken, parameters) {
    performAuthenticatedGetRequest(server, 'accommodation', authToken, parameters, console.log);
};

let getTenantProfile = function(server, authToken) {
    performAuthenticatedGetRequest(server, 'users/tenants', authToken, {}, console.log);
};

let performAuthenticatedGetRequest = function(server, endpoint, authToken, parameters, successCallback) {

    let uri = buildUri(server + '/' + endpoint, parameters);

    fetch(uri, {

        method: 'GET',
        headers: {"X-AUTH-TOKEN":authToken}

    }).then(function(response) {

        return response.json();

    }).then(function(responseObject) {

        successCallback(responseObject);

    }).catch(function(error) {

        console.log(error);

    });

};

module.exports = {
	localLogin: localLogin,
	getInterests: getInterests,
    getAccommodation: getAccommodation,
    getTenantProfile: getTenantProfile
};

