(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DocumentInterneDeleteController',DocumentInterneDeleteController);

    DocumentInterneDeleteController.$inject = ['$uibModalInstance', 'entity', 'DocumentInterne'];

    function DocumentInterneDeleteController($uibModalInstance, entity, DocumentInterne) {
        var vm = this;

        vm.documentInterne = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DocumentInterne.delete({id: id},

                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
