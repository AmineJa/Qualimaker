(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DocumenExterneDetailController', DocumenExterneDetailController);

    DocumenExterneDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'DocumenExterne', 'Employe', 'Origine', 'LieuxClassement', 'TypeDocumentation'];

    function DocumenExterneDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, DocumenExterne, Employe, Origine, LieuxClassement, TypeDocumentation) {
        var vm = this;

        vm.documenExterne = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('qualiMakerApp:documenExterneUpdate', function(event, result) {
            vm.documenExterne = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
