angular.module('YouMap', ['ngAnimate'])
    .constant('LOGOUT_ENDPOINT', '/logout')
    .constant('SITE', 'http://localhost:8080')
    .constant('CHANGE_NAME_ENDPOINT', '/editProfile/setName')
    .constant('CHANGE_PASSWORD_ENDPOINT', '/editProfile/changePassword')
    .constant('CHANGE_EMAIL_ENDPOINT', '/editProfile/changeEmail')
    .constant('DELETE_ACCOUNT_ENDPOINT', '/editProfile/deleteAccount')
    .constant('GET_PROFILE_ENDPOINT', '/profile/get')
    .service('AuthenticationService', function($http, LOGIN_ENDPOINT, LOGOUT_ENDPOINT) {
        this.logout = function(successCallback) {
            $http.post(LOGOUT_ENDPOINT)
                .then(successCallback());
        }
    })
    .service('Change', function($http, CHANGE_EMAIL_ENDPOINT, CHANGE_PASSWORD_ENDPOINT, DELETE_ACCOUNT_ENDPOINT, CHANGE_NAME_ENDPOINT) {
        this.changePassword = function (password) {
            $http.post(CHANGE_PASSWORD_ENDPOINT, passwords)
                .then( function success(response) {
                    console.log(response);
                }, function error(response) {
                    console.log(response);
                });
        };
        this.changeName = function (json) {
            $http.post(CHANGE_NAME_ENDPOINT, json)
                .then( function success(response) {
                    console.log(response);
                }, function error(response) {
                    console.log(response);
                });
        };
        this.deleteAccount = function (password) {
            $http.post(CHANGE_NAME_ENDPOINT, password)
                .then( function success(response) {
                    console.log(response);
                }, function error(response) {
                    console.log(response);
                });
        };
        this.changeEmail = function (email) {
            $http.post(CHANGE_EMAIL_ENDPOINT, email)
                .then( function success(response) {
                    console.log(response);
                }, function error(response) {
                    console.log(response);
                });
        };
    })
    .service('Profile', function($http, GET_PROFILE_ENDPOINT) {
        this.getProfile = function () {
            $http.get(GET_PROFILE_ENDPOINT)
                .then( function success(response) {
                    return response.data;
                }, function error(response) {
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
    .controller('myCtrl', function ($scope, $http, $window, $rootScope, $location, SITE, Change, Profile) {

        ////////////
        //variables
        ////////////
        $rootScope.site = SITE;

        var refresh = function() {
            var profile = Profile.getProfile();
            // if(profile.name !== undefined)
            //     $scope.name = profile.name;
            // if(profile.surname !== undefined)
            //     $scope.surname = profile.surname;
            // $scope.email = profile.email;
        };
        refresh();

        /////////////////
        //functions MODEL
        /////////////////



        /////////////////
        //functions CONTROLLER
        /////////////////
        var changeName = function(){
            json = {
                "name": $scope.name,
                "surname": $scope.surname
            };
            Change.changeName(json);
        };

        var changePassword = function() {
            var passwords = {
                "oldPassword": $scope.oldPassword,
                "newPassword": $scope.newPassword1
            };
            Change.changePassword(passwords);
            $scope.oldPassword = "";
            $scope.newPassword1 = "";
            $scope.newPassword2 = "";
        };

        var changeEmail = function() {
            Change.changeEmail($scope.email);
        };

        var deleteAccount = function() {
            Change.deleteAccount($scope.passwordForDelete);
        };

    });