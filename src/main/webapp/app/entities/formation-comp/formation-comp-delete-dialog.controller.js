(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FormationCompDeleteController',FormationCompDeleteController);

    FormationCompDeleteController.$inject = ['$uibModalInstance', 'entity', 'FormationComp'];

    function FormationCompDeleteController($uibModalInstance, entity, FormationComp) {
        var vm = this;

        vm.formationComp = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FormationComp.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
