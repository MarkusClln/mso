const mongoose = require("mongoose");



const eventSchema = new mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    pin_id: String,
    user_id: String,
    name: String,
    shortDescription: String,
    description: String,
    category: String,
    date: Date

});

eventSchema.index({pin_id:1});
module.exports = mongoose.model("Events", eventSchema);