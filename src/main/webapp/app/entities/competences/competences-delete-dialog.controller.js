(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CompetencesDeleteController',CompetencesDeleteController);

    CompetencesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Competences'];

    function CompetencesDeleteController($uibModalInstance, entity, Competences) {
        var vm = this;

        vm.competences = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Competences.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
