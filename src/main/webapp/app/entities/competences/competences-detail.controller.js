(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CompetencesDetailController', CompetencesDetailController);

    CompetencesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Competences', 'Employe'];

    function CompetencesDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Competences, Employe) {
        var vm = this;

        vm.competences = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('qualiMakerApp:competencesUpdate', function(event, result) {
            vm.competences = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
