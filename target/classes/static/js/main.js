angular.module('YouMap', ['ngAnimate'])
    .constant('LOGOUT_ENDPOINT', '/logout')
    .constant('SITE', 'http://localhost:8080')
    .constant('GET_TRAVELS_POINT', '/travel/get/all/forUser')
    .constant('NEW_ACCESS_KEY', '/access/travel/newCode/')
    .service('AuthenticationService', function($http, LOGIN_ENDPOINT, LOGOUT_ENDPOINT) {
        this.logout = function(successCallback) {
            $http.post(LOGOUT_ENDPOINT)
                .then(successCallback());
        }
    })
    .service('TravelService', function($http, $rootScope,  GET_TRAVELS_POINT){
        this.getTravels = function() {
            $http.get(GET_TRAVELS_POINT).
                then(function success(response) {
                    $rootScope.travels=  response.data;
                    for(var i=0; i<$rootScope.travels.length; i++) {
                        $rootScope.travels[i].howManyPoints = $rootScope.travels[i].points.length;
                        $rootScope.travels[i].points = $rootScope.travels[i].points[Math.floor((Math.random() * ($rootScope.travels[i].points.length - 1)) + 0)];
                    }
            })
        };
    })
    .service('Change', function($http) {
        this.changeAccessKeyTravel = function (target , uploaded) {
            $http.get(target)
                .then( function(response) {
                        uploaded(response)
                    }
                    , function error(response) {
                    console.log(response);
                });
        }
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
    .controller('myCtrl', function ($scope, $http, $window, $rootScope, $location, SITE, TravelService, Change, NEW_ACCESS_KEY) {

        ////////////
        //variables
        ////////////
        $rootScope.site = SITE;
        $rootScope.travels = [];

        var refresh = function() {
            TravelService.getTravels();
        };
        refresh();

        /////////////////
        //functions MODEL
        /////////////////

        var joinIdTravel = function (target, idTravel) {
            return target+idTravel;
        };

        /////////////////
        //functions CONTROLLER
        /////////////////

        $scope.generateNewKeyTravel = function (index){
            var uploaded = function (response) {
                $rootScope.travels[index].code = response.data.code;
            };
            Change.changeAccessKeyTravel( joinIdTravel(NEW_ACCESS_KEY, $rootScope.travels[index].code ), uploaded);
        };


    });