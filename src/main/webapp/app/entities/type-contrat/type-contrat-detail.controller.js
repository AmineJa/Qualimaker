(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeContratDetailController', TypeContratDetailController);

    TypeContratDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypeContrat', 'Carriere'];

    function TypeContratDetailController($scope, $rootScope, $stateParams, previousState, entity, TypeContrat, Carriere) {
        var vm = this;

        vm.typeContrat = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:typeContratUpdate', function(event, result) {
            vm.typeContrat = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
