(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ProfilsfonctionDialogController', ProfilsfonctionDialogController);

    ProfilsfonctionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Profilsfonction', 'Fonction'];

    function ProfilsfonctionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Profilsfonction, Fonction) {
        var vm = this;

        vm.profilsfonction = entity;
        vm.clear = clear;
        vm.save = save;
        vm.fonctions = Fonction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.profilsfonction.id !== null) {
                Profilsfonction.update(vm.profilsfonction, onSaveSuccess, onSaveError);
            } else {
                Profilsfonction.save(vm.profilsfonction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:profilsfonctionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
