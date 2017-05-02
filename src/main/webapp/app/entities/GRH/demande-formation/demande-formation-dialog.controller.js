(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DemandeFormationDialogController', DemandeFormationDialogController);

    DemandeFormationDialogController.$inject = ['$timeout','$state' ,'$scope', '$stateParams', '$uibModalInstance', 'entity', 'DemandeFormation', 'Employe'];

    function DemandeFormationDialogController ($timeout, $state,$scope, $stateParams, $uibModalInstance, entity, DemandeFormation, Employe) {
        var vm = this;

        vm.demandeFormation = entity;
        vm.clear = clear;
        vm.accepter = accepter;
       vm.refuser=refuser;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.demandeFormation.id !== null) {
                DemandeFormation.update(vm.demandeFormation, onSaveSuccess, onSaveError);
            } else {
                vm.demandeFormation.dateDemande=new Date();
                vm.demandeFormation.etatD ="Enattente";
                DemandeFormation.save(vm.demandeFormation, onSaveSuccess, onSaveError);
            }
        }

        function accepter () {
            vm.isSaving = true;

                vm.demandeFormation.etatD ="Valider";
                DemandeFormation.update(vm.demandeFormation, onSaveSuccess, onSaveError);

            $state.go('formation', null, { reload: 'demande' });
            $state.go('formation');


        }
        function refuser () {
            vm.isSaving = true;

            vm.demandeFormation.etatD ="Refuser";
            DemandeFormation.update(vm.demandeFormation, onSaveSuccess, onSaveError);

        }
        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:demandeFormationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }


        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateDemande = false;
        vm.datePickerOpenStatus.datesouhaite = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
