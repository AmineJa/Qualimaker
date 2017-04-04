(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeFormationDeleteController',TypeFormationDeleteController);

    TypeFormationDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeFormation'];

    function TypeFormationDeleteController($uibModalInstance, entity, TypeFormation) {
        var vm = this;

        vm.typeFormation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypeFormation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
