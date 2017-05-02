(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CongeDetailController', CongeDetailController);

    CongeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Conge', 'Employe', 'TypeConge'];

    function CongeDetailController($scope, $rootScope, $stateParams, previousState, entity, Conge, Employe, TypeConge) {
        var vm = this;

        vm.conge = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:congeUpdate', function(event, result) {
            vm.conge = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
