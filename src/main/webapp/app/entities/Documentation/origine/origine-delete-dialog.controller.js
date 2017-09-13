(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('OrigineDeleteController',OrigineDeleteController);

    OrigineDeleteController.$inject = ['$uibModalInstance', 'entity', 'Origine'];

    function OrigineDeleteController($uibModalInstance, entity, Origine) {
        var vm = this;

        vm.origine = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Origine.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
