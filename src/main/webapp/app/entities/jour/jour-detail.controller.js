(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('JourDetailController', JourDetailController);

    JourDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Jour', 'Programme', 'Calendrier'];

    function JourDetailController($scope, $rootScope, $stateParams, previousState, entity, Jour, Programme, Calendrier) {
        var vm = this;

        vm.jour = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:jourUpdate', function(event, result) {
            vm.jour = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
