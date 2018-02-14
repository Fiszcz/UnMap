angular.module('YouMap', ['ngAnimate'])
    .constant('LOGOUT_ENDPOINT', '/logout')
    .constant('SITE', 'http://localhost:8080')
    .constant('GET_TRAVEL', '/view/travel/')
    .service('AuthenticationService', function($http, LOGIN_ENDPOINT, LOGOUT_ENDPOINT) {
        this.logout = function(successCallback) {
            $http.post(LOGOUT_ENDPOINT)
                .then(successCallback());
        }
    })
    .service('Travel', function($http){
        this.getTravel = function(target, success) {
            $http.get(target).then( function(response){
                    success(response);
            },
            function(response) {
                console.log(response);
            })
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
    .controller('myCtrl', function ($scope, $http, $window, $rootScope, $location, Travel, GET_TRAVEL, SITE) {

        ////////////
        //variables
        ////////////
        $scope.points = [];
        $scope.travel = {};
        $scope.actualPoint = {};
        $scope.indexActualPoint = -1;
        $rootScope.site = SITE;
        $rootScope.idTravel = {};
        $rootScope.isPhotoSelected = false;
        var joinIdTravel = function (target) {
            return target+$rootScope.idTravel;
        };

        var refresh = function() {
            var url = $location.absUrl().split('=');
            $rootScope.idTravel = url[1];
            var target = joinIdTravel(GET_TRAVEL);
            var getTravel = function (response) {
                $scope.travel = response.data;
                $scope.points = $scope.travel.points;

                sortPoints($scope.points);
                for(var i=0; i<$scope.points.length; i++){
                    $scope.points[i].marker = addMarker($scope.points[i]);
                    $scope.points[i].date = (new Date($scope.points[i].when)).customFormat( "#DDDD# #DD#/#MM#/#YYYY#" );
                }
                setBounds($scope.points);
                createTravelPath($scope.points);
            };
            Travel.getTravel(target,getTravel);
        };

        // refresh();

        /////////////////
        //functions MODEL
        /////////////////



        var sortPoints = function (points) {
            points.sort(function(a, b){return a.when - b.when});
            createTravelPath(points);
        };

        //////////////////////
        //functions CONTROLLER
        //////////////////////

        $scope.setActualPoint = function (index) {
            if($scope.indexActualPoint == index){
                $scope.indexActualPoint = -1;
                $scope.isPhotoSelected = false;
                setBounds($scope.points);
                return;
            }
            $scope.isPhotoSelected = true;
            $scope.indexActualPoint = index;
            $scope.actualPoint = $scope.points[index];
            var point = [];
            point.push($scope.actualPoint);
            setBounds(point, true);
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

        var setBounds = function (points, onlyOne) {
            onlyOne = (typeof onlyOne !== 'undefined') ?  onlyOne : false;

            var bounds  = new google.maps.LatLngBounds();
            for(var i=0; i < points.length; i++) {
                var loc = new google.maps.LatLng(points[i].marker.position.lat(), points[i].marker.position.lng());
                bounds.extend(loc);
            }

            if(onlyOne){
                var extendPoint1 = new google.maps.LatLng(bounds.getNorthEast().lat() + 0.01, bounds.getNorthEast().lng() + 0.01);
                var extendPoint2 = new google.maps.LatLng(bounds.getNorthEast().lat() - 0.01, bounds.getNorthEast().lng() - 0.01);
                bounds.extend(extendPoint1);
                bounds.extend(extendPoint2);
            }

            console.log("center map:" + bounds.getCenter());

            $window.map.fitBounds(bounds);       // auto-zoom
            $window.map.panToBounds(bounds);     // auto-center
        };

        google.maps.event.addDomListener(window, 'load', initialize);
        google.maps.event.addDomListener(window, 'load', refresh);

        //////////////////////
        //functions CONTROLLER
        //////////////////////

        // created marker
        // pick marker to map
        // return reference to marker
        var count=-1;
        function addMarker(point) {
            count++;
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

            var index = Number(count);
            marker.addListener('click', function() {
                $scope.setActualPoint(index);
            });
            // google.maps.event.addListener(marker, 'click', function(evt){
            //     $scope.setActualPoint(count);
            // });
            marker.setOpacity(0.85);
            return marker;
        }
});

//*** This code is copyright 2002-2016 by Gavin Kistner, !@phrogz.net
//*** It is covered under the license viewable at http://phrogz.net/JS/_ReuseLicense.txt
Date.prototype.customFormat = function(formatString){
    var YYYY,YY,MMMM,MMM,MM,M,DDDD,DDD,DD,D,hhhh,hhh,hh,h,mm,m,ss,s,ampm,AMPM,dMod,th;
    YY = ((YYYY=this.getFullYear())+"").slice(-2);
    MM = (M=this.getMonth()+1)<10?('0'+M):M;
    MMM = (MMMM=["January","February","March","April","May","June","July","August","September","October","November","December"][M-1]).substring(0,3);
    DD = (D=this.getDate())<10?('0'+D):D;
    DDD = (DDDD=["Niedziela","Poniedziałek","Wtorek","Środa","Czwartek","Piątek","Sobota"][this.getDay()]).substring(0,3);
    th=(D>=10&&D<=20)?'th':((dMod=D%10)==1)?'st':(dMod==2)?'nd':(dMod==3)?'rd':'th';
    formatString = formatString.replace("#YYYY#",YYYY).replace("#YY#",YY).replace("#MMMM#",MMMM).replace("#MMM#",MMM).replace("#MM#",MM).replace("#M#",M).replace("#DDDD#",DDDD).replace("#DDD#",DDD).replace("#DD#",DD).replace("#D#",D).replace("#th#",th);
    h=(hhh=this.getHours());
    if (h==0) h=24;
    if (h>12) h-=12;
    hh = h<10?('0'+h):h;
    hhhh = hhh<10?('0'+hhh):hhh;
    AMPM=(ampm=hhh<12?'am':'pm').toUpperCase();
    mm=(m=this.getMinutes())<10?('0'+m):m;
    ss=(s=this.getSeconds())<10?('0'+s):s;
    return formatString.replace("#hhhh#",hhhh).replace("#hhh#",hhh).replace("#hh#",hh).replace("#h#",h).replace("#mm#",mm).replace("#m#",m).replace("#ss#",ss).replace("#s#",s).replace("#ampm#",ampm).replace("#AMPM#",AMPM);
};