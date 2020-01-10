var express = require('express');
var router = express.Router();
var mongoose = require("mongoose");

const bodyParser = require('body-parser');
router.use(bodyParser.json());

const eventSchema = require("../models/eventSchema");
const pinSchema = require("../models/pinSchema");
const userSchema = require("../models/userSchema");


const check_auth = require("../middleware/check-auth");


/* GET home page. */

router.post('/', check_auth, function (req, res, next) {

    const body = req.body;

    date = new Date(body.event.date);




    const event = new eventSchema({
        _id: new mongoose.Types.ObjectId(),
        pin_id: body.event.pin_id,
        user_id: req.user.sub,
        name: body.event.name,
        description: body.event.description,
        shortDescription: body.event.shortDescription,
        date: date,
        category: body.event.category
    });

    event.save(function (err, result) {
        if (err) res.send(err);
        userSchema.find({
            auth0_id: req.user.sub
        }).exec().then(user => {
            if (user.length >= 1) {
                user[0].ownEvents.push(result._id);
                user[0].save(function (err, user) {
                    res.json(result);
                });

            }
        });

    });

});


router.get('/all', function (req, res, next) {

    var lat = req.query.lat;
    var lng = req.query.lng;
    var distance = req.query.distance;

    console.log(lat);
    console.log(lng);
    console.log(distance);


    if (lat != undefined && lng != undefined && distance != undefined) {

        pinSchema.aggregate( //same fkt as in get all pins
            [{
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
                        }
                    }
                }
            ]).exec((error, idObjects) => {
            if (error) console.log(error);

            var onlyIds = idObjects.map(function (el) { //idObjects are objects but we only want the ids as array
                return el._id
            });



            eventSchema.aggregate( //find the events that have the id
                [{
                        $match: {
                            pin_id: {
                                $in: onlyIds
                            }
                        }
                    }, {
                        $addFields: {
                            convertedPinId: { //for lookup to work
                                $toObjectId: "$pin_id"
                            }
                        }
                    },
                    {
                        $lookup: {
                            from: pinSchema.collection.name,
                            localField: "convertedPinId",
                            foreignField: "_id",
                            as: "pin"
                        }
                    }, {
                        $unwind: { //pin array to only pin
                            path: "$pin",
                            preserveNullAndEmptyArrays: true
                        }
                    }
                ]).exec((error, results) => {
                if (error) console.log(error);
                res.json(results);
            });

        });

    }
});

router.get('/fav', check_auth, (req, res) => {

    console.log(req.user.sub);
    userSchema.find({
        auth0_id: req.user.sub
    }).exec().then(user => {
        if (user.length >= 1) {
            const event_ids = user[0].likedEvents;
            let arr = event_ids.map(ele => new mongoose.Types.ObjectId(ele));
            console.log(arr);

            eventSchema.find({
                '_id': {
                    $in: arr
                }
            }, function (err, result) {
                res.json(result)
            });
        }
    });
});

router.get('/own', check_auth, (req, res) => {

    console.log(req.user.sub);
    userSchema.find({
        auth0_id: req.user.sub
    }).exec().then(user => {
        if (user.length >= 1) {
            const event_ids = user[0].ownEvents;
            let arr = event_ids.map(ele => new mongoose.Types.ObjectId(ele));
            console.log(arr);

            eventSchema.find({
                '_id': {
                    $in: arr
                }
            }, function (err, result) {
                res.json(result)
            });
        }
    });


});


router.get('/:id', function (req, res, next) {
    var id = req.params.id;

    eventSchema.aggregate( //find the events that have the id
        [{
                $match: {
                    _id: mongoose.Types.ObjectId(id)
                }
            }, {
                $addFields: {
                    convertedPinId: { //for lookup to work
                        $toObjectId: "$pin_id"
                    }
                }
            },
            {
                $lookup: {
                    from: pinSchema.collection.name,
                    localField: "convertedPinId",
                    foreignField: "_id",
                    as: "pin"
                }
            }, {
                $unwind: { //pin array to only pin
                    path: "$pin",
                    preserveNullAndEmptyArrays: true
                }
            }
        ]).exec((error, results) => {
        if (error) console.log(error);

        if (results.length > 0) {
            res.json(results[0]);
        } else {
            res.json({});
        }
    });

});



function distance(lat1, lon1, lat2, lon2, unit) {
    if ((lat1 == lat2) && (lon1 == lon2)) {
        return 0;
    } else {
        var radlat1 = Math.PI * lat1 / 180;
        var radlat2 = Math.PI * lat2 / 180;
        var theta = lon1 - lon2;
        var radtheta = Math.PI * theta / 180;
        var dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
        if (dist > 1) {
            dist = 1;
        }
        dist = Math.acos(dist);
        dist = dist * 180 / Math.PI;
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344
        }
        if (unit == "N") {
            dist = dist * 0.8684
        }
        return dist;
    }
}

module.exports = router;
