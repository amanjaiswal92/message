(function() {
    'use strict';
    angular
        .module('msgApp')
        .factory('InboundMessages', InboundMessages);

    InboundMessages.$inject = ['$resource'];

    function InboundMessages ($resource) {
        var resourceUrl =  'api/inbound-messages/:id';

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
