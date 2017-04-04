(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FormationCompDetailController', FormationCompDetailController);

    FormationCompDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FormationComp'];

    function FormationCompDetailController($scope, $rootScope, $stateParams, previousState, entity, FormationComp) {
        var vm = this;

        vm.formationComp = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:formationCompUpdate', function(event, result) {
            vm.formationComp = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
