(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ServicepostDetailController', ServicepostDetailController);

    ServicepostDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Servicepost', 'Serviice', 'Poste'];

    function ServicepostDetailController($scope, $rootScope, $stateParams, previousState, entity, Servicepost, Serviice, Poste) {
        var vm = this;

        vm.servicepost = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:servicepostUpdate', function(event, result) {
            vm.servicepost = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
