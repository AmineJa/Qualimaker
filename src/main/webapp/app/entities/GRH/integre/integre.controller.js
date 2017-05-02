(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('IntegreController', IntegreController);

    IntegreController.$inject = ['Integre', 'IntegreSearch'];

    function IntegreController(Integre, IntegreSearch) {

        var vm = this;

        vm.integres = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Integre.query(function(result) {
                vm.integres = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            IntegreSearch.query({query: vm.searchQuery}, function(result) {
                vm.integres = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
