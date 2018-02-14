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

        //Do Rejestracji
        $scope.register = function () {
            $scope.errorRegPassword = false;
            $scope.errorRegUsername = false;
            $scope.errorRegEmail = false;
            if (('' + $scope.regUsername).length < 5 || $scope.regUsername === undefined) {
                $scope.errorRegUsername = true;
                $scope.textErrorRegUsername = "Podałeś zbyt krótki login";
            }
            if (('' + $scope.regPassword1).length < 7 || $scope.regPassword1 === undefined) {
                $scope.errorRegPassword = true;
                $scope.textErrorRegPassword = "Zbyt krótkie hasło!";
            }
            else if ($scope.regPassword1 != $scope.regPassword2) {
                $scope.errorRegPassword = true;
                $scope.textErrorRegPassword = "Podane hasła nie są takie same!";
            }
            if ($scope.regEmail === undefined) {
                $scope.errorRegEmail = true;
                $scope.textErrorRegEmail = "Do rejestracji potrzebny jest twój adres E-mail!";
            }

            if ($scope.errorRegUsername || $scope.errorRegPassword || $scope.errorRegEmail)
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