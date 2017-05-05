let functions = require('./functions');

let server = 'http://localhost:9000';
let facebookToken = '';

functions.facebookLogin(server, facebookToken, function(responseObject) {
        console.log(responseObject);
    }
);
