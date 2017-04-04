(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('NatureformationDetailController', NatureformationDetailController);

    NatureformationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Natureformation'];

    function NatureformationDetailController($scope, $rootScope, $stateParams, previousState, entity, Natureformation) {
        var vm = this;

        vm.natureformation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:natureformationUpdate', function(event, result) {
            vm.natureformation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
