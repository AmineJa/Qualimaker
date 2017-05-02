(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('PosteDeleteController',PosteDeleteController);

    PosteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Poste'];

    function PosteDeleteController($uibModalInstance, entity, Poste) {
        var vm = this;

        vm.poste = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Poste.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
