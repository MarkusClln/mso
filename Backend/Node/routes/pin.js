const express = require('express');
const router = express.Router();
const mongoose = require("mongoose");

const bodyParser = require('body-parser');
router.use(bodyParser.json());

const pinSchema = require("../models/pinSchema");
const eventSchema = require("../models/eventSchema");
const ckeck_auth = require("../middleware/check-auth");


/* GET home page. */



router.post('/', ckeck_auth, function (req, res, next) {

    const body = req.body;
    console.log("USER: " + JSON.stringify(req.user));
    console.log("PIN: " + JSON.stringify(body));
    const pin = new pinSchema({
        _id: new mongoose.Types.ObjectId(),
        location: body.location,
        name: body.name,
        description: body.description,
        user_id: req.user.sub

    });

    pin.save(function (err, result) {
        if (err) res.send(err);
        res.json(result);
    });
});

router.get('/all', function (req, res, next) {

    var lat = req.query.lat;
    var lng = req.query.lng;
    var distance = req.query.distance;

    console.log("lat " + lat + " lng " + lng);


    if (lat != undefined && lng != undefined && distance != undefined) {

        pinSchema.aggregate(
            [
                {
                    $geoNear: {
                        near: {
                            type: "Point",
                            coordinates: [parseFloat(lat), parseFloat(lng)]
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
                {
                    $lookup: {
                        from: eventSchema.collection.name,
                        localField: "_id",
                        foreignField: "pin_id",
                        as: "events"
                    }
                }
        ]).exec((error, results) => {
            if (error) console.log(error);
            res.json(results);
        });

    }

});



router.get('/getByUserId/:id', function (req, res, next) {
    var id = req.params.id;
    var query = pinSchema.find({
        'user_id': id
    });
    query.exec(function (err, result) {
        if (err) return console.error(err);
        res.json(result);
    });
});

router.get('/getByName/:name', function (req, res, next) {

    //var pin = mongoose.model('Pins', pinSchema);
    // find each person with a name contains 'Ghost'
    pinSchema.find({
            "name": {
                $regex: req.params.name,
                $options: 'i'
            }
        },
        function (err, pins) {
            if (err) return console.error(err);
            res.json(pins);

        });
});

router.get('/:id', function (req, res, next) {
    var id = req.params.id;
    pinSchema.findById(id, function (err, result) {
        if (err) return console.error(err);
        res.json(result)
    });
});

module.exports = router;
