(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CalendrierDeleteController',CalendrierDeleteController);

    CalendrierDeleteController.$inject = ['$uibModalInstance', 'entity', 'Calendrier'];

    function CalendrierDeleteController($uibModalInstance, entity, Calendrier) {
        var vm = this;

        vm.calendrier = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Calendrier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
