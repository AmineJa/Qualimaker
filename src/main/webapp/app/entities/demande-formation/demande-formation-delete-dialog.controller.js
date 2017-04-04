(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DemandeFormationDeleteController',DemandeFormationDeleteController);

    DemandeFormationDeleteController.$inject = ['$uibModalInstance', 'entity', 'DemandeFormation'];

    function DemandeFormationDeleteController($uibModalInstance, entity, DemandeFormation) {
        var vm = this;

        vm.demandeFormation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DemandeFormation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
