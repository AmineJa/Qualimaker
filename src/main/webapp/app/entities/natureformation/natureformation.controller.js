(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('NatureformationController', NatureformationController);

    NatureformationController.$inject = ['Natureformation', 'NatureformationSearch'];

    function NatureformationController(Natureformation, NatureformationSearch) {

        var vm = this;

        vm.natureformations = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Natureformation.query(function(result) {
                vm.natureformations = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            NatureformationSearch.query({query: vm.searchQuery}, function(result) {
                vm.natureformations = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
