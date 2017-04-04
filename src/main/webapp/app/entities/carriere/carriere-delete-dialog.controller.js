(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CarriereDeleteController',CarriereDeleteController);

    CarriereDeleteController.$inject = ['$uibModalInstance', 'entity', 'Carriere'];

    function CarriereDeleteController($uibModalInstance, entity, Carriere) {
        var vm = this;

        vm.carriere = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Carriere.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
