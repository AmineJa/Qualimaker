(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EnregistrementDetailController', EnregistrementDetailController);

    EnregistrementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Enregistrement'];

    function EnregistrementDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Enregistrement) {
        var vm = this;

        vm.enregistrement = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('qualiMakerApp:enregistrementUpdate', function(event, result) {
            vm.enregistrement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
