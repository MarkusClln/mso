var express = require('express');
var router = express.Router();
var mongoose = require("mongoose");

const bodyParser = require('body-parser');
router.use(bodyParser.json());

const eventSchema = require("../models/eventSchema");
const pinSchema = require("../models/pinSchema");
const ckeck_auth = require("../middleware/check-auth");


/* GET home page. */

router.post('/', ckeck_auth, function(req, res, next) {

    const body = req.body;
    date = new Date(body.event.date);
    console.log(date);
    const event = new eventSchema({
        _id: new mongoose.Types.ObjectId(),
        pin_id: body.event.pin_id,
        name: body.event.name,
        description: body.event.description,
        date: date,
        user_id: req.authData.userId
    });

    event.save(function (err, result) {
        if (err) res.send(err);
        res.json(result);
    });
});

router.get('/getAll',  function(req, res, next) {
    var lat = req.query.Lat;
    var lng = req.query.Lng;
    var distance = req.query.Distance
    if(lat != undefined && lng != undefined && distance != undefined){

        var query_pins = pinSchema.find({
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
        var pins;
        query_pins.exec(function (err, result) {
            if (err) return console.error(err);

            var query_events = eventSchema.find({ "pin_id": result });

            query_events.exec(function (err, result) {
                if (err) return console.error(err);
                res.json(result)
            });
        });







    }
});

module.exports = router;