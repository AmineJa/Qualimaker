(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CarriereDetailController', CarriereDetailController);

    CarriereDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Carriere'];

    function CarriereDetailController($scope, $rootScope, $stateParams, previousState, entity, Carriere) {
        var vm = this;

        vm.carriere = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:carriereUpdate', function(event, result) {
            vm.carriere = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
