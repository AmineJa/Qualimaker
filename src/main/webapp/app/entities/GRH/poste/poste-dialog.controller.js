(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('PosteDialogController', PosteDialogController);

    PosteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Poste', 'Serviice', 'Servicepost'];

    function PosteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Poste, Serviice, Servicepost) {
        var vm = this;

        vm.poste = entity;
        vm.clear = clear;
        vm.save = save;
        vm.serviices = Serviice.query();
        vm.serviceposts = Servicepost.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.poste.id !== null) {
                Poste.update(vm.poste, onSaveSuccess, onSaveError);
            } else {
                Poste.save(vm.poste, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:posteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
