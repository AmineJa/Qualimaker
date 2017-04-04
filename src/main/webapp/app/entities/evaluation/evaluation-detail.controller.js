(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EvaluationDetailController', EvaluationDetailController);

    EvaluationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Evaluation'];

    function EvaluationDetailController($scope, $rootScope, $stateParams, previousState, entity, Evaluation) {
        var vm = this;

        vm.evaluation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:evaluationUpdate', function(event, result) {
            vm.evaluation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
