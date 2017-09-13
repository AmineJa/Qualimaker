(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('LieuxClassementDeleteController',LieuxClassementDeleteController);

    LieuxClassementDeleteController.$inject = ['$uibModalInstance', 'entity', 'LieuxClassement'];

    function LieuxClassementDeleteController($uibModalInstance, entity, LieuxClassement) {
        var vm = this;

        vm.lieuxClassement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LieuxClassement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
