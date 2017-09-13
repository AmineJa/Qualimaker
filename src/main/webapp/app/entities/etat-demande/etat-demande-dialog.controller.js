(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EtatDemandeDialogController', EtatDemandeDialogController);

    EtatDemandeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EtatDemande'];

    function EtatDemandeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EtatDemande) {
        var vm = this;

        vm.etatDemande = entity;
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
            if (vm.etatDemande.id !== null) {
                EtatDemande.update(vm.etatDemande, onSaveSuccess, onSaveError);
            } else {
                EtatDemande.save(vm.etatDemande, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:etatDemandeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
