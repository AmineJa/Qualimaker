(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EtatDialogController', EtatDialogController);

    EtatDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Etat'];

    function EtatDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Etat) {
        var vm = this;

        vm.etat = entity;
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
            if (vm.etat.id !== null) {
                Etat.update(vm.etat, onSaveSuccess, onSaveError);
            } else {
                Etat.save(vm.etat, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:etatUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
