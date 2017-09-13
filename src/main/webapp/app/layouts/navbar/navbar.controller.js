(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('NavbarController', NavbarController)
        .directive('myCurrentTime', ['$interval', 'dateFilter', function($interval, dateFilter) {

            function link(scope, element, attrs) {
                var format,
                    timeoutId;

                function updateTime() {
                    element.text(dateFilter(new Date(), format));
                }

                scope.$watch(attrs.myCurrentTime, function(value) {
                    format = value;
                    updateTime();
                });

                element.on('$destroy', function() {
                    $interval.cancel(timeoutId);
                });

                // start the UI update process; save the timeoutId for canceling
                timeoutId = $interval(function() {
                    updateTime(); // update DOM
                }, 1000);
            }

            return {
                link: link
            };
        }]);

    NavbarController.$inject = ['$state', 'Auth','Employe', 'Principal', 'ProfileService', 'LoginService'];

    function NavbarController ($state, Auth, Employe,Principal, ProfileService, LoginService) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.employes =Employe.query() ;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;
        vm.format = 'MM-dd-yyyy  hh:mm:ss a';
        function login() {
           collapseNavbar();
          LoginService.open();



        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            LoginService.open();
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }

        vm.error = null;
        vm.save = save;
        vm.settingsAccount = null;
        vm.success = null;

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login
            };
        };

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
        });

        function save () {
            Auth.updateAccount(vm.settingsAccount).then(function() {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function(account) {
                    vm.settingsAccount = copyAccount(account);
                });
                JhiLanguageService.getCurrent().then(function(current) {
                    if (vm.settingsAccount.langKey !== current) {
                        $translate.use(vm.settingsAccount.langKey);
                    }
                });
            }).catch(function() {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }
    }
})();
