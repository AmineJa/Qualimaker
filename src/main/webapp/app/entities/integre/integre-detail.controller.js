(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('IntegreDetailController', IntegreDetailController);

    IntegreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Integre'];

    function IntegreDetailController($scope, $rootScope, $stateParams, previousState, entity, Integre) {
        var vm = this;

        vm.integre = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:integreUpdate', function(event, result) {
            vm.integre = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
