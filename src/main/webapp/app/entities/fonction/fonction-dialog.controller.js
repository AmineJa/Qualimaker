(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FonctionDialogController', FonctionDialogController);

    FonctionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fonction', 'Employe'];

    function FonctionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Fonction, Employe) {
        var vm = this;

        vm.fonction = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employes = Employe.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.fonction.id !== null) {
                Fonction.update(vm.fonction, onSaveSuccess, onSaveError);
            } else {
                Fonction.save(vm.fonction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:fonctionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
