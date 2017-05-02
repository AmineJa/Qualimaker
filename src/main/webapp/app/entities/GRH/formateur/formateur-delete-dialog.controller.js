(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FormateurDeleteController',FormateurDeleteController);

    FormateurDeleteController.$inject = ['$uibModalInstance', 'entity', 'Formateur'];

    function FormateurDeleteController($uibModalInstance, entity, Formateur) {
        var vm = this;

        vm.formateur = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Formateur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
