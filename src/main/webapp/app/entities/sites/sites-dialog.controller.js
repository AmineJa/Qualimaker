(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('SitesDialogController', SitesDialogController);

    SitesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sites'];

    function SitesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Sites) {
        var vm = this;

        vm.sites = entity;
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
            if (vm.sites.id !== null) {
                Sites.update(vm.sites, onSaveSuccess, onSaveError);
            } else {
                Sites.save(vm.sites, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:sitesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
