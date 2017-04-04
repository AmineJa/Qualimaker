(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FormateurDialogController', FormateurDialogController);

    FormateurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Formateur', 'TypeFormateur'];

    function FormateurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Formateur, TypeFormateur) {
        var vm = this;

        vm.formateur = entity;
        vm.clear = clear;
        vm.save = save;
        vm.typeformateurs = TypeFormateur.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.formateur.id !== null) {
                Formateur.update(vm.formateur, onSaveSuccess, onSaveError);
            } else {
                Formateur.save(vm.formateur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:formateurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
