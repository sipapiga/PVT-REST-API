let functions = require('./functions');

let server = 'http://localhost:9000';

functions.localLogin(server, 'kalle@example.com', 'password', function(responseObject) {
        functions.getTenantProfile(server, responseObject.authToken);
    }
);

