(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('AbscenceDeleteController',AbscenceDeleteController);

    AbscenceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Abscence'];

    function AbscenceDeleteController($uibModalInstance, entity, Abscence) {
        var vm = this;

        vm.abscence = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Abscence.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
