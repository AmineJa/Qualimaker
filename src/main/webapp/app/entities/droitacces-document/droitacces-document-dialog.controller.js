(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DroitaccesDocumentDialogController', DroitaccesDocumentDialogController);

    DroitaccesDocumentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DroitaccesDocument', 'Employe', 'DocumentInterne'];

    function DroitaccesDocumentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DroitaccesDocument, Employe, DocumentInterne) {
        var vm = this;

        vm.droitaccesDocument = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employes = Employe.query();
        vm.documentinternes = DocumentInterne.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.droitaccesDocument.id !== null) {
                DroitaccesDocument.update(vm.droitaccesDocument, onSaveSuccess, onSaveError);
            } else {
                DroitaccesDocument.save(vm.droitaccesDocument, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:droitaccesDocumentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
