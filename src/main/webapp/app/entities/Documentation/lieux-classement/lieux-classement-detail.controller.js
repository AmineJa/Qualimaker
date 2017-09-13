(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('LieuxClassementDetailController', LieuxClassementDetailController);

    LieuxClassementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LieuxClassement'];

    function LieuxClassementDetailController($scope, $rootScope, $stateParams, previousState, entity, LieuxClassement) {
        var vm = this;

        vm.lieuxClassement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:lieuxClassementUpdate', function(event, result) {
            vm.lieuxClassement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
