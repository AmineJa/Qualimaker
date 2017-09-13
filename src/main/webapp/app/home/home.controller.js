(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('HomeController', HomeController)
        .directive('calender',CalenderDirective);



    HomeController.$inject = ['$timeout','$scope','Employe', 'DocumentInterne','Principal','LoginService','Events' ,'Conge','$state', 'Carriere','Formation'];

    function HomeController ($timeout,$scope,Employe,DocumentInterne, Principal ,LoginService, Events,Conge,$state,Carriere,Formation) {
        var vm = this;

        vm.end=[];
        vm.start=[];
        vm.status=true;
        vm.calculateNumberDoc=calculateNumberDoc;
        vm.calculateNumberconge=calculateNumberconge;
        vm.calculateNumberformation=calculateNumberformation;

        vm.c=80;
        vm.calculateActive=calculateActive;
        vm.date =new Date();
        vm.n=0;
        vm.numberdoc=0
        vm.numberconge=0
        vm.numberformation=0
        vm.calculate=calculate;
        vm.settingsAccount = null;
        vm.success = null;
        vm.numberEvents=numberEvents;
        vm.account = null;
        vm.conge=Conge.query();
        vm.carrieres=Carriere.query();
        vm.formation=Formation.query();
        vm.employes =Employe.query() ;
        vm.documentInterne=DocumentInterne.query();
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.events=Events.query();

        vm.register = register;

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();
        function calculateNumberDoc() {
            vm.numberdoc +=1;

        }
        function calculateNumberconge() {
            vm.numberconge +=1;

        }
        function calculateNumberformation() {
            vm.numberformation +=1;

        }
    function calculateActive() {
     vm.c +=1;
      }
       function numberEvents(events) {

        vm.start.push( events.start.toString());
         vm.end.push(events.end.toString());
       }

        function calculate() {
              vm.n += 1;
            }

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }




    }
    function CalenderDirective() {
        return {
            templateUrl: 'app/entities/events/events.html'
        };
    }
})(window.angular);
