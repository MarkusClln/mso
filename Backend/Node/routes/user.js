var express = require('express');
var jwt = require("jsonwebtoken");
var router = express.Router();
var mongoose = require("mongoose");
var bcrypt = require("bcryptjs");

const User = require("../models/userSchema");

const check_auth = require("../middleware/check-auth");

router.post('/',check_auth, (req, res) => {
    User.find({auth0_id: req.body.auth0_id}).exec().then(user => {
        if(user.length >=1){
            return res.status(409).json({
                    message: "User exists"
            })
        }else{
            const user = new User({
                _id: new mongoose.Types.ObjectId(),
                email: req.body.email,
                auth0_id: req.body.auth0_id,
                rating: 0,
                name: req.body.name
            });
            user
                .save()
                .then(result => {
                    console.log(result);
                    res.status(201).json({
                        message: "User created"
                    });
                })
                .catch(err => {
                    res.status(500).json({
                        error: err
                    })
                });
        }
    });
});

router.get('/:id',check_auth, (req, res) => {

    console.log(req.params.id);
    User.find({auth0_id: req.params.id}).exec().then(user =>{
        if (user.length < 1){
            return res.status(401).json({
                message: "Auth failed"
            })
        }else{
            res.end(JSON.stringify(user));
        }
    })


});

module.exports = router;