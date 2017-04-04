(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('NatureAbsDetailController', NatureAbsDetailController);

    NatureAbsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'NatureAbs'];

    function NatureAbsDetailController($scope, $rootScope, $stateParams, previousState, entity, NatureAbs) {
        var vm = this;

        vm.natureAbs = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:natureAbsUpdate', function(event, result) {
            vm.natureAbs = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
