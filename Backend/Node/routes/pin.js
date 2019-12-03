const express = require('express');
const router = express.Router();
const mongoose = require("mongoose");

const bodyParser = require('body-parser');
router.use(bodyParser.json());

const pinSchema = require("../models/pinSchema");
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

router.get('/getAll',  function(req, res, next) {
    var lat = req.query.Lat
    var lng = req.query.Lng
    var distance = req.query.Distance
    if(lat != undefined && lng != undefined && distance != undefined){

        pinSchema.find({
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

module.exports = router;