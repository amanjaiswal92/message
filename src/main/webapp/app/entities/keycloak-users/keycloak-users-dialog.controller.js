(function() {
    'use strict';

    angular
        .module('msgApp')
        .controller('Keycloak_usersDialogController', Keycloak_usersDialogController);

    Keycloak_usersDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Keycloak_users'];

    function Keycloak_usersDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Keycloak_users) {
        var vm = this;

        vm.keycloak_users = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.keycloak_users.id !== null) {
                Keycloak_users.update(vm.keycloak_users, onSaveSuccess, onSaveError);
            } else {
                Keycloak_users.save(vm.keycloak_users, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('msgApp:keycloak_usersUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
