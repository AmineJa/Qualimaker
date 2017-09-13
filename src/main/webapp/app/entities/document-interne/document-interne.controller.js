(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DocumentInterneController', DocumentInterneController);

    DocumentInterneController.$inject = ['$state','Principal', 'DataUtils','sharedProperties', 'DocumentInterne', 'DocumentInterneSearch', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function DocumentInterneController($state,Principal, DataUtils,sharedProperties, DocumentInterne, DocumentInterneSearch, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;
        vm.result=null;
        vm.settingsAccount = null;
        vm.success = null;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.load=loadredacteur;
        vm.red=[];
        vm.v=[];
        vm.x=[];
         vm.list =[];

        vm.listverificateur=listverificateur;
        vm.doc=DocumentInterne.query();
        vm.getpropriety=sharedProperties.getProperty();



        loadAll();
   /* function listverif(verif) {

    for(var i=0;i<=verif.length;i++){

        vm.list.push(verif[i].id);
    }



}*/
        function loadAll () {
            if (pagingParams.search) {
                DocumentInterneSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                DocumentInterne.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.documentInternes = data;
                 for(var i=0; i<vm.documentInternes.length; i++) {
                     vm.v=DocumentInterne.get({id: vm.documentInternes[i].id});
                     vm.v.$promise.then(function(data) {
                         for (var j = 0; j < data.redacteurs.length; j++) {
                             console.log("Les redacteurs");
                             console.log(data.redacteurs[j].employe.nom + "  " + data.redacteurs[j].employe.prenom);
                         }

                         for (var a = 0; a < data.verificateurs.length; a++) {
                             console.log("les verificateurs");
                             console.log(data.verificateurs[a].employe.nom + "  " + data.verificateurs[a].employe.prenom);
                         }
                         });
                 }
                 

                 /* vm.v= DocumentInterne._find(9201);*/
                    vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }


        }

        function listverificateur(id) {
                vm.v=DocumentInterne.get({id: id});
                //console.log(vm.v);
                vm.v.$promise.then(function(data) {

                    for (var a = 0; a < data.verificateurs.length; a++) {
                        vm.result=false;
                        //console.log( vm.result);
                        if (vm.settingsAccount.email == data.verificateurs[a].employe.email){
                      return true;

                            //console.log(data.verificateurs[a].employe.email);
                        }

                        /*console.log("les verificateurs");
                        console.log(data.verificateurs[a].employe.nom + "  " + data.verificateurs[a].employe.prenom);*/
                    }
                });

        }



      function loadredacteur() {
            var query=DocumentInterne.query();

          vm.red= query.redac;



}
        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search(searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear() {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login,
                authorities:account.authorities
            };
        };

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
        });

        function save () {
            Auth.updateAccount(vm.settingsAccount).then(function() {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function(account) {
                    vm.settingsAccount = copyAccount(account);
                });
                JhiLanguageService.getCurrent().then(function(current) {
                    if (vm.settingsAccount.langKey !== current) {
                        $translate.use(vm.settingsAccount.langKey);
                    }
                });
            }).catch(function() {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }


    }
})();
