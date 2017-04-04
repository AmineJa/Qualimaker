(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DomaineCompetenceDetailController', DomaineCompetenceDetailController);

    DomaineCompetenceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DomaineCompetence'];

    function DomaineCompetenceDetailController($scope, $rootScope, $stateParams, previousState, entity, DomaineCompetence) {
        var vm = this;

        vm.domaineCompetence = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:domaineCompetenceUpdate', function(event, result) {
            vm.domaineCompetence = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
