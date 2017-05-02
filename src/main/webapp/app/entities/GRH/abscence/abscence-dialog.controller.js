(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('AbscenceDialogController', AbscenceDialogController);

    AbscenceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Abscence', 'Employe', 'NatureAbs'];

    function AbscenceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Abscence, Employe, NatureAbs) {
        var vm = this;

        vm.abscence = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.employes = Employe.query();
        vm.natureabs = NatureAbs.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.abscence.id !== null) {
                Abscence.update(vm.abscence, onSaveSuccess, onSaveError);
            } else {
                Abscence.save(vm.abscence, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:abscenceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateD = false;
        vm.datePickerOpenStatus.dateF = false;

        vm.setDoc = function ($file, abscence) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        abscence.doc = base64Data;
                        abscence.docContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
