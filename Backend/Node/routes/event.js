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

router.post('/', check_auth, function(req, res, next) {

    const body = req.body;

    date = new Date(body.event.date);




    const event = new eventSchema({
        _id: new mongoose.Types.ObjectId(),
        pin_id: body.event.pin_id,
        user_id: req.user.sub,
        name: body.event.name,
        description: body.event.description,
        shortDescription: body.event.shortDescription,
        date: date
    });

    event.save(function (err, result) {
        if (err) res.send(err);
        res.json(result);
    });
});

router.post('/all',  function(req, res, next) {

    var lat = req.body.lat;
    var lng = req.body.lng;
    var distance = req.body.distance;

    console.log(lat);
    console.log(lng);
    console.log(distance);


    if(lat != undefined && lng != undefined && distance != undefined){


        var query_pins_ids = pinSchema.find({
            location: {
                $nearSphere: {
                    $maxDistance: distance,
                    $minDistance: 0,
                    $geometry: {
                        type: "Point",
                        coordinates: [lng, lat]
                    }
                }
            }
        }).distinct("_id").lean();


        query_pins_ids.exec(function (err, result) {
            if (err) return console.error(err);

            var query_events = eventSchema.find({ "pin_id": result});

            query_events.exec(function (err, events) {
                    if (err) return console.error(err);
                    console.log(events);
                    res.json(events)
            });
        });


    }

});

router.post('/fav',check_auth, (req, res) => {

    console.log(req.user.sub);
    userSchema.find({auth0_id: req.user.sub}).exec().then(user => {
        if(user.length >=1){
            const event_ids = user[0].likedEvents;
            let arr = event_ids.map(ele => new mongoose.Types.ObjectId(ele));
            console.log(arr);

            eventSchema.find({
                '_id': { $in: arr}
            }, function(err, result){
                res.json(result)
            });
        }
    });


});



function distance(lat1, lon1, lat2, lon2, unit) {
    if ((lat1 == lat2) && (lon1 == lon2)) {
        return 0;
    }
    else {
        var radlat1 = Math.PI * lat1/180;
        var radlat2 = Math.PI * lat2/180;
        var theta = lon1-lon2;
        var radtheta = Math.PI * theta/180;
        var dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
        if (dist > 1) {
            dist = 1;
        }
        dist = Math.acos(dist);
        dist = dist * 180/Math.PI;
        dist = dist * 60 * 1.1515;
        if (unit=="K") { dist = dist * 1.609344 }
        if (unit=="N") { dist = dist * 0.8684 }
        return dist;
    }
}

module.exports = router;