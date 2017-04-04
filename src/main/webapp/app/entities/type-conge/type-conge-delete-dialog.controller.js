(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeCongeDeleteController',TypeCongeDeleteController);

    TypeCongeDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeConge'];

    function TypeCongeDeleteController($uibModalInstance, entity, TypeConge) {
        var vm = this;

        vm.typeConge = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypeConge.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
