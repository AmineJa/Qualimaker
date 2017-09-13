(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EventsController', EventsController)
        .directive('fullCalendar',fullCalendar);

    fullCalendar.$inject = ['Events', '$log', '$timeout'];
   EventsController.$inject = ['$scope','$log','$state', 'DataUtils', 'Events', 'EventsSearch', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];
    function fullCalendar (Events, $log, $timeout) {

        return {
            restrict: 'E',
            replace: true,
            templateUrl: 'app/entities/events/full-calendar.tpl.html',
            scope: {
                events: "=events"
            },
            link: function (scope, element) {


                var $calendar = $("#calendar");

                var calendar = null;


                function initCalendar() {

                    // $log.log(events);


                    calendar = $calendar.fullCalendar({
                        lang: 'en',
                        editable: true,
                        draggable: true,
                        selectable: false,
                        selectHelper: true,
                        unselectAuto: false,
                        disableResizing: false,
                        droppable: true,

                        header: {
                            left: 'prev', //,today
                            center: 'title , today',
                           right: 'next' //month, agendaDay,*/
                        },

                        drop: function (date, allDay) { // this function is called when something is dropped

                            // retrieve the dropped element's stored Event Object
                            var originalEventObject = $(this).data('eventObject');

                            // we need to copy it, so that multiple events don't have a reference to the same object
                            var copiedEventObject = $.extend({}, originalEventObject);

                            // assign it the date that was reported
                            copiedEventObject.start = date;
                            copiedEventObject.allDay = allDay;

                            // $log.log(scope);

                            // render the event on the calendar
                            // the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
                            $('#calendar').fullCalendar('renderEvent', copiedEventObject, true);

                            // is the "remove after drop" checkbox checked?
                            if ($('#drop-remove').is(':checked')) {

                                // if so, remove the element from the "Draggable Events" list
                                // $(this).remove();
                                // $log.log($(this).scope());
                                var index = $(this).scope().$index;

                                $("#external-events").scope().eventsExternal.splice(index, 1);
                                $(this).remove();

                            }

                        },

                        select: function (start, end, allDay) {
                            var title = prompt('Event Title:');
                            if (title) {
                                calendar.fullCalendar('renderEvent', {
                                        title: title,
                                        start: start,
                                        end: end,
                                        allDay: allDay
                                    }, true // make the event "stick"
                                );
                            }
                            calendar.fullCalendar('unselect');
                        },

                        // events: scope.events,

                        events: function(start, end, timezone, callback) {

                            callback(scope.events);

                        },

                        eventRender: function (event, element, icon) {
                            if (!event.description == "") {
                                element.find('.fc-event-title').append("<br/><span class='ultra-light'>" + event.description + "</span>");
                            }
                            if (!event.icon == "") {
                                element.find('.fc-event-title').append("<i class='air air-top-right fa " + event.icon + " '></i>");
                            }
                        }
                    });

                    $('.fc-header-right, .fc-header-center', $calendar).hide();
                }


                initCalendar();


                // Now events will be refetched every time events scope is updated in controller!!!
                scope.$watch("events", function(newValue, oldValue) {

                    $calendar.fullCalendar( 'refetchEvents' );

                }, true);


                scope.next = function () {
                    $('.fc-button-next', $calendar).click();
                };
                scope.prev = function () {
                    $('.fc-button-prev', $calendar).click();
                };
                scope.today = function () {
                    $('.fc-button-today', $calendar).click();
                };
               scope.changeView = function (period) {
                    $calendar.fullCalendar('changeView', period);
                };
            }
        }
    };
    function EventsController($scope, $log,$state, DataUtils, Events, EventsSearch, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.calender=calender;
        vm.liste=false;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        loadAll();
     // Events scope
     $scope.events = [];



     function calender(valeur) {
         vm.liste=valeur;
        console.log(vm.liste);
     }

     // Unassigned events scope
     $scope.eventsExternal = [
     {
     title: "Office Meeting",
     description: "Currently busy",
     className: "bg-color-darken txt-color-white",
     icon: "fa-time"
     },
     {
     title: "Lunch Break",
     description: "No Description",
     className: "bg-color-blue txt-color-white",
     icon: "fa-pie"
     },
     {
     title: "URGENT",
     description: "urgent tasks",
     className: "bg-color-red txt-color-white",
     icon: "fa-alert"
     }
     ];


     // Queriing our events from Events resource...
     // Scope update will automatically update the calendar
        Events.query().$promise.then(function (events) {
     $scope.events = events;
     });


     $scope.newEvent = {};

     $scope.addEvent = function() {

     $log.log("Adding new event:", $scope.newEvent);

     var newEventDefaults = {
     title: "Untitled Event",
     description: "no description",
     className: "bg-color-darken txt-color-white",
     icon: "fa-info"
     };


     $scope.newEvent = angular.extend(newEventDefaults, $scope.newEvent);

     $scope.eventsExternal.unshift($scope.newEvent);

     $scope.newEvent = {};

     // $log.log("New events now:", $scope.eventsExternal);

     };




        function loadAll () {
            if (pagingParams.search) {
                EventsSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()

                }, onSuccess, onError);
            } else {
                Events.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.events = data;
                vm.page = pagingParams.page;

            }
            vm.liste=false;
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search(searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear() {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }
    }
})();
