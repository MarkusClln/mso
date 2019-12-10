var express = require('express');
var jwt = require("jsonwebtoken");
var router = express.Router();
var mongoose = require("mongoose");
var bcrypt = require("bcryptjs");

const User = require("../models/userSchema");
const eventSchema = require("../models/eventSchema");

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

router.post('/fav/:id',check_auth, (req, res) => {

        console.log(req.params.id);
        User.find({auth0_id: req.user.sub}).exec().then(user => {
            if(user.length >=1){
                var userData;

                if(user[0].likedEvents.includes(req.params.id)){
                    user[0].likedEvents.pull(req.params.id);
                }
                user[0].likedEvents.push(req.params.id);
                //user.likedEvents.push(req.body.event_id);


                eventSchema.findById(req.params.id,function (err, event) {
                    console.log(user[0]._id.toString());

                        if(event.likedUsers.includes(user[0].auth0_id)){
                            event.likedUsers.pull(user[0].auth0_id);
                        }else{
                            user[0].rating +=1;
                        }

                        event.likedUsers.push(user[0].auth0_id);
                        //user.likedEvents.push(req.body.event_id);

                    if(event.dislikedUsers.includes(user[0].auth0_id)){
                        event.dislikedUsers.pull(user[0].auth0_id);
                    }

                    user[0].save(function(err, result) {
                        userData = result;
                    });

                    event.save(function (err, result) {
                        res.json(userData);
                    });

                });
            }
        });
});
router.delete('/fav/:id',check_auth, (req, res) => {

    console.log(req.params.id);
    User.find({auth0_id: req.user.sub}).exec().then(user => {
        if(user.length >=1){
            var userData;
            user[0].likedEvents.pull(req.params.id);
            //user.likedEvents.push(req.body.event_id);


            eventSchema.findById(req.params.id,function (err, event) {
                console.log(user[0]._id.toString());

                if(event.dislikedUsers.includes(user[0].auth0_id)){
                    event.dislikedUsers.pull(user[0].auth0_id);
                }else{
                    user[0].rating -=1;
                }

                event.dislikedUsers.push(user[0].auth0_id);
                //user.likedEvents.push(req.body.event_id);

                if(event.likedUsers.includes(user[0].auth0_id)){
                    event.likedUsers.pull(user[0].auth0_id);
                }

                user[0].save(function(err, result) {
                    userData = result;
                });

                event.save(function (err, result) {
                    res.json(userData);
                });

            });

        }
    });
});

router.post('/fav_ids',check_auth, (req, res) => {

    console.log(req.user.sub);
    User.find({auth0_id: req.user.sub}).exec().then(user => {
        if(user.length >=1){
            const event_ids = user[0].likedEvents;
            res.json(event_ids);

        }
    });


});

router.post('/auth0',check_auth, (req, res) => {
    res.json({ auth0: req.user.sub });
});


module.exports = router;