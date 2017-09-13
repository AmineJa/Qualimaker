(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeDocumentationDialogController', TypeDocumentationDialogController);

    TypeDocumentationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeDocumentation', 'Employe', 'DocumenExterne'];

    function TypeDocumentationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeDocumentation, Employe, DocumenExterne) {
        var vm = this;

        vm.typeDocumentation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employes = Employe.query();
        vm.documenexternes = DocumenExterne.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.typeDocumentation.id !== null) {

                TypeDocumentation.update(vm.typeDocumentation, onSaveSuccess, onSaveError);
            } else {

                TypeDocumentation.save(vm.typeDocumentation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:typeDocumentationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
