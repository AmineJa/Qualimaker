(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EventsDialogController', EventsDialogController);

    EventsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Events'];

    function EventsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Events) {
        var vm = this;

        vm.events = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.events.id !== null) {
                Events.update(vm.events, onSaveSuccess, onSaveError);
            } else {
                Events.save(vm.events, onSaveSuccess, onSaveError);
                vm.liste==true;
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:eventsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.start = false;
        vm.datePickerOpenStatus.end = false;

        vm.setIcon = function ($file, events) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        events.icon = base64Data;
                        events.iconContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
