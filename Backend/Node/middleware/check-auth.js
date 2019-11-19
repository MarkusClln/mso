var jwt = require("jsonwebtoken");


module.exports = (req, res, next) => {

    const bearerHeader = req.headers['authorization'];
    if(typeof bearerHeader !== 'undefined'){
        const bearer = bearerHeader.split(" ");
        const bearerToken = bearer[1];
        jwt.verify(bearerToken, process.env.JWT_Secret, (err, authData) => {
            if (err) {
                res.sendStatus(403);
            } else {
               req.authData = authData;
               next();
            }
        });
    }else{
        //Forbidden
        res.sendStatus(403);
    }
};