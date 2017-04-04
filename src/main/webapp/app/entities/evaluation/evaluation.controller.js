(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EvaluationController', EvaluationController);

    EvaluationController.$inject = ['Evaluation', 'EvaluationSearch'];

    function EvaluationController(Evaluation, EvaluationSearch) {

        var vm = this;

        vm.evaluations = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Evaluation.query(function(result) {
                vm.evaluations = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EvaluationSearch.query({query: vm.searchQuery}, function(result) {
                vm.evaluations = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
