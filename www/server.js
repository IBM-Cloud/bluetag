/**
 * Module dependencies.
 */
var express = require('express');
var http = require('http');

var cfenv = require("cfenv");
var appEnv = cfenv.getAppEnv();

var app = express();

//var bodyParser = require('body-parser');
//var multer = require('multer'); // v1.0.5
//var upload = multer(); // for parsing multipart/form-data

//app.use(bodyParser.json()); // for parsing application/json
//app.use(bodyParser.urlencoded({ extended: true })); // for parsing application/x-www-form-urlencoded

// all environments
//app.set('port', process.env.PORT || 80);

//app.use(express.json());
//app.use(express.urlencoded());
//app.use(express.methodOverride());

//CORS middleware
var allowCrossDomain = function(req, res, next) {
    res.header('Access-Control-Allow-Origin', 'mybluemix.net');
    res.header('Access-Control-Allow-Methods', 'GET,PUT,POST');
    res.header('Access-Control-Allow-Headers', 'Content-Type');

    next();
}

    app.use(express.bodyParser());
    app.use(express.cookieParser());
    app.use(express.session({ secret: 'cool beans' }));
    app.use(express.methodOverride());
    app.use(allowCrossDomain);

// development only
//if ('development' == app.get('env')) {
//  app.use(express.errorHandler());
//}

app.use(function(request, response, next){
        //this intercepts all requests
        //usefull for logging and stuff
	console.log( "req/resp: " + request);
	console.log( "req.baseUrl: "+request.baseUrl);
//	response.send("hello World");
	//console.log( "req/resp: " + request.body);
        next();
});

app.use(express.static(__dirname+"/app"));
//app.use(express.static(__dirname+"/public"));


http.createServer(app).listen(appEnv.port, appEnv.bind);
console.log('App started on ' + appEnv.bind + ':' + appEnv.port);
//http.createServer(app).listen(app.get('port'), function(){
//  console.log('Kibana server listening on port ' + app.get('port'));
//});
