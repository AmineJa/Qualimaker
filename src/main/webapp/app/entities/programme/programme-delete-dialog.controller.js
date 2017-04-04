(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ProgrammeDeleteController',ProgrammeDeleteController);

    ProgrammeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Programme'];

    function ProgrammeDeleteController($uibModalInstance, entity, Programme) {
        var vm = this;

        vm.programme = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Programme.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
