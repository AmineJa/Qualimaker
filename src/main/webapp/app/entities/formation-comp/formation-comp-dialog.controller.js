(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FormationCompDialogController', FormationCompDialogController);

    FormationCompDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FormationComp'];

    function FormationCompDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FormationComp) {
        var vm = this;

        vm.formationComp = entity;
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
            if (vm.formationComp.id !== null) {
                FormationComp.update(vm.formationComp, onSaveSuccess, onSaveError);
            } else {
                FormationComp.save(vm.formationComp, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:formationCompUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
