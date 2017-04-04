(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeCongeDialogController', TypeCongeDialogController);

    TypeCongeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeConge'];

    function TypeCongeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeConge) {
        var vm = this;

        vm.typeConge = entity;
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
            if (vm.typeConge.id !== null) {
                TypeConge.update(vm.typeConge, onSaveSuccess, onSaveError);
            } else {
                TypeConge.save(vm.typeConge, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:typeCongeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
