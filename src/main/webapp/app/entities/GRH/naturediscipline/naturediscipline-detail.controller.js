(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('NaturedisciplineDetailController', NaturedisciplineDetailController);

    NaturedisciplineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Naturediscipline'];

    function NaturedisciplineDetailController($scope, $rootScope, $stateParams, previousState, entity, Naturediscipline) {
        var vm = this;

        vm.naturediscipline = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:naturedisciplineUpdate', function(event, result) {
            vm.naturediscipline = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
