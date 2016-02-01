(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
module.exports={
	"register": "http://bluetagregister1.mybluemix.net/" ,
	"engine": "http://bluetaggame1.mybluemix.net/",
	"location": "ws://bluetaglocation1.mybluemix.net/",
	"search": "ws://bluetagsearch1.mybluemix.net/",
	"tag": "http://bluetagtag1.mybluemix.net/"

}

},{}],2:[function(require,module,exports){
/*
 COPYRIGHT LICENSE: This information contains sample code provided in source
 code form. You may copy, modify, and distribute these sample programs in any
 form without payment to IBM for the purposes of developing, using, marketing
 or distributing application programs conforming to the application programming
 interface for the operating platform for which the sample code is written.

 Notwithstanding anything to the contrary, IBM PROVIDES THE SAMPLE SOURCE CODE
 ON AN "AS IS" BASIS AND IBM DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED, INCLUDING,
 BUT NOT LIMITED TO, ANY IMPLIED WARRANTIES OR CONDITIONS OF MERCHANTABILITY,
 SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND ANY WARRANTY OR
 CONDITION OF NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR
 OPERATION OF THE SAMPLE SOURCE CODE. IBM HAS NO OBLIGATION TO PROVIDE
 MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS OR MODIFICATIONS TO THE SAMPLE
 SOURCE CODE.

 (C) Copyright IBM Corp. 2015.

All Rights Reserved. Licensed Materials - Property of IBM.
*/
/*
Copyright (c) 2015 The Polymer Project Authors. All rights reserved.
This code may only be used under the BSD style license found at http://polymer.github.io/LICENSE.txt
The complete set of authors may be found at http://polymer.github.io/AUTHORS.txt
The complete set of contributors may be found at http://polymer.github.io/CONTRIBUTORS.txt
Code distributed by Google as part of the polymer project is also
subject to an additional IP rights grant found at http://polymer.github.io/PATENTS.txt
*/


var env = require('../env-config.json');

(function(document) {
    'use strict';

    // Grab a reference to our auto-binding template
    // and give it some initial binding values
    // Learn more about auto-binding templates at http://goo.gl/Dx1u2g
    var app = document.querySelector('#app');
    var locationSocket;
    var appuser;
    var longitudePrev = 0;
    var latitudePrev = 0;
                
    app.displayInstalledToast = function() {
        // Check to make sure caching is actually enabled—it won't be in the dev environment.
        if (!document.querySelector('platinum-sw-cache').disabled) {
            document.querySelector('#caching-complete').show();
        }
    };

    // Listen for template bound event to know when bindings
    // have resolved and content has been stamped to the page
    app.addEventListener('dom-change', function() {
        console.log('Our app is ready to rock!');
	
	    document.querySelector('get-username').addEventListener('kick', function(e) {
	    console.log('App user is ' + e.detail.q);
	    appuser = e.detail.q;
	
	    //locationSocket = new WebSocket('ws://bluetaglocation1.mybluemix.net/wsLocationResource');
	    locationSocket = new WebSocket(env.location + 'wsLocationResource');
	    locationSocket.onopen = function(msg) {console.log('Socket Open: ' + JSON.stringify(msg));};
	    locationSocket.onmessage = function(msg) {console.log('Server says: ' + JSON.stringify(msg));};
	
	    document.querySelector('my-map').addEventListener('locupdate', function(e) {
	    console.log('Location changed: ' + 'lat: ' + e.detail.lat + ' lon: ' + e.detail.lon);
		
	    if (appuser !== null) {
		    var userloc = {
						_id: appuser,
						longitude: e.detail.lat,
						latitude: e.detail.lon };

		    console.log('Sending location update over socket: ' + JSON.stringify(userloc));
		    //TODO - this socket will timeout if location data is not flowing - need to add error checking
		    locationSocket.send(JSON.stringify(userloc));				  
	    }
    }); // end addEventListener()
	           
    //added this block so map centers once around current location - then only marker will be updated to show marker
    //moving on stationary map. Limits data usage on every map load for testing - to have map re-center on each update comment out
    //getCurrentPosition call and update #g-map in watchPosition call.
      
    navigator.geolocation.getCurrentPosition( function(singlePos){

        var singleLat = singlePos.coords.latitude;
        var singleLon = singlePos.coords.longitude;

        console.log('geting single position to center map once: ' + singleLat + ' , ' + singleLon);
        document.querySelector('#g-map').latitude = singleLat;
        document.querySelector('#g-map').longitude = singleLon;

        document.querySelector('#g-mark').latitude = singleLat;
        document.querySelector('#g-mark').longitude = singleLon;

        document.querySelector('#markitlat').innerHTML = singleLat;
        document.querySelector('#markitlon').innerHTML = singleLon;
    });
            

    var options={enableHighAccuracy: true, timeout: 60000, maximumAge: 10000};
        
    var watchID = navigator.geolocation.watchPosition(
        function(position) {
            if (!(document.querySelector('#onlyupdateonpress').active)) {
                var longitude = position.coords.longitude;
                var latitude = position.coords.latitude;
                var d = getDistanceFromLatLonInKm(latitude,longitude, latitudePrev, longitudePrev)
                 
                console.log('distance ' + d);

                longitudePrev = position.coords.longitude;
                latitudePrev = position.coords.latitude;

                //document.querySelector('#location').innerHTML = latitude + "," + longitude;
             	console.log('app.js watchpos: ' + latitude + ',' + longitude );
				document.querySelector('#latdiv').innerHTML = latitude;
				document.querySelector('#londiv').innerHTML = longitude;
                document.querySelector('#distdiv').innerHTML = d;
              
                document.querySelector('#g-map').latitude = latitude;
                document.querySelector('#g-map').longitude = longitude;
                                         
                document.querySelector('#g-mark').latitude = latitude;
                document.querySelector('#g-mark').longitude = longitude;

                document.querySelector('#markitlat').innerHTML = latitude;
                document.querySelector('#markitlon').innerHTML = longitude;

				if(appuser !== null) {
					console.log(appuser);

					var userloc = {
						_id: appuser,
						longitude: longitude,
						latitude: latitude};																		
                    try {
                        console.log('Trying to send through socket: ' + JSON.stringify(userloc));
						locationSocket.send(JSON.stringify(userloc));
                    } catch(err) {
                        console.log('Failed because: ' + err.message);

                        //locationSocket = new WebSocket('ws://bluetaglocation1.mybluemix.net/wsLocationResource');
                        locationSocket = new WebSocket(env.location + 'wsLocationResource');
                        locationSocket.onopen = function(msg) {console.log('Socket Open')};
                        locationSocket.onmessage = function(msg) {console.log('Server says: ' + JSON.stringify(msg) )};
                        locationSocket.send(JSON.stringify(userloc));
                    }
					document.getElementById('taggable').generateRequest();
				}
            }
        },
        function(error) {
            console.log('Error getting location: ' + error.code + ' ' + error.message);
            console.log('Error getting location');
        }, options);                    
    }); //end watchPosition()
  
    });  //TODO  is this line extra? :)
 
    // See https://github.com/Polymer/polymer/issues/1381
    window.addEventListener('WebComponentsReady', function() {
	    console.log('webcomponents ready');
	    app.route = 'splash';
	    document.querySelector('#paperDrawerPanel').closeDrawer();
	    // imports are loaded and elements have been registered

	    document.querySelector('#meta-config').setAttribute('value', JSON.stringify(env));
	    var urls = document.querySelector('#meta-config').getAttribute('value');
	    
	    document.querySelector('get-username').url = JSON.parse(urls).register + 'api/register/';
	    document.querySelector('get-taggable').url = JSON.parse(urls).engine;
	    document.querySelector('get-taggable').tagUrl = JSON.parse(urls).tag + 'api/tag/';
	    document.querySelector('get-tagged').url = JSON.parse(urls).tag;
	    document.querySelector('mark-it').url = JSON.parse(urls).tag + 'api/markit/newmark/';
	    document.querySelector('my-places').url = JSON.parse(urls).tag + 'api/markit/marked/';
	    document.querySelector('bt-search').url = JSON.parse(urls).search + 'SearchWS/';

	    document.querySelector('ws-element').open(); //opening search socket after url is known - need to change this to work within bt-search
    });

    // Main area's paper-scroll-header-panel custom condensing transformation of
	// the appName in the middle-container and the bottom title in the bottom-container.
	// The appName is moved to top and shrunk on condensing. The bottom sub title
	// is shrunk to nothing on condensing.
	addEventListener('paper-header-transform', function(e) {
	    var appName = document.querySelector('#mainToolbar .app-name');
	    var middleContainer = document.querySelector('#mainToolbar .middle-container');
	    var bottomContainer = document.querySelector('#mainToolbar .bottom-container');
	    var detail = e.detail;
	    var heightDiff = detail.height - detail.condensedHeight;
	    var yRatio = Math.min(1, detail.y / heightDiff);
	    var maxMiddleScale = 0.50;  // appName max size when condensed. The smaller the number the smaller the condensed size.
	    var scaleMiddle = Math.max(maxMiddleScale, (heightDiff - detail.y) / (heightDiff / (1-maxMiddleScale))  + maxMiddleScale);
	    var scaleBottom = 1 - yRatio;

	    // Move/translate middleContainer
	    Polymer.Base.transform('translate3d(0,' + yRatio * 100 + '%,0)', middleContainer);

	    // Scale bottomContainer and bottom sub title to nothing and back
	    //Polymer.Base.transform('scale(' + scaleBottom + ') translateZ(0)', bottomContainer);

	    // Scale middleContainer appName
	    Polymer.Base.transform('scale(' + scaleMiddle + ') translateZ(0)', appName);
    });

    // Close drawer after menu item is selected if drawerPanel is narrow
    app.onDataRouteClick = function() {
        var drawerPanel = document.querySelector('#paperDrawerPanel');
        if (drawerPanel.narrow) {
            drawerPanel.closeDrawer();
        }
    };

    app.singleUpdateLocation = function() {
        if (document.querySelector('#onlyupdateonpress').active){
  	        console.log('Single location update');

            navigator.geolocation.getCurrentPosition( function(singlePos) {

            var singleLat = singlePos.coords.latitude;
            var singleLon = singlePos.coords.longitude;

            console.log('getting single position to center map once: ' + singleLat + ' , ' + singleLon);
            document.querySelector('#g-map').latitude = singleLat;
            document.querySelector('#g-map').longitude = singleLon;

            document.querySelector('#g-mark').latitude = singleLat;
            document.querySelector('#g-mark').longitude = singleLon;

            document.querySelector('#markitlat').innerHTML = singleLat;
            document.querySelector('#markitlon').innerHTML = singleLon;

            if(appuser != null) {
                console.log(appuser);

                var userloc = {
                    _id: appuser,
                    longitude: singleLat,
                    latitude: singleLon };
            
            try {
                console.log('Trying to send through socket: ' + JSON.stringify(userloc));
                locationSocket.send(JSON.stringify(userloc));
            } catch(err) {
                console.log('Failed because: ' + err.message);

                //locationSocket = new WebSocket('ws://bluetaglocation1.mybluemix.net/wsLocationResource');
                locationSocket = new WebSocket(env.location + 'wsLocationResource');
                locationSocket.onopen = function(msg) {console.log('Socket Open')};
                locationSocket.onmessage = function(msg) {console.log('Server says: ' + JSON.stringify(msg) )};
                locationSocket.send(JSON.stringify(userloc));
            }
            document.getElementById('taggable').generateRequest();
        }
        }); //TODO why is this here?
        } else {
            console.log('Pause automatic updates in settings menu to use this');
            document.querySelector('#refreshtooltip').playAnimation();
        }
    };

    // Scroll page to top and expand header
    app.scrollPageToTop = function() {
        document.getElementById('mainContainer').scrollTop = 0;
    };

	function getDistanceFromLatLonInKm(lat1,lon1,lat2,lon2) {
	    //var R = 6371; // Radius of the earth in km
	    var R = 6371000; // Radius of the earth in m
	    var dLat = deg2rad(lat2-lat1);  // deg2rad below
	    var dLon = deg2rad(lon2-lon1); 
	    var a = 
	        Math.sin(dLat/2) * Math.sin(dLat/2) +
	        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
	        Math.sin(dLon/2) * Math.sin(dLon/2)
	    ; 
	    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
	    //var d = R * c; // Distance in km
	    var d = Math.round(R * c *100) / 100; // Distance in m rounded to 2 decimals
	    return d;
    }

    function deg2rad(deg) {
        return deg * (Math.PI/180)
    }

})(document);

},{"../env-config.json":1}]},{},[2]);
