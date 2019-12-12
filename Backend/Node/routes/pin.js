const express = require('express');
const router = express.Router();
const mongoose = require("mongoose");

const bodyParser = require('body-parser');
router.use(bodyParser.json());

const pinSchema = require("../models/pinSchema");
const eventSchema = require("../models/eventSchema");
const ckeck_auth = require("../middleware/check-auth");


/* GET home page. */



router.post('/', ckeck_auth, function(req, res, next) {

    const body = req.body;
    console.log(req.user);
    const pin = new pinSchema({
        _id: new mongoose.Types.ObjectId(),
        location: body.pin.location,
        name: body.pin.name,
        description: body.pin.description,
        user_id: req.user.sub

    });

    pin.save(function (err, result) {
        if (err) res.send(err);
        res.json(result);
    });
});

router.post('/all',  function(req, res, next) {

    var lat = req.body.lat;
    var lng = req.body.lng;
    var distance = req.body.distance;

    console.log("lat "+lat+" lng "+lng);


    if(lat != undefined && lng != undefined && distance != undefined){

        mongoose.model("Pins").aggregate(
            [
            {$geoNear: {
                near: {
                    type: "Point",
                    coordinates: [ parseFloat(lat) , parseFloat(lng) ]
                },
                distanceField: "distance",
                maxDistance: parseFloat(distance),
                spherical: true
            }
            },
                {
                    "$project": {
                        "_id": {
                            "$toString": "$_id"
                        },
                        "location": true,
                        "name": true,
                        "description": true,
                        "distance": true
                    }
                },
            {$lookup:{
                    from: eventSchema.collection.name,
                    localField: "_id",
                    foreignField:"pin_id",
                    as: "events"
                }}
        ]).exec((error, results) => {
            if (error) console.log(error);
            res.json(results);
        });

    }

});

router.get('/:id', function(req, res, next) {
    var id = req.params.id;
    pinSchema.findById(id,function (err, result) {
        if (err) return console.error(err);
        res.json(result)
    });
});

router.get('/getByUserId/:id', function(req, res, next) {
    var id = req.params.id;
    var query = pinSchema.find({ 'user_id': id });
    query.exec(function (err, result) {
        if (err) return console.error(err);
        res.json(result);
    });
});

router.get('/getByName/:name', function(req, res, next) {

    //var pin = mongoose.model('Pins', pinSchema);
    // find each person with a name contains 'Ghost'
    pinSchema.find({ "name" : { $regex: req.params.name, $options: 'i' } },
        function (err, pins) {
            if (err) return console.error(err);
            res.json(pins);

        });
});

module.exports = router;