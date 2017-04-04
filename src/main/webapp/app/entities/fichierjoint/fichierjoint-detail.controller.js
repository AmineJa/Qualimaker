(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FichierjointDetailController', FichierjointDetailController);

    FichierjointDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Fichierjoint'];

    function FichierjointDetailController($scope, $rootScope, $stateParams, previousState, entity, Fichierjoint) {
        var vm = this;

        vm.fichierjoint = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:fichierjointUpdate', function(event, result) {
            vm.fichierjoint = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
