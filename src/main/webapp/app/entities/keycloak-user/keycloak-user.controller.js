(function() {
    'use strict';

    angular
        .module('msgApp')
        .controller('KeycloakUserController', KeycloakUserController);

    KeycloakUserController.$inject = ['$scope', '$state', 'KeycloakUser'];

    function KeycloakUserController ($scope, $state, KeycloakUser) {
        var vm = this;
        
        vm.keycloakUsers = [];

        loadAll();

        function loadAll() {
            KeycloakUser.query(function(result) {
                vm.keycloakUsers = result;
            });
        }
    }
})();
