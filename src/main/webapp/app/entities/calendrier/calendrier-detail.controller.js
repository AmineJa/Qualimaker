(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CalendrierDetailController', CalendrierDetailController);

    CalendrierDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Calendrier', 'Jour'];

    function CalendrierDetailController($scope, $rootScope, $stateParams, previousState, entity, Calendrier, Jour) {
        var vm = this;

        vm.calendrier = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:calendrierUpdate', function(event, result) {
            vm.calendrier = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
