(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EtatDetailController', EtatDetailController);

    EtatDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Etat'];

    function EtatDetailController($scope, $rootScope, $stateParams, previousState, entity, Etat) {
        var vm = this;

        vm.etat = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:etatUpdate', function(event, result) {
            vm.etat = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
