(function() {
    'use strict';
    angular
        .module('msgApp')
        .factory('KeycloakUser', KeycloakUser);

    KeycloakUser.$inject = ['$resource'];

    function KeycloakUser ($resource) {
        var resourceUrl =  'api/keycloak-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
