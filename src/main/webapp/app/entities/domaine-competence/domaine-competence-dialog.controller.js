(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DomaineCompetenceDialogController', DomaineCompetenceDialogController);

    DomaineCompetenceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DomaineCompetence'];

    function DomaineCompetenceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DomaineCompetence) {
        var vm = this;

        vm.domaineCompetence = entity;
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
            if (vm.domaineCompetence.id !== null) {
                DomaineCompetence.update(vm.domaineCompetence, onSaveSuccess, onSaveError);
            } else {
                DomaineCompetence.save(vm.domaineCompetence, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:domaineCompetenceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
