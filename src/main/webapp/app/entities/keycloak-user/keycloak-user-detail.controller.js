(function() {
    'use strict';

    angular
        .module('msgApp')
        .controller('KeycloakUserDetailController', KeycloakUserDetailController);

    KeycloakUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'KeycloakUser', 'Clients'];

    function KeycloakUserDetailController($scope, $rootScope, $stateParams, previousState, entity, KeycloakUser, Clients) {
        var vm = this;

        vm.keycloakUser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('msgApp:keycloakUserUpdate', function(event, result) {
            vm.keycloakUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
