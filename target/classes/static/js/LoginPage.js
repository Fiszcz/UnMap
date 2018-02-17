angular.module('YouMap', [])
    .constant('LOGIN_ENDPOINT', 'http://localhost:8080/login')
    .constant('REGISTER_ENDPOINT', 'http://localhost:8080/access/register')
    .service('AuthenticationService', function ($http, LOGIN_ENDPOINT, REGISTER_ENDPOINT) {
        this.authenticate = function (credentials, successCallback) {
            var form = new FormData();
            form.append("remember-me", "true");
            var authorization = "Basic "+btoa(credentials.username + ':' + credentials.password);
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": LOGIN_ENDPOINT,
                "method": "POST",
                "headers": {
                    "Authorization": authorization,
                    "Cache-Control": "no-cache"
                },
                "processData": false,
                "contentType": false,
                "mimeType": "multipart/form-data",
                "data": form
            };

            $.ajax(settings).done(function (response) {
                console.log(response);
                window.location.replace("http://localhost:8080/main");
            });
        };
        this.registration = function (user) {
            $http
                .post(REGISTER_ENDPOINT, user)
                .then(function success(value) {

                }, function error(reason) {
                    console.log(reason);
                });
        }
    })
    .controller('logRegForm', function ($scope, $http, $rootScope, AuthenticationService) {
        $scope.red = false;

        $scope.credentials = {};

        $scope.regPassword1 = "";
        $scope.regPassword2 = "";
        $scope.regLogin = "";
        $scope.regEmail = "";

        $scope.colorEmail = {};
        $scope.colorIconEmail = {};
        $scope.colorLogin = {};
        $scope.colorIconLogin = {};
        $scope.colorPassword = {};
        $scope.colorIconPassword = {};

        regExpPassword = new RegExp("(/^(?=.*\d)[0-9a-zA-Z]{7,}$/)");
        regExpEmail = new RegExp("/^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/");

        var loginSuccess = function () {
            $rootScope.authenticated = true;
        };
        $scope.login = function () {
            if ($scope.credentials.username === undefined || $scope.credentials.password === undefined) {
                $scope.red = 4;
                $scope.errorLogin = "Wprowadz dane do logowania!";
                return;
            }
            AuthenticationService.authenticate($scope.credentials, loginSuccess);
        };

        //Do Logowania
        // $scope.wyslij = function () {
        //
        //     vm.credentials.username = $scope.login;
        //     vm.credentials.password = $scope.haslo;
        //     vm.login();
//        $http({
//            method : "POST",
//            url : "gooddfsfs.com"
//        }).then(function sukces(response) {
//            if(response==2){
//                $scope.czerwony = 4;
//                $scope.tekstBledu = "Błędne dane do logowania!";
//            }
//            //przekierowanie
//        }, function error(response) {
//            $scope.czerwony = 4;
//            $scope.tekstBledu = "Błąd podczas połączenia z serwerem";
//        });
//         };

        $scope.checkLogin = function() {
            if($scope.regUsername.length < 4) {
                $scope.colorLogin = "is-invalid";
                $scope.iconLogin = "fa-warning";
                $scope.colorIconLogin = "bg-danger";
                return 1;
            }
            $scope.colorLogin = "is-valid";
            $scope.iconLogin = "";
            $scope.colorIconLogin = "";
        };

        $scope.checkPassword = function() {
            if(!regExpPassword.test($scope.regPassword1) && $scope.regPassword1 != $scope.regPassword2) {
                $scope.colorPassword = "is-invalid";
                $scope.iconPassword = "fa-warning";
                $scope.colorIconPassword = "bg-danger";
                return 1;
            }
            $scope.colorPassword = "is-valid";
            $scope.iconPassword = "";
            $scope.colorIconPassword = "";
        };

        $scope.checkEmail = function() {
            if(!regExpEmail.test($scope.regEmail) && $scope.regPassword1 != $scope.regPassword2) {
                $scope.colorEmail = "is-invalid";
                $scope.iconEmail = "fa-warning";
                $scope.colorIconEmail = "bg-danger d-block";
                return 1;
            }
            $scope.colorEmail = "is-valid";
            $scope.iconEmail = "";
            $scope.colorIconEmail = "";
        };

        //Do Rejestracji
        $scope.register = function () {

            if(checkEmail() || checkPassword() || checkLogin())
                return;

            var user = {};
            user.password = $scope.regPassword1;
            user.username = $scope.regUsername;
            user.email = $scope.regEmail;
            AuthenticationService.registration(user);
            // $http.post('api/registration', details)
            //     .then(function sukces(response) {
            //         vm.credentials.username = $scope.loginRejestracja;
            //         vm.credentials.password = $scope.haslo1;
            //         vm.login();
            //     }, function error(response) {
            //         if (response.data.description == "2") {
            //             $scope.bladLoginu = 1;
            //             $scope.trescBladLoginu = "Taki login już istnieje. Wybierz inny!";
            //         }
            //         else if (response.data.description == "3") {
            //             $scope.bladEmail = 1;
            //             $scope.trescBladEmail = "Taki adres e-mail już widnieje w naszej bazie danych";
            //         }
            //         else
            //             console.log(response);
            //     });
        }
    });