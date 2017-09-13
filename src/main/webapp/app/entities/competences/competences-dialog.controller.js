(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CompetencesDialogController', CompetencesDialogController);

    CompetencesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Competences', 'Employe'];

    function CompetencesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Competences, Employe) {
        var vm = this;

        vm.competences = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
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
            if (vm.competences.id !== null) {
                Competences.update(vm.competences, onSaveSuccess, onSaveError);
            } else {
                Competences.save(vm.competences, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:competencesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
