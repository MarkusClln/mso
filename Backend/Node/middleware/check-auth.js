
var jwt = require("express-jwt");
var jwks = require('jwks-rsa');


module.exports = jwt({
    secret: jwks.expressJwtSecret({
        cache: true,
        rateLimit: true,
        jwksRequestsPerMinute: 100,
        jwksUri: 'https://dev-markus.eu.auth0.com/.well-known/jwks.json'
    }),
    audience: 'https://mso-api',
    algorithms: ['RS256']
});


