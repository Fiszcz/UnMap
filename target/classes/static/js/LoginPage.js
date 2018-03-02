angular.module('YouMap', [])
    .constant('LOGIN_ENDPOINT', 'http://localhost:8080/login')
    .constant('REGISTER_ENDPOINT', 'http://localhost:8080/access/register')
    .service('AuthenticationService', function ($http, LOGIN_ENDPOINT, REGISTER_ENDPOINT) {
        this.authenticate = function (credentials, unsuccessCallback) {
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
            }).fail(unsuccessCallback({data:{code: "dataLogin.invalid", message: "Username or password is incorrect"}}));
        };
        this.registration = function (user, successCallback, unsuccessCallback) {
            $http
                .post(REGISTER_ENDPOINT, user)
                .then(successCallback, unsuccessCallback);
        }
    })
    .controller('logRegForm', function ($scope, $http, $rootScope, AuthenticationService) {
        $scope.red = false;

        $scope.credentials = {};

        $scope.titleLogin = 'Login składający się z co najmniej 4 znaków';
        $scope.titlePassword = "Hasło z co najmniej 7 znaków w tym z cyfr";

        $scope.regPassword1 = "";
        $scope.regPassword2 = "";
        $scope.regLogin = "";
        $scope.regEmail = "";

        $scope.iconLogin='fa-question-circle-o';
        $scope.iconPassword='fa-question-circle-o';
        $scope.colorEmail = {};
        $scope.colorIconEmail = {};
        $scope.colorLogin = {};
        $scope.colorIconLogin = {};
        $scope.colorPassword = {};
        $scope.colorIconPassword = {};

        $scope.errorsRegister = [];
        var invalidEmail;
        var invalidUsername;

        var regExpPassword = new RegExp("^(?=[^\d_].*?\d)\w(\w|[!@#$%]){7,50}");
        var regExpEmail = new RegExp("^[^@]+@[^@]+.[^@]{2,}$");

        $scope.login = function () {
            if ($scope.credentials.username === undefined || $scope.credentials.password === undefined) {
                $scope.red = 4;
                $scope.errorLogin = "Wprowadź dane do logowania!";
                return;
            }
            AuthenticationService.authenticate($scope.credentials, setAlertLogin);
        };

        $scope.checkLogin = function(after = true) {
            if($scope.regUsername.length < 4 && after) {
                $scope.colorLogin = "is-invalid";
                $scope.iconLogin = "fa-warning";
                $scope.colorIconLogin = "bg-danger";
            } else if(invalidUsername != $scope.regUsername) {
                $scope.colorLogin = "is-valid";
                $scope.iconLogin = "fa-question-circle-o";
                $scope.colorIconLogin = "";
            }
        };

        $scope.checkPassword = function(after = true) {
            if(!regExpPassword.test($scope.regPassword1) && $scope.regPassword1 != $scope.regPassword2 && after) {
                $scope.colorPassword = "is-invalid";
                $scope.iconPassword = "fa-warning";
                $scope.colorIconPassword = "bg-danger";
                return 1;
            }
            $scope.colorPassword = "is-valid";
            $scope.iconPassword = "fa-question-circle-o";
            $scope.colorIconPassword = "";
        };

        $scope.checkEmail = function(after = true) {
            if(!regExpEmail.test($scope.regEmail) && after) {
                $scope.colorEmail = "is-invalid";
                $scope.iconEmail = "fa-warning";
                $scope.colorIconEmail = "bg-danger d-block";
            } else if(invalidEmail != $scope.regEmail) {
                $scope.colorEmail = "is-valid";
                $scope.iconEmail = "";
                $scope.colorIconEmail = "";
            }
        };

        $scope.clearRegisterForm = function () {
            $scope.regEmail = "";
            $scope.regPassword1 = "";
            $scope.regPassword2 = "";
            $scope.regUsername = "";
            $scope.checkEmail(false);
            $scope.checkLogin(false);
            $scope.checkPassword(false);
            $scope.textAlertRegister = "";
            $scope.classAlertRegister = "";
        };

        var setAlertRegister = function(response) {
            response = response.data;
            var classDanger = 'alert alert-danger';
            var classWarning = 'alert alert-warning';

            for(var i=0; i<response.length; i++){
                var error = response[i];
                switch(error.code){
                    case 'username.syntax':
                    case 'email.syntax':
                        $scope.errorsRegister.push({text: error.message, class: classDanger});
                        return;
                    case 'username.repeated':
                        $scope.colorLogin = "is-warning";
                        $scope.iconLogin = "fa-warning";
                        $scope.colorIconLogin = "bg-warning";
                        invalidUsername = $scope.regUsername.toString();
                        $scope.errorsRegister.push({text: error.message, class: classWarning});
                        break;
                    case 'email.repeated':
                        $scope.colorEmail = "is-warning";
                        $scope.iconEmail = "fa-warning";
                        $scope.colorIconEmail = "bg-warning d-block";
                        invalidEmail = $scope.regEmail.toString();
                        $scope.errorsRegister.push({text: error.message, class: classWarning});
                        break;
                }}
        };

        var setAlertLogin = function (response) {
            response = response.data;
            $scope.textAlertLogin = response.message;
            switch(response.code){
                case 'dataLogin.invalid':
                    $scope.classAlertLogin = 'd-block alert-warning';
                    $scope.credentials.password = "";
                    break;
                case 'register.done':
                    $scope.classAlertLogin = 'd-block alert-success';
                    $scope.clearRegisterForm();
                    $('[href="#login"]').tab('show');
                    break;
            }
        };

        //Registration
        $scope.register = function () {

            if($scope.checkEmail() || $scope.checkPassword() || $scope.checkLogin())
                return;

            var user = {};
            user.password = $scope.regPassword1;
            user.username = $scope.regUsername;
            user.email = $scope.regEmail;

            //clear alerts for login and register forms
            $scope.errorsRegister = [];
            $scope.classAlertLogin = "";
            //clear password fields
            $scope.regPassword1 = "";
            $scope.regPassword2 = "";

            AuthenticationService.registration(user, setAlertLogin, setAlertRegister);
        }
    });