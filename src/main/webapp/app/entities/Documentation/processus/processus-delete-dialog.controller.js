(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ProcessusDeleteController',ProcessusDeleteController);

    ProcessusDeleteController.$inject = ['$uibModalInstance', 'entity', 'Processus'];

    function ProcessusDeleteController($uibModalInstance, entity, Processus) {
        var vm = this;

        vm.processus = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Processus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
