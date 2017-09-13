(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CongeDialogController', CongeDialogController);

    CongeDialogController.$inject = ['$timeout','Principal' ,'$scope', '$stateParams', '$uibModalInstance', 'entity', 'Conge', 'Employe', 'TypeConge'];

    function CongeDialogController ($timeout,Principal, $scope, $stateParams, $uibModalInstance, entity, Conge, Employe, TypeConge) {
        var vm = this;

        vm.settingsAccount = null;
        vm.success = null;

        vm.refresh=refresh;
        vm.conge = entity;
        vm.accepter=accepter;
        vm.refuser=refuser;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.employes = Employe.query();
        vm.typeconges = TypeConge.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.conge.id !== null) {
                Conge.update(vm.conge, onSaveSuccess, onSaveError);
            } else {
                vm.conge.statut = "Enattente"
                Conge.save(vm.conge, onSaveSuccess, onSaveError);

            }
        }

            function refresh() {
                  vm.loadAll();
        vm.clear();}
        function accepter () {
            vm.isSaving = true;

            vm.conge.statut ="Accepter";
            Conge.update(vm.conge, onSaveSuccess, onSaveError);



        }
        function refuser () {
            vm.isSaving = true;

            vm.conge.statut ="Refuser";
            Conge.update(vm.conge, onSaveSuccess, onSaveError);

        }
        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:congeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateD = false;
        vm.datePickerOpenStatus.dateF = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        vm.settingsAccount = null;
        vm.success = null;







    var copyAccount = function (account) {
        return {
            activated: account.activated,
            email: account.email,
            firstName: account.firstName,
            langKey: account.langKey,
            lastName: account.lastName,
            login: account.login
        };
    };

    Principal.identity().then(function(account) {
        vm.settingsAccount = copyAccount(account);
    });

    function savee () {
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
