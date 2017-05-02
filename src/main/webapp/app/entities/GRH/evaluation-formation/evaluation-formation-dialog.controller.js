(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EvaluationFormationDialogController', EvaluationFormationDialogController);

    EvaluationFormationDialogController.$inject = ['$timeout', 'Principal','$scope', '$stateParams', '$uibModalInstance', 'entity', 'EvaluationFormation', 'Formation'];

    function EvaluationFormationDialogController ($timeout,Principal, $scope, $stateParams, $uibModalInstance, entity, EvaluationFormation, Formation) {
        var vm = this;

        vm.evaluationFormation = entity;
        vm.clear = clear;
        vm.savee = savee;
        vm.formations = Formation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function savee () {
            vm.isSaving = true;
            if (vm.evaluationFormation.id !== null) {
                EvaluationFormation.update(vm.evaluationFormation, onSaveSuccess, onSaveError);
            } else {

                 vm.evaluationFormation.evaluerpar=vm.settingsAccount.firstName +' '+ vm.settingsAccount.lastName;
                EvaluationFormation.save(vm.evaluationFormation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:evaluationFormationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.error = null;
        vm.save = save;
        vm.settingsAccount = null;
        vm.success = null;

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
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
