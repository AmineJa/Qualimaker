(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EvaluationDeleteController',EvaluationDeleteController);

    EvaluationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Evaluation'];

    function EvaluationDeleteController($uibModalInstance, entity, Evaluation) {
        var vm = this;

        vm.evaluation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Evaluation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
