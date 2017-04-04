(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CritereevaluationDetailController', CritereevaluationDetailController);

    CritereevaluationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Critereevaluation'];

    function CritereevaluationDetailController($scope, $rootScope, $stateParams, previousState, entity, Critereevaluation) {
        var vm = this;

        vm.critereevaluation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:critereevaluationUpdate', function(event, result) {
            vm.critereevaluation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
