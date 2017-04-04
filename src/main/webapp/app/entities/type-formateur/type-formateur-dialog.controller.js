(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeFormateurDialogController', TypeFormateurDialogController);

    TypeFormateurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeFormateur', 'Formateur'];

    function TypeFormateurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeFormateur, Formateur) {
        var vm = this;

        vm.typeFormateur = entity;
        vm.clear = clear;
        vm.save = save;
        vm.formateurs = Formateur.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.typeFormateur.id !== null) {
                TypeFormateur.update(vm.typeFormateur, onSaveSuccess, onSaveError);
            } else {
                TypeFormateur.save(vm.typeFormateur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:typeFormateurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
