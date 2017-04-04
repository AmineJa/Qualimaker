(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CritereevaluationDialogController', CritereevaluationDialogController);

    CritereevaluationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Critereevaluation'];

    function CritereevaluationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Critereevaluation) {
        var vm = this;

        vm.critereevaluation = entity;
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
            if (vm.critereevaluation.id !== null) {
                Critereevaluation.update(vm.critereevaluation, onSaveSuccess, onSaveError);
            } else {
                Critereevaluation.save(vm.critereevaluation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:critereevaluationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
