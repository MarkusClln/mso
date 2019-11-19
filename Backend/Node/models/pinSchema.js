const mongoose = require("mongoose");

const pinsSchema = new mongoose.Schema({
    location: {
        type: { type: String },
        coordinates: []
    },
    name: String,
    description: String,
    user_id: String
});

pinsSchema.index({location:"2dsphere"});
module.exports = mongoose.model("Pins", pinsSchema);