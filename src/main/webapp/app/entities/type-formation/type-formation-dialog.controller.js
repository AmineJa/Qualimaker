(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeFormationDialogController', TypeFormationDialogController);

    TypeFormationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeFormation'];

    function TypeFormationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeFormation) {
        var vm = this;

        vm.typeFormation = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.typeFormation.id !== null) {
                TypeFormation.update(vm.typeFormation, onSaveSuccess, onSaveError);
            } else {
                TypeFormation.save(vm.typeFormation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:typeFormationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
