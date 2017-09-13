(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DroitaccesDocumentDeleteController',DroitaccesDocumentDeleteController);

    DroitaccesDocumentDeleteController.$inject = ['$uibModalInstance', 'entity', 'DroitaccesDocument'];

    function DroitaccesDocumentDeleteController($uibModalInstance, entity, DroitaccesDocument) {
        var vm = this;

        vm.droitaccesDocument = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DroitaccesDocument.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
