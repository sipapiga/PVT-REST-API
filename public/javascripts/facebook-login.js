$(document).ready(function() {

    $.ajaxSetup({ cache: true });
    $.getScript('//connect.facebook.net/en_US/sdk.js', function(){
        FB.init({
            appId: '1889028451313578',
            version: 'v2.7' // or v2.1, v2.2, v2.3, ...
        });
        $('#loginbutton,#feedbutton').removeAttr('disabled');

        $('#login').click(function() {

            FB.getLoginStatus(function(response) {

                console.log(response);

                if (response.status === 'connected') {

                    console.log(response.authResponse.accessToken);
                    facebookLogin(response.authResponse.accessToken);

                    FB.api('/me', function(response) {
                        console.log(JSON.stringify(response));
                    });

                }
                else {
                    FB.login(function(response) {

                        console.log(response);
                        facebookLogin(response.authResponse.accessToken);

                        FB.api('/me', function(response) {
                            console.log(JSON.stringify(response));
                        });

                    }, {scope: 'email,public_profile'});
                }
            });
        });
    });

    let SERVER = 'http://localhost:9000';

    let facebookLogin = function(facebookToken) {

        let body = {
            'facebookAuthToken': facebookToken
        };

        fetch(SERVER + '/facebook/login', {

            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body),
            credentials: 'include'

        }).then(function(response) {

            console.log(response);
            return response.json();

        }).then(function(responseObject) {

            console.log(responseObject);

        }).catch(function(error) {

            console.log(error);

        });
    };
});