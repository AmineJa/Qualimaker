(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('OrigineDetailController', OrigineDetailController);

    OrigineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Origine'];

    function OrigineDetailController($scope, $rootScope, $stateParams, previousState, entity, Origine) {
        var vm = this;

        vm.origine = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:origineUpdate', function(event, result) {
            vm.origine = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
