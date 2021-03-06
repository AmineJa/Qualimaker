(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FormationDialogController', FormationDialogController);

    FormationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'Formation', 'DemandeFormation', 'FormationComp', 'Formateur', 'Natureformation', 'Fichierjoint', 'Critereevaluation', 'Jour', 'Events', 'Employe'];

    function FormationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, Formation, DemandeFormation, FormationComp, Formateur, Natureformation, Fichierjoint, Critereevaluation, Jour, Events, Employe) {
        var vm = this;

        vm.formation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.demandeformations = DemandeFormation.query({filter: 'formation-is-null'});
        $q.all([vm.formation.$promise, vm.demandeformations.$promise]).then(function() {
            if (!vm.formation.demandeformation || !vm.formation.demandeformation.id) {
                return $q.reject();
            }
            return DemandeFormation.get({id : vm.formation.demandeformation.id}).$promise;
        }).then(function(demandeformation) {
            vm.demandeformations.push(demandeformation);
        });
        vm.formationcomps = FormationComp.query({filter: 'formation-is-null'});
        $q.all([vm.formation.$promise, vm.formationcomps.$promise]).then(function() {
            if (!vm.formation.formationcomp || !vm.formation.formationcomp.id) {
                return $q.reject();
            }
            return FormationComp.get({id : vm.formation.formationcomp.id}).$promise;
        }).then(function(formationcomp) {
            vm.formationcomps.push(formationcomp);
        });
        vm.formateurs = Formateur.query({filter: 'formation-is-null'});
        $q.all([vm.formation.$promise, vm.formateurs.$promise]).then(function() {
            if (!vm.formation.formateur || !vm.formation.formateur.id) {
                return $q.reject();
            }
            return Formateur.get({id : vm.formation.formateur.id}).$promise;
        }).then(function(formateur) {
            vm.formateurs.push(formateur);
        });
        vm.natureformations = Natureformation.query();
        vm.fichierjoints = Fichierjoint.query();
        vm.critereevaluations = Critereevaluation.query();
        vm.jours = Jour.query();
        vm.events = Events.query();
        vm.employes = Employe.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.formation.id !== null) {
                Formation.update(vm.formation, onSaveSuccess, onSaveError);
            } else {
                Formation.save(vm.formation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:formationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateD = false;
        vm.datePickerOpenStatus.dateF = false;
        vm.datePickerOpenStatus.daterec = false;

        vm.setFichjoint = function ($file, formation) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        formation.fichjoint = base64Data;
                        formation.fichjointContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
