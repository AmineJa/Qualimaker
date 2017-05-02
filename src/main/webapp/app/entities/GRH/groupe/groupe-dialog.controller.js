(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('GroupeDialogController', GroupeDialogController);

    GroupeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Groupe'];

    function GroupeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Groupe) {
        var vm = this;

        vm.groupe = entity;
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
            if (vm.groupe.id !== null) {
                Groupe.update(vm.groupe, onSaveSuccess, onSaveError);
            } else {
                Groupe.save(vm.groupe, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:groupeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
