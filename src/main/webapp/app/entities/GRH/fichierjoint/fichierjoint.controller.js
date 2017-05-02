(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FichierjointController', FichierjointController);

    FichierjointController.$inject = ['DataUtils', 'Fichierjoint', 'FichierjointSearch'];

    function FichierjointController(DataUtils, Fichierjoint, FichierjointSearch) {

        var vm = this;

        vm.fichierjoints = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Fichierjoint.query(function(result) {
                vm.fichierjoints = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FichierjointSearch.query({query: vm.searchQuery}, function(result) {
                vm.fichierjoints = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
