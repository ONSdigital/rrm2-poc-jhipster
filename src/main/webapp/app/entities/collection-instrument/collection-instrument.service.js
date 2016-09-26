(function() {
    'use strict';
    angular
        .module('rrmApp')
        .factory('CollectionInstrument', CollectionInstrument);

    CollectionInstrument.$inject = ['$resource'];

    function CollectionInstrument ($resource) {
        var resourceUrl =  'api/collection-instruments/:id';

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
