(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('RemplacerDeleteController',RemplacerDeleteController);

    RemplacerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Remplacer'];

    function RemplacerDeleteController($uibModalInstance, entity, Remplacer) {
        var vm = this;

        vm.remplacer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Remplacer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
