(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FormateurDialogController', FormateurDialogController);

    FormateurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Formateur'];

    function FormateurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Formateur) {
        var vm = this;

        vm.formateur = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.formateur.id !== null) {
                Formateur.update(vm.formateur, onSaveSuccess, onSaveError);
            } else {
                Formateur.save(vm.formateur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:formateurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setCv = function ($file, formateur) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        formateur.cv = base64Data;
                        formateur.cvContentType = $file.type;
                    });
                });
            }
        };

    }
})();
