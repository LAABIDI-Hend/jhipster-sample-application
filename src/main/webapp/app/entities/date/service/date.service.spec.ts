import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDate } from '../date.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../date.test-samples';

import { DateService } from './date.service';

const requireRestSample: IDate = {
  ...sampleWithRequiredData,
};

describe('Date Service', () => {
  let service: DateService;
  let httpMock: HttpTestingController;
  let expectedResult: IDate | IDate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DateService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Date', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const date = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(date).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Date', () => {
      const date = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(date).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Date', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Date', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Date', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDateToCollectionIfMissing', () => {
      it('should add a Date to an empty array', () => {
        const date: IDate = sampleWithRequiredData;
        expectedResult = service.addDateToCollectionIfMissing([], date);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(date);
      });

      it('should not add a Date to an array that contains it', () => {
        const date: IDate = sampleWithRequiredData;
        const dateCollection: IDate[] = [
          {
            ...date,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDateToCollectionIfMissing(dateCollection, date);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Date to an array that doesn't contain it", () => {
        const date: IDate = sampleWithRequiredData;
        const dateCollection: IDate[] = [sampleWithPartialData];
        expectedResult = service.addDateToCollectionIfMissing(dateCollection, date);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(date);
      });

      it('should add only unique Date to an array', () => {
        const dateArray: IDate[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const dateCollection: IDate[] = [sampleWithRequiredData];
        expectedResult = service.addDateToCollectionIfMissing(dateCollection, ...dateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const date: IDate = sampleWithRequiredData;
        const date2: IDate = sampleWithPartialData;
        expectedResult = service.addDateToCollectionIfMissing([], date, date2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(date);
        expect(expectedResult).toContain(date2);
      });

      it('should accept null and undefined values', () => {
        const date: IDate = sampleWithRequiredData;
        expectedResult = service.addDateToCollectionIfMissing([], null, date, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(date);
      });

      it('should return initial array if no Date is added', () => {
        const dateCollection: IDate[] = [sampleWithRequiredData];
        expectedResult = service.addDateToCollectionIfMissing(dateCollection, undefined, null);
        expect(expectedResult).toEqual(dateCollection);
      });
    });

    describe('compareDate', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDate(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDate(entity1, entity2);
        const compareResult2 = service.compareDate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDate(entity1, entity2);
        const compareResult2 = service.compareDate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDate(entity1, entity2);
        const compareResult2 = service.compareDate(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
