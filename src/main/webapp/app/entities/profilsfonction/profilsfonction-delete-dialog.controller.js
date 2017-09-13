(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ProfilsfonctionDeleteController',ProfilsfonctionDeleteController);

    ProfilsfonctionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Profilsfonction'];

    function ProfilsfonctionDeleteController($uibModalInstance, entity, Profilsfonction) {
        var vm = this;

        vm.profilsfonction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Profilsfonction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
