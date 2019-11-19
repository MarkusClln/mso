var express = require('express');
var jwt = require("jsonwebtoken");
var router = express.Router();
var mongoose = require("mongoose");
var bcrypt = require("bcryptjs");

const User = require("../models/userSchema");


router.post('/signin', (req, res) => {
    User.find({email: req.body.email})
        .exec()
        .then(user =>{
            if (user.length < 1){
                return res.status(401).json({
                    message: "Auth failed"
                })
            }
            bcrypt.compare(req.body.password, user[0].password, (err, result) =>{
                if(err){
                    return res.status(401).json({
                        message: "Auth failed"
                    })
                }
                if(result){

                    var token = jwt.sign({
                        email: user[0].email,
                        userId: user[0]._id }, process.env.JWT_Secret);
                    return res.status(200).json({
                        message : "Auth successful",
                        token : token
                    });

                }else{
                    return res.status(401).json({
                        message: "Auth failed"
                    })
                }
            })
        })
        .catch(err => {
            return res.status(200).json({
                message : "Auth successful"
            })
    });
});

router.post('/signup', (req, res, next) => {

    User.find({email: req.body.email}).exec().then(user => {
        if(user.length >=1){
            return res.status(409).json({
                message: "Mail exists"
            })
        }else{
            bcrypt.hash(req.body.password, 10, (err, hash) => {
                if (err) {
                    return res.statusCode(500)
                } else {
                    const user = new User({
                        _id: new mongoose.Types.ObjectId(),
                        email: req.body.email,
                        password: hash
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
        }
    });


});

module.exports = router;