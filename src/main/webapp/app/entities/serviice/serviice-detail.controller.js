(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ServiiceDetailController', ServiiceDetailController);

    ServiiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Serviice'];

    function ServiiceDetailController($scope, $rootScope, $stateParams, previousState, entity, Serviice) {
        var vm = this;

        vm.serviice = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:serviiceUpdate', function(event, result) {
            vm.serviice = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
