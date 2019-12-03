const mongoose = require('mongoose');


const userSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    auth0_id: { type: String, required: true },
    rating: {type: Number, required: true},
    name: {type: String, required: true},
    email: {type: String, required: true},
    likedEvents:[String],
    ownEvents:[String]

});

module.exports = mongoose.model('User', userSchema);