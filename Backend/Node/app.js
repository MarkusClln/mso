var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var mongoose = require("mongoose");

connect_db();

var indexRouter = require('./routes/index');
var userRouter = require('./routes/user');
var pinRoute = require("./routes/pin");
var eventRoute = require("./routes/event");


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

app.use('/user', userRouter);
app.use('/pin', pinRoute);
app.use('/event', eventRoute);




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

create_swagger();
function create_swagger(){
  var argv = require('minimist')(process.argv.slice(2));
  var bodyParser = require( 'body-parser' );
  var subpath = express();
  app.use(bodyParser());
  app.use("/v1", subpath);
  var swagger = require('swagger-node-express').createNew(subpath);
  app.use(express.static('dist'));

  swagger.setApiInfo({
    title: "mso API",
    description: "API from mso Projekt",
    termsOfServiceUrl: "",
    contact: "jens@dudu.xxx",
    license: "",
    licenseUrl: ""
  });

  subpath.get('/', function (req, res) {
    res.sendfile(__dirname + '/dist/index.html');
  });

  swagger.configureSwaggerPaths('', 'api-docs', '');

  var domain = 'localhost';
  if(process.env.DOMAIN !== undefined)
    domain = process.env.DOMAIN;
  else
    console.log('No --domain=xxx specified, taking default hostname "localhost".');
  var applicationUrl = 'http://' + domain;
  swagger.configure(applicationUrl, '1.0.0');

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


var port = 3000;
if(process.env.PORT !== undefined)
  port = process.env.PORT;
else
  console.log('No --port=xxx specified, taking default port 3000');

app.listen(port, () => console.log("Listen to port 3000"));
