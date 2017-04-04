(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeFormateurDeleteController',TypeFormateurDeleteController);

    TypeFormateurDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeFormateur'];

    function TypeFormateurDeleteController($uibModalInstance, entity, TypeFormateur) {
        var vm = this;

        vm.typeFormateur = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypeFormateur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
