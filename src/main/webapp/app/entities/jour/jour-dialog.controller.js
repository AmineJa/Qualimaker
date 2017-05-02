(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('JourDialogController', JourDialogController);

    JourDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Jour', 'Programme', 'Calendrier'];

    function JourDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Jour, Programme, Calendrier) {
        var vm = this;

        vm.jour = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.programmes = Programme.query({filter: 'jour-is-null'});
        $q.all([vm.jour.$promise, vm.programmes.$promise]).then(function() {
            if (!vm.jour.programme || !vm.jour.programme.id) {
                return $q.reject();
            }
            return Programme.get({id : vm.jour.programme.id}).$promise;
        }).then(function(programme) {
            vm.programmes.push(programme);
        });
        vm.calendriers = Calendrier.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.jour.id !== null) {
                Jour.update(vm.jour, onSaveSuccess, onSaveError);
            } else {
                Jour.save(vm.jour, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:jourUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.jour = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
