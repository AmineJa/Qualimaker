(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ServiiceDialogController', ServiiceDialogController);

    ServiiceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Serviice', 'Poste', 'Servicepost'];

    function ServiiceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Serviice, Poste, Servicepost) {
        var vm = this;

        vm.serviice = entity;
        vm.clear = clear;
        vm.save = save;
        vm.postes = Poste.query();
        vm.serviceposts = Servicepost.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.serviice.id !== null) {
                Serviice.update(vm.serviice, onSaveSuccess, onSaveError);
            } else {
                Serviice.save(vm.serviice, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:serviiceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
