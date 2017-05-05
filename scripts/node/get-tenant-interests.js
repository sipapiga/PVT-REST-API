let functions = require('./functions');

let server = 'http://localhost:9000';

functions.localLogin(server, 'kalle@example.com', 'password', function(responseObject) {

		functions.getInterests(server, responseObject.authToken, {

			count: 1,
			offset: 1,
			tenantId: 5,
			accommodationId: 2

		});
	}
);
