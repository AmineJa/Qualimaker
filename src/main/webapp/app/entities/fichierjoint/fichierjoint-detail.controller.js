(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FichierjointDetailController', FichierjointDetailController);

    FichierjointDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Fichierjoint'];

    function FichierjointDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Fichierjoint) {
        var vm = this;

        vm.fichierjoint = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('qualiMakerApp:fichierjointUpdate', function(event, result) {
            vm.fichierjoint = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
