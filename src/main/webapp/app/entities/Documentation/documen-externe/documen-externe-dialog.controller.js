(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DocumenExterneDialogController', DocumenExterneDialogController);

    DocumenExterneDialogController.$inject = ['$timeout','Principal', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'DocumenExterne', 'Employe', 'Origine', 'LieuxClassement', 'TypeDocumentation'];

    function DocumenExterneDialogController ($timeout,Principal, $scope, $stateParams, $uibModalInstance, DataUtils, entity, DocumenExterne, Employe, Origine, LieuxClassement, TypeDocumentation) {
        var vm = this;

        vm.documenExterne = entity;
        vm.list=[];
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.employes = Employe.query();
        vm.origines = Origine.query();
        vm.lieuxclassements = LieuxClassement.query();
        vm.typedocumentations = TypeDocumentation.query();
         vm.coreBranches=coreBranches;
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.documenExterne.id !== null) {
                DocumenExterne.update(vm.documenExterne, onSaveSuccess, onSaveError);
            } else {
                DocumenExterne.save(vm.documenExterne, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:documenExterneUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.daterevision = false;

        vm.setFichier = function ($file, documenExterne) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        documenExterne.fichier = base64Data;
                        documenExterne.fichierContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }


            function coreBranches() {
                return function (items) {
                    var filtered = [];
                    angular.forEach(items, function (item) {
                        if (item.employe.email=== vm.settingsAccount.email) {
                            filtered.push(item);
                        }
                    });
                    return filtered;
                }
            }



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
            Auth.updateAccount(vm.settingsAccount).then(function () {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function (account) {
                    vm.settingsAccount = copyAccount(account);
                });
                JhiLanguageService.getCurrent().then(function (current) {
                    if (vm.settingsAccount.langKey !== current) {
                        $translate.use(vm.settingsAccount.langKey);
                    }
                });
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }
    }
})();
