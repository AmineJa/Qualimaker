(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FichierjointDeleteController',FichierjointDeleteController);

    FichierjointDeleteController.$inject = ['$uibModalInstance', 'entity', 'Fichierjoint'];

    function FichierjointDeleteController($uibModalInstance, entity, Fichierjoint) {
        var vm = this;

        vm.fichierjoint = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Fichierjoint.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
