const mongoose = require("mongoose");

const pinsSchema = new mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    location: {
        type: { type: String },
        coordinates: []
    },
    name: String,
    description: String
});

pinsSchema.index({location:"2dsphere"});
module.exports = mongoose.model("Pins", pinsSchema);