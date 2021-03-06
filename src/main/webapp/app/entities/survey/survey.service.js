(function() {
    'use strict';
    angular
        .module('rrmApp')
        .factory('Survey', Survey);

    Survey.$inject = ['$resource'];

    function Survey ($resource) {
        var resourceUrl =  'api/surveys/:id';

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
