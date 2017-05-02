(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EvaluationFormationDetailController', EvaluationFormationDetailController);

    EvaluationFormationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EvaluationFormation', 'Formation'];

    function EvaluationFormationDetailController($scope, $rootScope, $stateParams, previousState, entity, EvaluationFormation, Formation) {
        var vm = this;

        vm.evaluationFormation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:evaluationFormationUpdate', function(event, result) {
            vm.evaluationFormation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
