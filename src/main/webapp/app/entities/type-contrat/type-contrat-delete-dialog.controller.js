(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeContratDeleteController',TypeContratDeleteController);

    TypeContratDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeContrat'];

    function TypeContratDeleteController($uibModalInstance, entity, TypeContrat) {
        var vm = this;

        vm.typeContrat = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypeContrat.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
