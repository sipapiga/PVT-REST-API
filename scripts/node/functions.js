let fetch = require('node-fetch');
let FormData = require('form-data');

/*
 * Login
 */

let facebookLogin = function(server, facebookToken, successCallback) {

    let options = {
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            facebookAuthToken: facebookToken
        })
    };

    performPostRequest(server, 'facebook/login', options, successCallback);

};

let localLogin = function(server, email, password, successCallback) {

	let formData = new FormData();
	formData.append('emailAddress', email);
	formData.append('password', password);

	performPostRequest(server, 'login', {body: formData}, successCallback)

};

/*
 * Interests
 */

let getInterests = function(server, authToken, parameters) {
	performAuthenticatedGetRequest(server, 'interests', authToken, parameters, console.log);
};

/*
 * Accommodation
 */

let getAccommodation = function(server, authToken, parameters) {
    performAuthenticatedGetRequest(server, 'accommodation', authToken, parameters, console.log);
};

/*
 * Users
 */

let getTenantProfile = function(server, authToken) {
    performAuthenticatedGetRequest(server, 'users/tenants', authToken, {}, console.log);
};

/*
 * Utilities
 */

let performPostRequest = function(server, endpoint, options, successCallback) {

    options.method = 'POST';
    options.credentials = 'include';

    fetch(server + '/' + endpoint, options) .then(function(response) {

        return response.json();

    }).then(function(responseObject) {

        successCallback(responseObject);

    }).catch(function(error) {

        console.log(error);

    });

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

module.exports = {
    facebookLogin: facebookLogin,
    localLogin: localLogin,
	getInterests: getInterests,
    getAccommodation: getAccommodation,
    getTenantProfile: getTenantProfile
};

