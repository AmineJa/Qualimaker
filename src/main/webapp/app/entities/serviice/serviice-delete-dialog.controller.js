(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ServiiceDeleteController',ServiiceDeleteController);

    ServiiceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Serviice'];

    function ServiiceDeleteController($uibModalInstance, entity, Serviice) {
        var vm = this;

        vm.serviice = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Serviice.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
