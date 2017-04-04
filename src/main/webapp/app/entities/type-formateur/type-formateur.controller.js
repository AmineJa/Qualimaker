(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeFormateurController', TypeFormateurController);

    TypeFormateurController.$inject = ['TypeFormateur', 'TypeFormateurSearch'];

    function TypeFormateurController(TypeFormateur, TypeFormateurSearch) {

        var vm = this;

        vm.typeFormateurs = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TypeFormateur.query(function(result) {
                vm.typeFormateurs = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TypeFormateurSearch.query({query: vm.searchQuery}, function(result) {
                vm.typeFormateurs = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
