(function() {
    'use strict';

    angular
        .module('msgApp')
        .controller('Keycloak_usersController', Keycloak_usersController);

    Keycloak_usersController.$inject = ['$scope', '$state', 'Keycloak_users'];

    function Keycloak_usersController ($scope, $state, Keycloak_users) {
        var vm = this;
        
        vm.keycloak_users = [];

        loadAll();

        function loadAll() {
            Keycloak_users.query(function(result) {
                vm.keycloak_users = result;
            });
        }
    }
})();
