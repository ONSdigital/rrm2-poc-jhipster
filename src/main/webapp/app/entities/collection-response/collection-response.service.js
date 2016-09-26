(function() {
    'use strict';
    angular
        .module('rrmApp')
        .factory('CollectionResponse', CollectionResponse);

    CollectionResponse.$inject = ['$resource'];

    function CollectionResponse ($resource) {
        var resourceUrl =  'api/collection-responses/:id';

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
