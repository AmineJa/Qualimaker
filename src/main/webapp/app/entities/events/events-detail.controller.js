(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EventsDetailController', EventsDetailController);

    EventsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Events'];

    function EventsDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Events) {
        var vm = this;

        vm.events = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('qualiMakerApp:eventsUpdate', function(event, result) {
            vm.events = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
