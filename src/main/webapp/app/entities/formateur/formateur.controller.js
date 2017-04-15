(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FormateurController', FormateurController);

    FormateurController.$inject = ['DataUtils', 'Formateur', 'FormateurSearch'];

    function FormateurController(DataUtils, Formateur, FormateurSearch) {

        var vm = this;

        vm.formateurs = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Formateur.query(function(result) {
                vm.formateurs = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FormateurSearch.query({query: vm.searchQuery}, function(result) {
                vm.formateurs = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
