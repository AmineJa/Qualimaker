(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('IntegreDeleteController',IntegreDeleteController);

    IntegreDeleteController.$inject = ['$uibModalInstance', 'entity', 'Integre'];

    function IntegreDeleteController($uibModalInstance, entity, Integre) {
        var vm = this;

        vm.integre = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Integre.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
