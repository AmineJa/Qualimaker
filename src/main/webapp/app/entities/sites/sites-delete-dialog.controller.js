(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('SitesDeleteController',SitesDeleteController);

    SitesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Sites'];

    function SitesDeleteController($uibModalInstance, entity, Sites) {
        var vm = this;

        vm.sites = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Sites.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
