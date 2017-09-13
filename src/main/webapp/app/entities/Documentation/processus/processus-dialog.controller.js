(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ProcessusDialogController', ProcessusDialogController);

    ProcessusDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Processus', 'DocumentInterne'];

    function ProcessusDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Processus, DocumentInterne) {
        var vm = this;

        vm.processus = entity;
        vm.clear = clear;
        vm.save = save;
        vm.documentinternes = DocumentInterne.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.processus.id !== null) {
                Processus.update(vm.processus, onSaveSuccess, onSaveError);
            } else {
                Processus.save(vm.processus, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:processusUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
