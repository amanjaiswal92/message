(function() {
    'use strict';

    angular
        .module('msgApp')
        .controller('KeycloakUserDeleteController',KeycloakUserDeleteController);

    KeycloakUserDeleteController.$inject = ['$uibModalInstance', 'entity', 'KeycloakUser'];

    function KeycloakUserDeleteController($uibModalInstance, entity, KeycloakUser) {
        var vm = this;

        vm.keycloakUser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            KeycloakUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
