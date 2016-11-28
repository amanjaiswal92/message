(function() {
    'use strict';

    angular
        .module('msgApp')
        .controller('Keycloak_usersDeleteController',Keycloak_usersDeleteController);

    Keycloak_usersDeleteController.$inject = ['$uibModalInstance', 'entity', 'Keycloak_users'];

    function Keycloak_usersDeleteController($uibModalInstance, entity, Keycloak_users) {
        var vm = this;

        vm.keycloak_users = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Keycloak_users.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
