(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DocumentInterneDetailController', DocumentInterneDetailController);

    DocumentInterneDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'DocumentInterne', 'Sites', 'Processus', 'TypeDocumentation', 'DroitaccesDocument'];

    function DocumentInterneDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, DocumentInterne, Sites, Processus, TypeDocumentation, DroitaccesDocument) {
        var vm = this;

        vm.documentInterne = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('qualiMakerApp:documentInterneUpdate', function(event, result) {
            vm.documentInterne = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
