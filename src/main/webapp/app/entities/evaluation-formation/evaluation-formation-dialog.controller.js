(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EvaluationFormationDialogController', EvaluationFormationDialogController);

    EvaluationFormationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EvaluationFormation', 'Formation'];

    function EvaluationFormationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EvaluationFormation, Formation) {
        var vm = this;

        vm.evaluationFormation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.formations = Formation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.evaluationFormation.id !== null) {
                EvaluationFormation.update(vm.evaluationFormation, onSaveSuccess, onSaveError);
            } else {
                EvaluationFormation.save(vm.evaluationFormation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:evaluationFormationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
