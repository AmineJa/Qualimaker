(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EvaluationFormationDeleteController',EvaluationFormationDeleteController);

    EvaluationFormationDeleteController.$inject = ['$uibModalInstance', 'entity', 'EvaluationFormation'];

    function EvaluationFormationDeleteController($uibModalInstance, entity, EvaluationFormation) {
        var vm = this;

        vm.evaluationFormation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EvaluationFormation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
