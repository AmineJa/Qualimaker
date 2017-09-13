(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CarriereDialogController', CarriereDialogController);

    CarriereDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Carriere', 'Employe', 'TypeContrat'];

    function CarriereDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Carriere, Employe, TypeContrat) {
        var vm = this;

        vm.carriere = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.employes = Employe.query();
        vm.typecontrats = TypeContrat.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.carriere.id !== null) {
                Carriere.update(vm.carriere, onSaveSuccess, onSaveError);
            } else {
                Carriere.save(vm.carriere, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:carriereUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.debutINt = false;
        vm.datePickerOpenStatus.finINT = false;
        vm.datePickerOpenStatus.dateRec = false;

        vm.setDocument = function ($file, carriere) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        carriere.document = base64Data;
                        carriere.documentContentType = $file.type;
                    });
                });
            }
        };

        vm.setContrat = function ($file, carriere) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        carriere.contrat = base64Data;
                        carriere.contratContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
