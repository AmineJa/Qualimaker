(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('NatureAbsDeleteController',NatureAbsDeleteController);

    NatureAbsDeleteController.$inject = ['$uibModalInstance', 'entity', 'NatureAbs'];

    function NatureAbsDeleteController($uibModalInstance, entity, NatureAbs) {
        var vm = this;

        vm.natureAbs = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            NatureAbs.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
