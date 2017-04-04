(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeFormationDetailController', TypeFormationDetailController);

    TypeFormationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypeFormation'];

    function TypeFormationDetailController($scope, $rootScope, $stateParams, previousState, entity, TypeFormation) {
        var vm = this;

        vm.typeFormation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:typeFormationUpdate', function(event, result) {
            vm.typeFormation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
