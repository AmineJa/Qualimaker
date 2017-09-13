(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EtatDemandeDeleteController',EtatDemandeDeleteController);

    EtatDemandeDeleteController.$inject = ['$uibModalInstance', 'entity', 'EtatDemande'];

    function EtatDemandeDeleteController($uibModalInstance, entity, EtatDemande) {
        var vm = this;

        vm.etatDemande = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EtatDemande.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
