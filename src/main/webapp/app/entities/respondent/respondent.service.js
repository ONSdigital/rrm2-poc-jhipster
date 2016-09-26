(function() {
    'use strict';
    angular
        .module('rrmApp')
        .factory('Respondent', Respondent);

    Respondent.$inject = ['$resource'];

    function Respondent ($resource) {
        var resourceUrl =  'api/respondents/:id';

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
