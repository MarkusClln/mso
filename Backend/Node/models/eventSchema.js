const mongoose = require("mongoose");

const eventSchema = new mongoose.Schema({

    pin_id: String,
    user_id: String,
    name: String,
    description: String,
    date: Date

});


module.exports = mongoose.model("Events", eventSchema);