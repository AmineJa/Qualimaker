(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FichierjointDialogController', FichierjointDialogController);

    FichierjointDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Fichierjoint'];

    function FichierjointDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Fichierjoint) {
        var vm = this;

        vm.fichierjoint = entity;
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
            if (vm.fichierjoint.id !== null) {
                Fichierjoint.update(vm.fichierjoint, onSaveSuccess, onSaveError);
            } else {
                Fichierjoint.save(vm.fichierjoint, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:fichierjointUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setFichejoint = function ($file, fichierjoint) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        fichierjoint.fichejoint = base64Data;
                        fichierjoint.fichejointContentType = $file.type;
                    });
                });
            }
        };

    }
})();
