(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EtatDeleteController',EtatDeleteController);

    EtatDeleteController.$inject = ['$uibModalInstance', 'entity', 'Etat'];

    function EtatDeleteController($uibModalInstance, entity, Etat) {
        var vm = this;

        vm.etat = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Etat.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
