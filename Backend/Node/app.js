var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var mongoose = require("mongoose");

connect_db();

var indexRouter = require('./routes/index');
var authRouter = require('./routes/user');
var testRoute = require("./routes/pin");
var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

app.use('/', indexRouter);
app.use('/user', authRouter);
app.use('/pin', testRoute);


// Connect MongoDB

function connect_db(){

  if(process.env.DB_IP == undefined){
    db_ip = "172.21.0.1"
  }else{
    db_ip = process.env.DB_IP
  }
  if(process.env.DB_PORT == undefined){
    db_port = "27017"
  }else{
    db_port = process.env.DB_PORT
  }
  if(process.env.DB_NAME == undefined){
    db_name = "mso"
  }else{
    db_name = process.env.DB_NAME
  }

  mongoose.connect("mongodb://"+db_ip+":"+db_port+"/"+db_name,
      {useNewUrlParser: true, useUnifiedTopology: true});
  var db = mongoose.connection;
  db.on('error', console.error.bind(console, 'connection error:'));
  db.once('open', function() {
    console.log("Connected to database");
  });
}



// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

app.listen(process.env.PORT, () => console.log("Listen to port 3000"));
