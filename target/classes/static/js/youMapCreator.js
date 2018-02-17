angular.module('YouMap', ['ngAnimate'])
    .constant('UPLOAD_ENDPOINT', '/new/photo/')
    .constant('LOGOUT_ENDPOINT', '/logout')
    .constant('NEW_TRAVEL_ENDPOINT', '/new/travel')
    .constant('SITE', 'http://localhost:8080')
    .constant('CHANGE_TITLE_ENDPOINT', '/edit/travel/title/')
    .constant('CHANGE_DESCRIPTION_ENDPOINT', '/edit/travel/description/')
    .constant('DELETE_POINT_ENDPOINT', '/edit/travel/points/delete/')
    .constant('CHANGE_DESCRIPTION_POINT_ENDPOINT', '/edit/travel/points/description/')
    .constant('CHANGE_LOCATION_POINT_ENDPOINT','/edit/travel/points/location/')
    .constant('CHANGE_WHEN_POINT_ENDPOINT', '/edit/travel/points/when/')
    .service('AuthenticationService', function($http, LOGIN_ENDPOINT, LOGOUT_ENDPOINT) {
        this.logout = function(successCallback) {
            $http.post(LOGOUT_ENDPOINT)
                .then(successCallback());
        }
    })
    .service('UploadService', function($http, $rootScope, UPLOAD_ENDPOINT) {
        this.uploadFile = function(point, successCallback) {
            $http.post(UPLOAD_ENDPOINT+$rootScope.idTravel, point)
                .then(function success(response) {
                    successCallback(response.data);
                }, function error(response) {
                    console.log(response);
                });
        }
    })
    .service('NewTravel', function($http, $rootScope, NEW_TRAVEL_ENDPOINT) {
        this.newTravel = function(successCallback, files) {
            $http.get(NEW_TRAVEL_ENDPOINT, {})
                .then(function success(response) {
                    $rootScope.idTravel = response.data.idTravel;
                    successCallback(files);
                }, function error(response) {
                    console.log(response);
                });
            }
        }
    )
    .service('Change', function($http) {
        this.change = function (target, change) {
            $http.post(target, change)
                .then( function success(response) {
                    console.log(response);
                }, function error(response) {
                    console.log(response);
                });
        }
    })
    .directive('ngOnChange', function() {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var onChangeFunc = scope.$eval(attrs.ngOnChange);
                element.bind('change', onChangeFunc);
            }
        };
    })
    .directive('bsTooltip', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                $(element).hover(function () {
                    // on mouseenter
                    $(element).tooltip('show');
                }, function () {
                    // on mouseleave
                    $(element).tooltip('hide');
                });
            }
        };
    })
    .controller('myCtrl', function ($scope, $http, $window, $rootScope, $location, UploadService, NewTravel, Change, SITE, CHANGE_TITLE_ENDPOINT, CHANGE_DESCRIPTION_ENDPOINT, DELETE_POINT_ENDPOINT, CHANGE_DESCRIPTION_POINT_ENDPOINT,CHANGE_LOCATION_POINT_ENDPOINT, CHANGE_WHEN_POINT_ENDPOINT) {

        ////////////
        //variables
        ////////////
        $scope.points = [];
        $rootScope.site = SITE;

        /////////////////
        //functions MODEL
        /////////////////
        var changePercentage = function() {
            $scope.percentage = (($scope.howManyFilesUploaded * 100) / $scope.howManyFiles).toFixed(2);
            if($scope.percentage == 100 && $scope.showSecondPart){
                $scope.showSecondPart = false;
                $scope.showThirdPart = true;
                setBounds($scope.points);
                sortPoints($scope.points);
            }
        };

        //pass in an HTML5 ArrayBuffer, returns a base64 encoded string
        function arrayBufferToBase64( arrayBuffer ) {
            var bytes = new Uint8Array( arrayBuffer );
            var len = bytes.byteLength;
            var binary = '';
            for (var i = 0; i < len; i++) {
                binary += String.fromCharCode( bytes[ i ] );
            }
            return window.btoa( binary );
        }

        var readAndSend = function(files) {
            $scope.howManyFiles = files.length;
            $scope.howManyFilesUploaded = 0;
            $scope.percentage = 1;

            var reader = new FileReader();

            function readFile(index) {
                if (index >= files.length) return;
                var file = files[index];
                reader.onload = function (evt) {
                    var point = {};
                    point.contentType = files[index].type;
                    point.photo = arrayBufferToBase64(evt.target.result);

                    var afterUpload = function(newPoint){
                        newPoint.importantDetail = newPoint.ox != null && newPoint.oy != null && newPoint.when != null;
                        newPoint.whenInput = new Date(newPoint.when);
                        newPoint.marker = addMarker(newPoint);
                        $scope.points.push(newPoint);
                        $scope.howManyFilesUploaded += 1;
                        changePercentage();
                    };
                    UploadService.uploadFile(point, afterUpload);

                    readFile(index + 1)
                };
                reader.readAsArrayBuffer(file);
            }

            readFile(0);
        };

        var joinIdTravel = function (target) {
            return target+$rootScope.idTravel;
        };

        var sortPoints = function (points) {
            points.sort(function(a, b){return a.when - b.when});
            createTravelPath(points);
        };

        //////////////////////
        //functions CONTROLLER
        //////////////////////
        $scope.uploadFiles = function () {
            var files = $('#photos').get(0).files;
            if(files == null){
                return;
            }
            $scope.hideFirstPart = true;
            $scope.showSecondPart = true;
            NewTravel.newTravel(readAndSend, files);
        };

        $scope.changeTitle = function() {
            Change.change(joinIdTravel(CHANGE_TITLE_ENDPOINT), $scope.title);
        };

        $scope.changeDescription = function() {
            Change.change(joinIdTravel(CHANGE_DESCRIPTION_ENDPOINT), $scope.description);
        };

        $scope.changeDescriptionPoint = function () {
            var name = event.target.name;
            var change = {
                code: name,
                description: $scope.points.find(function (obj) { return obj.name === name; }).description
            };
            Change.change(joinIdTravel(CHANGE_DESCRIPTION_POINT_ENDPOINT), change);
        };

        $scope.deletePoint = function (name, index) {
            $scope.points[index].marker.setMap(null);
            $scope.points.splice(index, 1);
            createTravelPath($scope.points);
            Change.change(joinIdTravel(DELETE_POINT_ENDPOINT), {"code": name});
        };

        $scope.changeLocation = function () {
            var name = event.target.name;
            var change = {
                code: name,
                ox: $scope.points.find(function (obj) { return obj.name === name; }).ox,
                oy: $scope.points.find(function (obj) { return obj.name === name; }).oy
            };
            var latlng = new google.maps.LatLng(Number(change.oy), Number(change.ox));
            $scope.points.find(function (obj) { return obj.name === name; }).marker.setPosition(latlng);
            createTravelPath($scope.points);
            Change.change(joinIdTravel(CHANGE_LOCATION_POINT_ENDPOINT), change);
        };

        $scope.changeWhenPoint = function () {
            var name = event.target.name;
            var change = $scope.points.find(function (obj) { return obj.name === name; }).whenInput;
            change = change.getTime();
            $scope.points.find(function (obj) { return obj.name === name; }).when = change;
            change = {
                'code':name,
                'when':change
            };
            sortPoints($scope.points);

            Change.change(joinIdTravel(CHANGE_WHEN_POINT_ENDPOINT), change);
        };

        var setLocationClick = 0;
        $scope.changeLocationByClick = function (name) {
            if(setLocationClick){
                google.maps.event.clearListeners(map, 'click');
            }
            setLocationClick = 1;
            google.maps.event.addListener(map, 'click', function(event) {
                var lat = event.latLng.lat();
                var lng = event.latLng.lng();
                var change = {
                    code: name,
                    ox: lng.toString(),
                    oy: lat.toString()
                };
                $scope.points.find(function (obj) { return obj.name === name; }).ox = change.ox;
                $scope.points.find(function (obj) { return obj.name === name; }).oy = change.oy;
                $scope.points.find(function (obj) { return obj.name === name; }).marker.setPosition(event.latLng);
                createTravelPath($scope.points);
                Change.change(joinIdTravel(CHANGE_LOCATION_POINT_ENDPOINT), change);
                google.maps.event.clearListeners(map, 'click');
                setLocationClick = 0;
            });
        };

        ////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////
        ////Google Maps
        ////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////

        //////////////////////
        //variables
        //////////////////////
        var travelPath;

        var initialize = function() {
            $window.map = new google.maps.Map(document.getElementById('map'), {
                center: {
                    lat: 20, //oy - N/S
                    lng: 0   //ox - W/E
                },
                zoom: 2,
                mapTypeId: 'hybrid'
            });
        };

        //////////////////////
        //functions MODEL
        //////////////////////

        //animation bounce
        function toggleBounce(marker) {
            if (marker.getAnimation() !== null) {
                marker.setAnimation(null);
            } else {
                marker.setAnimation(google.maps.Animation.BOUNCE);
            }
        }

        var createTravelPath = function(points) {
            var pathFromPoint = [];
            for(var i=0; i<points.length; i++)
                pathFromPoint.push({lat: points[i].oy, lng: points[i].ox})
            if(travelPath !== undefined)
                travelPath.setMap(null);
            travelPath = new google.maps.Polyline({
                path: pathFromPoint,
                geodesic: true,
                strokeColor: '#FF0000',
                strokeOpacity: 0.25,
                strokeWeight: 11
            });
            travelPath.setMap(map);
        };

        var setBounds = function (points) {
            var bounds  = new google.maps.LatLngBounds();
            for(var i=0; i < points.length; i++) {
                loc = new google.maps.LatLng(points[i].marker.position.lat(), points[i].marker.position.lng());
                bounds.extend(loc);
            }
            console.log("center map:" + bounds.getCenter());
            map.fitBounds(bounds);       // auto-zoom
            map.panToBounds(bounds);     // auto-center
        };

        google.maps.event.addDomListener(window, 'load', initialize);

        //////////////////////
        //functions CONTROLLER
        //////////////////////
        function addMarker(point) {
            var marker = new google.maps.Marker({
                position: {lat: point.oy, lng: point.ox},
                map: map,
                draggable: true,
                animation: google.maps.Animation.DROP,
                icon: {
                    scaledSize: new google.maps.Size(25, point.height * 25 / point.width),
                    url: SITE + '/photo/' + $rootScope.idTravel + '/' + point.name
                }
            });
            marker.addListener('click', toggleBounce(marker));
            //change location by drag:
            google.maps.event.addListener(marker, 'dragend', function(evt){
                point.ox = evt.latLng.lng();
                point.oy = evt.latLng.lat();
                var change = {
                    code: point.name,
                    ox: evt.latLng.lng().toString(),
                    oy: evt.latLng.lat().toString()
                };
                createTravelPath($scope.points);
                Change.change(joinIdTravel(CHANGE_LOCATION_POINT_ENDPOINT), change);
            });
            marker.setOpacity(0.85);
            return marker;
        }
});