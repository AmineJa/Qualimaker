(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ServicepostDialogController', ServicepostDialogController);

    ServicepostDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Servicepost', 'Serviice', 'Poste'];

    function ServicepostDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Servicepost, Serviice, Poste) {
        var vm = this;

        vm.servicepost = entity;
        vm.clear = clear;
        vm.save = save;
        vm.serviices = Serviice.query();
        vm.postes = Poste.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.servicepost.id !== null) {
                Servicepost.update(vm.servicepost, onSaveSuccess, onSaveError);
            } else {
                Servicepost.save(vm.servicepost, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:servicepostUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
