(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('NaturedisciplineDeleteController',NaturedisciplineDeleteController);

    NaturedisciplineDeleteController.$inject = ['$uibModalInstance', 'entity', 'Naturediscipline'];

    function NaturedisciplineDeleteController($uibModalInstance, entity, Naturediscipline) {
        var vm = this;

        vm.naturediscipline = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Naturediscipline.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
