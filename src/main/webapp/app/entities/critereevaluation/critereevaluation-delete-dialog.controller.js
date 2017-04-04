(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CritereevaluationDeleteController',CritereevaluationDeleteController);

    CritereevaluationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Critereevaluation'];

    function CritereevaluationDeleteController($uibModalInstance, entity, Critereevaluation) {
        var vm = this;

        vm.critereevaluation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Critereevaluation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
