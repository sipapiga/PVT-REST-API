let SERVER = 'https://protected-gorge-44302.herokuapp.com';
//let SERVER = 'http:/localhost:9000';

// From http://stackoverflow.com/questions/5639346/what-is-the-shortest-function-for-reading-a-cookie-by-name-in-javascript
let cookies;

function readCookie(name, c, C, i) {

    if (cookies) {
        return cookies[name];
    }

    c = document.cookie.split('; ');
    cookies = {};

    for (i = c.length - 1; i >= 0; i--) {

        C = c[-i].split('=');
        cookies[C[0]] = C[1];
    }
    return cookies[name];
}

let buildJsonArrayString = function(array) {

    let arrayString = '[';
    let first = true;

    array.forEach(value => {

        if (first) {
            first = false;
        } else {
            arrayString += ',';
        }

        arrayString += '"' + value + '"';

    });

    return arrayString + ']';

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

let facebookLogin = function(facebookToken) {

    fetch(SERVER + '/facebook/login', {

        method: 'POST',
        headers: {
            'FACEBOOK-AUTH-TOKEN': facebookToken
        },
        credentials: 'include'

    }).then(function(response) {

        console.log(response);
        return response.json();

    }).then(function(responseObject) {

        console.log(responseObject);
        //getSwipingSessions()

    }).catch(function(error) {

        console.log(error);

    });
};

facebookLogin('EAACEdEose0cBAJv87a8liUo6Wr1w25ZBhfSGZB3ZA746JfjgVZAmD77Bbe6x4qCJ4jKKwLqv3QdIBfkI3BZBqn9FRPpoBeQkj2e5wlQ5xINWqwOCtd1XteDNLCsBgLxFLZCnFO5q3QzGUNpYfvLQ2I77kmdv0kzZAFLRI6garjiA1IQTppry5Jl');

/*
 * 1. Login.
 */
let localLogin = function(email, password) {

    let formData = new FormData();
    formData.append('emailAddress', email);
    formData.append('password', password);

    fetch(SERVER + '/login', {

        method: 'POST',
        credentials: 'include',
        body: formData

    }).then(function(response) {

        return response;

    }).then(function(responseObject) {

        getSwipingSessions(email, readCookie('authToken'));

    }).catch(function(error) {

        console.log(error);

    });
};

/*
 * 2. Get swiping sessions associated with the user logged in.
 */
let getSwipingSessions = function(email, token) {

    fetch(SERVER + '/swipingsessions/' + email, {

        method: 'GET',
        headers: {'X-AUTH-TOKEN': token}

    }).then(response => {

        console.log(response);
        return response.json();

    }).then(responseObject => {

        console.log("In getSwipingSessions():");

        responseObject.forEach(swipingSession => {

            console.log("swipingSessionId: " + swipingSession.id);

            swipingSession.generatedActivities.forEach(generatedActivity => {
                console.log(generatedActivity);
            });

            swipingSession.participatingUsers.forEach(participatingUser => {
                console.log(participatingUser);
            });

        });

        createSwipingSession(['user1@demo.com', 'user2@demo.com'], token);

    }).catch(error => {

        console.log(error);

    });
};

/*
 * 3. Create new swiping session.
 */
let createSwipingSession = function(users, token) {

    let uri = buildUri(SERVER + '/swipingsessions', {
        emailAddresses: buildJsonArrayString(users)
    });

    fetch(uri, {

        method: 'POST',
        headers: {'X-AUTH-TOKEN': token}

    }).then(response => {

        return response.json();

    }).then(responseObject => {

        chooseActivities(responseObject.swipingSessionId, users[0], responseObject.activities, token);

    }).catch(error => {

        console.log(error);

    });
};

/*
 * 4. Choose among the activities returned after creating swiping session.
 */
let chooseActivities = function(swipingSessionId, user, activities, token) {

    let uri = buildUri(SERVER + '/swipingsessions', {
        swipingSessionId: swipingSessionId,
        email: user,
        activities: buildJsonArrayString(activities)
    });

    fetch(uri, {

        method: 'PUT',
        headers: {'X-AUTH-TOKEN': token}

    }).then(response => {

        return response;

    }).then(responseObject => {

        console.log("In chooseActivities():");
        console.log(responseObject);

    }).catch(error => {

        console.log(error);

    });
};

//localLogin('user1@demo.com', 'password'); // execute
