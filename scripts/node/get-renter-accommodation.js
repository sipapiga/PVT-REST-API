let functions = require('./functions');

let server = 'http://localhost:9000';

functions.localLogin(server, 'anna@example.com', 'password', function(responseObject) {
        functions.getAccommodation(server, responseObject.authToken, {});
    }
);
