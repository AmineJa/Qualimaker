(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EnregistrementDialogController', EnregistrementDialogController);

    EnregistrementDialogController.$inject = ['$timeout', 'Principal','$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Enregistrement', 'Employe'];

    function EnregistrementDialogController ($timeout,Principal, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Enregistrement, Employe) {
        var vm = this;
        vm.settingsAccount = null;
        vm.success = null;
        vm.enregistrement = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.e=[];
        vm.employes = Employe.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.enregistrement.id !== null) {
                Enregistrement.update(vm.enregistrement, onSaveSuccess, onSaveError);
            } else {
                vm.enregistrement.date=new Date();

                for(var i=0;i<vm.employes.length;i++){
                    if(vm.employes[i].email==vm.settingsAccount.email){
                        /*console.log(vm.employes[i].id);
                        angular.equals(vm.enregistrement.employe,vm.employes[i]);
                        for(var k=0;k< vm.enregistrement;k++){
                        console.log(vm.enregistrement.employe);
                        console.log("gggg");}*/
                        //console.log(vm.enregistrement.employe);
                        vm.enregistrement.emp=vm.employes[i].nom+" "+vm.employes[i].prenom;
                        console.log(vm.enregistrement.emp);
                    }
                }
                Enregistrement.save(vm.enregistrement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:enregistrementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setFichier = function ($file, enregistrement) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        enregistrement.fichier = base64Data;
                        enregistrement.fichierContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
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
