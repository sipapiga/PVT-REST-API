let requestBody = {
    emailAddress: 'user1@demo.com',
    password: 'password'
};

fetch('/login', {
    method: 'POST',
    body: requestBody
});
