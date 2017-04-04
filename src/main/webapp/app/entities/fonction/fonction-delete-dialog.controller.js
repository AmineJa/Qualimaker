(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FonctionDeleteController',FonctionDeleteController);

    FonctionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Fonction'];

    function FonctionDeleteController($uibModalInstance, entity, Fonction) {
        var vm = this;

        vm.fonction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Fonction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
