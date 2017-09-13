(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EnregistrementDeleteController',EnregistrementDeleteController);

    EnregistrementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Enregistrement'];

    function EnregistrementDeleteController($uibModalInstance, entity, Enregistrement) {
        var vm = this;

        vm.enregistrement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Enregistrement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
