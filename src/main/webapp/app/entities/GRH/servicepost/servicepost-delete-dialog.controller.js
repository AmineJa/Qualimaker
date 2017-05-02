(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ServicepostDeleteController',ServicepostDeleteController);

    ServicepostDeleteController.$inject = ['$uibModalInstance', 'entity', 'Servicepost'];

    function ServicepostDeleteController($uibModalInstance, entity, Servicepost) {
        var vm = this;

        vm.servicepost = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Servicepost.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
