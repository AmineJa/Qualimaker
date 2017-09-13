(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CarriereDetailController', CarriereDetailController);

    CarriereDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Carriere', 'Employe', 'TypeContrat'];

    function CarriereDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Carriere, Employe, TypeContrat) {
        var vm = this;

        vm.carriere = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('qualiMakerApp:carriereUpdate', function(event, result) {
            vm.carriere = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
