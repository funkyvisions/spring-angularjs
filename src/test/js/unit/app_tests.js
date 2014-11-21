toastr = {
    success: function (msg) {
        console.log(msg);
    }
}
describe('Controllers', function () {
    beforeEach(module('sprang.services'));

    beforeEach(function () {
        this.addMatchers({
            toEqualData: function (expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    describe('BookListController', function () {
        var scope, controller, http;
        beforeEach(inject(function ($httpBackend, $rootScope, $controller) {
            http = $httpBackend;
            http.expectGET('/api/books').respond([
                {
                    id: 1,
                    title: "Pro Spring",
                    author: {
                        firstName: "Rob",
                        lastName: "Harrop"
                    }
                }
            ]);
            scope = $rootScope.$new();
            controller = $controller(BookListController, {$scope: scope});
        }));

        it('should load the Book from the API', function () {
            expect(scope.books).toEqual([]);
            http.flush();
            expect(scope.books.length).toEqual(1);
            expect(scope.books[0].title).toEqual("Pro Spring");
        });

        it('should allow for books to be deleted', function () {
            expect(scope.books).toEqual([]);
            http.flush();

            var book = scope.books[0];

            http.expectDELETE('/api/books/' + book.id).respond(204);
            scope.deleteBook(book);
            http.flush();

            expect(scope.books).toEqual([]);
        });
    });

    describe('BookDetailController', function () {
        var scope, controller, http, book;

        beforeEach(inject(function ($rootScope, $httpBackend) {
            scope = $rootScope.$new();
            http = $httpBackend;
            book = {
                id: 1,
                title: "Pro Spring",
                author: {
                    firstName: "Rob",
                    lastName: "Harrop"
                }
            };
        }));

        describe('adding new books', function () {
            it('should save the new book', inject(function ($location, $controller, Book) {
                $controller(BookDetailController, {$scope: scope, $routeParams: {bookId: 'new'}});

                book.id = undefined;
                scope.book = new Book(book);

                http.expectPOST('/api/books', book).respond(201, '', {'Location': '/api/books/100'});
                scope.save();
                http.flush();

                expect($location.path()).toEqual('/books/100');
            }));
        });

        describe('updating books', function () {

            beforeEach(inject(function ($controller) {
                http.expectGET('/api/books/1').respond(book);
                $controller(BookDetailController, {$scope: scope, $routeParams: {bookId: 1}});
            }));

            it('should load books for editing', function () {
                expect(scope.book).toEqualData({});
                http.flush();
                expect(scope.book).toEqualData(book);
            });

            it('should allow books to be edited', function () {
                http.flush();
                http.expectPUT('/api/books/1', book).respond(204);
                scope.save();
                http.flush();
            });
        });
    });
});