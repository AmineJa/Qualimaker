(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('AbscenceDetailController', AbscenceDetailController);

    AbscenceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Abscence', 'Employe', 'NatureAbs'];

    function AbscenceDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Abscence, Employe, NatureAbs) {
        var vm = this;

        vm.abscence = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('qualiMakerApp:abscenceUpdate', function(event, result) {
            vm.abscence = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
