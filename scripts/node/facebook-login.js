let functions = require('./functions');

let server = 'http://localhost:9000';
let facebookToken = 'EAAa2D7XPp6oBAO5WkCSceiyyOBIiIyJGY8xDAka7LWNZBrPEeosgRFV2c9mlcUDIZASehMkR2EuijdZCq1u2412msLyBGnqPmCLOjSecY8baVvgcbZAF6ZBqlCZC7YQuz31n5JeeWrtRvsKFJPWqt9v09W5OZCzcQ1BCleAuKzPaDSYZCT6lGAvRcHZB36PZCE8DgZD';

functions.facebookLogin(server, facebookToken, function(responseObject) {
        console.log(responseObject);
    }
);
