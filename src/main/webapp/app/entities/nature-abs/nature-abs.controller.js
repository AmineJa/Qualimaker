(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('NatureAbsController', NatureAbsController);

    NatureAbsController.$inject = ['NatureAbs', 'NatureAbsSearch'];

    function NatureAbsController(NatureAbs, NatureAbsSearch) {

        var vm = this;

        vm.natureAbs = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            NatureAbs.query(function(result) {
                vm.natureAbs = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            NatureAbsSearch.query({query: vm.searchQuery}, function(result) {
                vm.natureAbs = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
