(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FormateurDetailController', FormateurDetailController);

    FormateurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Formateur'];

    function FormateurDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Formateur) {
        var vm = this;

        vm.formateur = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('qualiMakerApp:formateurUpdate', function(event, result) {
            vm.formateur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
