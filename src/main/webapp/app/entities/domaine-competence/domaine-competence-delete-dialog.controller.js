(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DomaineCompetenceDeleteController',DomaineCompetenceDeleteController);

    DomaineCompetenceDeleteController.$inject = ['$uibModalInstance', 'entity', 'DomaineCompetence'];

    function DomaineCompetenceDeleteController($uibModalInstance, entity, DomaineCompetence) {
        var vm = this;

        vm.domaineCompetence = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DomaineCompetence.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
