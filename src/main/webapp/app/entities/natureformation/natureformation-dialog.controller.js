(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('NatureformationDialogController', NatureformationDialogController);

    NatureformationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Natureformation'];

    function NatureformationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Natureformation) {
        var vm = this;

        vm.natureformation = entity;
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
            if (vm.natureformation.id !== null) {
                Natureformation.update(vm.natureformation, onSaveSuccess, onSaveError);
            } else {
                Natureformation.save(vm.natureformation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:natureformationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
