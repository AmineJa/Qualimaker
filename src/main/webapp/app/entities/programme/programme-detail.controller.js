(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ProgrammeDetailController', ProgrammeDetailController);

    ProgrammeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Programme'];

    function ProgrammeDetailController($scope, $rootScope, $stateParams, previousState, entity, Programme) {
        var vm = this;

        vm.programme = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:programmeUpdate', function(event, result) {
            vm.programme = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
