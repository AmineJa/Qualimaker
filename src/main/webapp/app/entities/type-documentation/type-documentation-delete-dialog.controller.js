(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeDocumentationDeleteController',TypeDocumentationDeleteController);

    TypeDocumentationDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeDocumentation'];

    function TypeDocumentationDeleteController($uibModalInstance, entity, TypeDocumentation) {
        var vm = this;

        vm.typeDocumentation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypeDocumentation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
