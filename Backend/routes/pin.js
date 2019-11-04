var express = require('express');
var router = express.Router();
var mongoose = require("mongoose");

const bodyParser = require('body-parser');
router.use(bodyParser.json());

const pinsSchema = require("../models/pin");

/* GET home page. */

router.post('/set', function(req, res, next) {

    const body = req.body;

    const pin = new pinsSchema({

        location: body.pin.location,
        name: body.pin.name,
        description: body.pin.description

    });

    pin.save(function (err, result) {
        if (err) res.send(err);
        res.json(result);
    });

});

router.get('/getAll',  function(req, res, next) {
    var lat = req.query.Lat
    var lng = req.query.Lng
    var distance = req.query.Distance
    if(lat != undefined && lng != undefined && distance != undefined){

        pinsSchema.find({
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
        }).find((error, results) => {
            if (error) console.log(error);
            res.json(results);
        });

    }
});

router.get('/get/:id', function(req, res, next) {
    var id = req.params.id;
    pinsSchema.findById(id,function (err, result) {
        if (err) return console.error(err);
        res.json(result)
    });
});

module.exports = router;