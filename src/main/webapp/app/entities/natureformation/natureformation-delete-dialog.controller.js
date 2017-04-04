(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('NatureformationDeleteController',NatureformationDeleteController);

    NatureformationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Natureformation'];

    function NatureformationDeleteController($uibModalInstance, entity, Natureformation) {
        var vm = this;

        vm.natureformation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Natureformation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
