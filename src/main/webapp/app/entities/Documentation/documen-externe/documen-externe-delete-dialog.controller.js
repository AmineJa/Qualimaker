(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DocumenExterneDeleteController',DocumenExterneDeleteController);

    DocumenExterneDeleteController.$inject = ['$uibModalInstance', 'entity', 'DocumenExterne'];

    function DocumenExterneDeleteController($uibModalInstance, entity, DocumenExterne) {
        var vm = this;

        vm.documenExterne = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DocumenExterne.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
